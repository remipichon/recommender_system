package service;

import model.Feature;
import model.FeatureSummary;
import model.Review;
import model.Sentiment;
import util.nlp.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Feature> extractFeatures(List<Review> reviews){
        List<Feature> features = new ArrayList<>();


        int cpt = 0;
        for (Review review : reviews) {
            features.addAll(this.extractFeatures(review));
            if(++cpt % 10 == 0 ) System.out.println("Review reads "+cpt+"/"+reviews.size());
        }

        System.out.println("Features extracted "+features.size());

        return features;
    }


    public List<Feature> extractFeatures(Review review){

        List<Feature> reviewFeatures = new ArrayList<>();


        String[] sentences = parser.getSentences(review.getReviewText().replaceAll("<br />", EOL)); // get the sentences

        for (String sentence : sentences) {

            // First, extract features
            List<Feature> features = featureService.extractBiGramAndFeature(review.getProductId(),sentence);

            //Then, find sentiment
            sentimentService.findClosestSentiment(sentence,features);

            reviewFeatures.addAll(features);
        }


        return reviewFeatures;


    }


}
