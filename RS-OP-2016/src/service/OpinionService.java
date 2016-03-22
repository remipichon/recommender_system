package service;

import model.Feature;
import model.Sentiment;
import util.nlp.Parser;

import java.util.*;

public class OpinionService {
    private static OpinionService instance;

    public static OpinionService getInstance() {
        if (instance == null)
            instance = new OpinionService();

        return instance;
    }

    private Parser parser;

    private OpinionService() {
        parser = Parser.getInstance();
    }

    public void validPattern(List<Feature> features) {
        Hashtable<String, Integer> posPatternOccurrence = new Hashtable<>();

        //pattern frequency
        for (Feature feature : features) {
            if (feature.getSentiment().equals(Sentiment.NEUTRAL)) continue; //no sentiment word were found, there is no pattern available

            if (!posPatternOccurrence.contains(feature.getPosPattern())) {
                posPatternOccurrence.put(feature.getPosPattern(), 0);
            }
            posPatternOccurrence.put(feature.getPosPattern(), posPatternOccurrence.get(feature.getPosPattern()) + 1);
        }

        for (Feature feature : features) {
            if (feature.getSentiment().equals(Sentiment.NEUTRAL)) continue; //no sentiment word were found

            if (posPatternOccurrence.get(feature.getPosPattern()) == 1) {
                //non valid pattern
                feature.setSentiment(Sentiment.NEUTRAL);
            }
        }

    }

    public void negationTerm(List<Feature> features) {

        for (Feature feature : features) {
            if (feature.getSentiment().equals(Sentiment.NEUTRAL)) continue;

            String[] sentenceTokens = parser.getSentenceTokens(feature.getSentence());

            //search for "not" and "n't" which is a RB with 4 words before or after sentiment word
            int from = feature.getSentimentPosition() - 4;
            if(from < 0) from = 0 ;
            List<String> aroundSentimentWord = new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(sentenceTokens, from, feature.getSentimentPosition() + 4 + 2)));
            aroundSentimentWord.removeAll(Collections.singleton(null));

            //ignore if "not only"
            if (aroundSentimentWord.contains("not only")) {
                continue;
            }

            //not
            if (aroundSentimentWord.contains("not")) {
                feature.reverseSentiment();
                continue;
            }

            //n't which is RB (an adverb)
            for (int i = 0; i < aroundSentimentWord.size(); i++) {
                String word = aroundSentimentWord.get(i);
                if (word.endsWith("n't")) {
                        feature.reverseSentiment();
//                    String[] posTags = parser.getPOSTags(sentenceTokens);
//                    String tag = posTags[from + i - 1]; //tag is VBZ...
//                    if (tag == "RB") {
//                        feature.reverseSentiment();
//                        continue;
//                    }
                }
            }
        }

        System.out.println("Negation term done");
    }
}
