package runner;

import model.FeatureSummary;
import model.Product;
import service.OutputService;
import service.RecommendationService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Experiment {

    public static void main(String[] args) {
        OutputService outputService = OutputService.getInstance();
        RecommendationService recommendationService = RecommendationService.getInstance();

        int topN = 10;

        String filename = "Digital Camera.txt";

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
