package service;

import model.*;
import util.reader.DatasetReader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class RunnerService {
    private static RunnerService instance;

    public static RunnerService getInstance() {
        if (instance == null)
            instance = new RunnerService();

        return instance;
    }


    public void readDatasetAndGenerateOutputs(String distFolder, String filename, String featureSetFilename, String prefixFileName) {

        ReviewService reviewService = ReviewService.getInstance();
        OpinionService opinionService = OpinionService.getInstance();
        OutputService outputService = OutputService.getInstance();
        FeatureService featureService = FeatureService.getInstance();
        featureService.readBiGramAndFeature(featureSetFilename);



        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class


        Wrapper wrapper = reviewService.extractFeatures(reader.getReviews());
        List<Feature> features = wrapper.features;

        opinionService.validPattern(features);

        opinionService.negationTerm(features);

        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.computeOutputPerProduct(features);
        outputService.computeFeatureCountForPopularity(computeOutputPerProduct,wrapper.featuresPerReview);

        outputService.generativeCSVFiles(distFolder,prefixFileName,computeOutputPerProduct);

        outputService.storeFeatureSummariesPerProduct(computeOutputPerProduct, filename);

        //review count per product
        Map<String, Integer> reviewCountPerProduct = new HashMap<>();
        for (Review review : reader.getReviews()) {
            if (!reviewCountPerProduct.containsKey(review.getProductId()))
                reviewCountPerProduct.put(review.getProductId(), 0);
            reviewCountPerProduct.put(review.getProductId(), reviewCountPerProduct.get(review.getProductId()) + 1);
        }

        outputService.storeReviewCountPerProduct(reviewCountPerProduct, filename);

        reviewService.computeAndStorereviewCountAndMeanRatingPerProduct(reader.getReviews(),filename);
    }

    public void experiment(String filename) {
        OutputService outputService = OutputService.getInstance();
        RecommendationService recommendationService = RecommendationService.getInstance();

        int topN = 10;

        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.restoreFeatureSummariesPerProductFromFile(filename);
        Map<String, Integer> reviewCountPerProduct = outputService.restoreReviewCountPerProductFromFile(filename);
        Map<String, Double> meanRatingPerProduct = outputService.restoreMeanRatingPerProductFromFile(filename);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat)nf;


        List<Product> products = new ArrayList<>();

        for (Map.Entry<String, List<FeatureSummary>> entry : computeOutputPerProduct.entrySet()) {
            String productId = entry.getKey();
            List<FeatureSummary> featureSummaries = entry.getValue();
            products.add(new Product(productId, featureSummaries, reviewCountPerProduct.get(productId), meanRatingPerProduct.get(productId)));
        }

        recommendationService.setSentimentAndPopularity(products);


        System.out.println("w\tmeanRatingLift\tmeanQuerySimilarity");
        for (Double w = 0.0; w <= 1.0; w = w + 0.1) {
            recommendationService.setW(w);

            recommendationService.setRecommendations(products, topN);

            //compute mean rating lift
            Double totalRatingLift = 0.0;
            //compute mean query similarity
            Double totalQuerySimilarity = 0.0;
            for (Product query : products) {
                //rating lift
                Double totalRating = 0.0;
                for(Product recommendation : query.getRecommendations()){
                    totalRating += recommendation.getMeanRating();
                }
                query.setRatingLift(query.getMeanRating() - totalRating / query.getRecommendations().size());
                totalRatingLift += query.getRatingLift();


                //query similarity
                Double totalSimilarity = 0.0;
                for(Product recommendation : query.getRecommendations()){

                    Double similarity = recommendationService.getSimilarity(query, recommendation);
                    totalSimilarity += similarity;
                }

                query.setQuerySimilarity(totalSimilarity /  query.getRecommendations().size());
                totalQuerySimilarity += query.getQuerySimilarity();


            }

            Double meanRatingLift = totalRatingLift / products.size();
            Double meanQuerySimilarity = totalQuerySimilarity / products.size();
            System.out.println(df.format(w) + "\t" + df.format(meanRatingLift) + "\t" + df.format(meanQuerySimilarity));


        }
    }


}
