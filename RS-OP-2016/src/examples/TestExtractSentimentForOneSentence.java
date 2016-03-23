package examples;


import model.Feature;
import model.Sentence;
import service.FeatureService;
import service.SentimentService;
import util.nlp.Parser;

import java.util.List;

public class TestExtractSentimentForOneSentence {

    public static void main(String[] args) {

        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();

        Parser parser = Parser.getInstance();


        // First, extract features
        String sentence = "The macro mode is a rare high-quality but the macro shots are poor and outstanding is the lens.";

        String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)
        String pos[] = parser.getPOSTags(tokens); // get the POS tag for each sentence token
        String chunks[] = parser.getChunkTags(tokens, pos); // get the chunk tags for the sentence

        Sentence sentenceModel = new Sentence();
        sentenceModel.setTokens(tokens);
        sentenceModel.setPos(pos);
        sentenceModel.setChunks(chunks);

        List<Feature> features = featureService.extractBiGramAndFeature("ONE", sentenceModel);


        //Then, find sentiment
        sentimentService.findClosestSentiment(sentenceModel,features);


        System.out.println("SENTENCE " + sentence);
        for (Feature feature : features) {
            System.out.println(feature);
        }


    }


}
