package examples;


import model.Feature;
import service.FeatureService;
import service.SentimentService;

import java.util.List;

public class TestExtractSentimentForOneSentence {

    public static void main(String[] args) {

        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();


        // First, extract features
        String sentence = "The macro mode is a rare high-quality but the macro shots are poor and outstanding is the lens.";
        List<Feature> features = featureService.extractBiGramAndFeature(sentence);


        //Then, find sentiment
        features = sentimentService.findClosestSentiment(sentence,features);


        System.out.println("SENTENCE " + sentence);
        for (Feature feature : features) {
            System.out.println(feature);
        }
        

    }


}
