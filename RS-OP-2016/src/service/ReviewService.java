package service;

import model.*;
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
            if(++cpt % 100 == 0 ) System.out.println("Review reads "+cpt+"/"+reviews.size());
        }

        System.out.println("Features extracted "+features.size());

        return features;
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


}
