package service;

import model.*;
import sun.text.resources.cldr.et.FormatData_et;
import util.nlp.Parser;

import java.util.*;

public class ReviewService {
    private static ReviewService instance;

    public static ReviewService getInstance() {
        if (instance == null)
            instance = new ReviewService();

        return instance;
    }

    public final static String EOL = System.getProperty("line.separator");
    private Parser parser;
    private FeatureService featureService;
    private SentimentService sentimentService;

    private ReviewService(){
        parser = Parser.getInstance();
        featureService = FeatureService.getInstance();
        sentimentService = SentimentService.getInstance();
    }

    public Wrapper extractFeatures(List<Review> reviews){
        List<Feature> features = new ArrayList<>();
        Map<Review,List<Feature>> featuresPerReview = new HashMap<>();


        int cpt = 0;
        for (Review review : reviews) {
            List<Feature> f = this.extractFeatures(review);
            features.addAll(f);
            featuresPerReview.put(review,f);
            if(++cpt % 100 == 0 ) System.out.println("Review reads "+cpt+"/"+reviews.size());
        }

        System.out.println("Features extracted "+features.size());
        


        return new Wrapper(features,featuresPerReview);
    }


    public List<Feature> extractFeatures(Review review){

        List<Feature> reviewFeatures = new ArrayList<>();

        String[] sentences = parser.getSentences(review.getReviewText().replaceAll("<br />", EOL)); // get the sentences


        for (String sentence : sentences) {

            String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)
            String pos[] = parser.getPOSTags(tokens); // get the POS tag for each sentence token
//            String chunks[] = parser.getChunkTags(tokens, pos); // get the chunk tags for the sentence ==> too long to compute
//
            Sentence sentenceModel = new Sentence();
            sentenceModel.setTokens(tokens);
            sentenceModel.setPos(pos);
//            sentenceModel.setChunks(chunks);

//            First, extract features
            List<Feature> features = featureService.extractBiGramAndFeature(review.getProductId(),sentenceModel);

//            Then, find sentiment
            sentimentService.findClosestSentiment(sentenceModel,features);

            reviewFeatures.addAll(features);
        }


        return reviewFeatures;


    }


    public void computeAndStorereviewCountAndMeanRatingPerProduct(ArrayList<Review> reviews, String filename) {

        Map<String, Integer> reviewCountPerProduct = new HashMap<>();
        Map<String, Double> totalRatingPerProduct = new HashMap<>();
        for (Review review : reviews) {
            //reviewCountPerProduct
            if (!reviewCountPerProduct.containsKey(review.getProductId()))
                reviewCountPerProduct.put(review.getProductId(), 0);
            reviewCountPerProduct.put(review.getProductId(), reviewCountPerProduct.get(review.getProductId()) + 1);

            //meanRatingPerProduct
            if (!totalRatingPerProduct.containsKey(review.getProductId()))
                totalRatingPerProduct.put(review.getProductId(), 0.0);
            totalRatingPerProduct.put(review.getProductId(), totalRatingPerProduct.get(review.getProductId()) + review.getReviewRating());
        }

        Map<String, Double> meanRatingPerProduct = new HashMap<>();
        for (Map.Entry<String, Double> stringDoubleEntry : totalRatingPerProduct.entrySet()) {
            String productId = stringDoubleEntry.getKey();
            Double totalRating = stringDoubleEntry.getValue();
            meanRatingPerProduct.put(productId,totalRating / reviewCountPerProduct.get(productId));
        }

        OutputService outputService = OutputService.getInstance();

        outputService.storeReviewCountPerProduct(reviewCountPerProduct, filename);
        outputService.storeMeanRatingPerProduct(meanRatingPerProduct, filename);

        reviewCountPerProduct = outputService.restoreReviewCountPerProductFromFile(filename);
        meanRatingPerProduct = outputService.restoreMeanRatingPerProductFromFile(filename);

        System.out.println("reviewCountPerProduct");
        System.out.println(Arrays.asList(reviewCountPerProduct));
        System.out.println("meanRatingPerProduct");
        System.out.println(Arrays.asList(meanRatingPerProduct));
    }
}
