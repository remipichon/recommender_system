package service;


import model.Feature;
import model.Sentiment;
import util.nlp.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SentimentService {

    private static SentimentService instance;

    public static SentimentService getInstance() {
        if (instance == null)
            instance = new SentimentService();

        return instance;
    }


    private HashSet<String> negativeWords;
    private HashSet<String> positiveWords;
    private Parser parser;

    private SentimentService() {
        negativeWords = new HashSet<>();
        positiveWords = new HashSet<>();
        parser = Parser.getInstance();

        readSentimentLexicons();
    }


    public void readSentimentLexicons() {
        //populate positiveWords and negativeWords;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("sentiment lexicon" + File.separator + "negative-words.txt")));
            String line;

            while ((line = br.readLine()) != null) {
                negativeWords.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("sentiment lexicon" + File.separator + "positive-words.txt")));
            String line;

            while ((line = br.readLine()) != null) {
                positiveWords.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


    public void findClosestSentiment(String sentence, List<Feature> features) {
        String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)


        for (Feature feature : features) {

            //split sentence in two part (before and after feature)
            int biGram = 0;
            if (feature.getName().split(" ").length == 2) //if feature is a biGram, we need to remove it from the 'after' sub array
                biGram = 1;

            String[] before = Arrays.copyOfRange(tokens, 0, feature.getFeaturePosition());
            String[] after;
            if (feature.getFeaturePosition() + 1 + biGram < tokens.length) //if feature is the last word
                after = Arrays.copyOfRange(tokens, feature.getFeaturePosition() + 1 + biGram, tokens.length);
            else
                after = new String[0];


            //search in each part for the first closest sentiment word
            for (int i = 0; i < Math.max(before.length, after.length); i++) {
                if (feature.getSentiment() != null) break; //we just found a sentiment
                //search in before
                if (i < before.length) {
                    String word = before[before.length - 1 - i]; //reverse search
                    if (negativeWords.contains(word)) {
                        feature.setSentiment(Sentiment.NEGATIVE);
                        feature.setSentimentPosition(feature.getFeaturePosition() - i - 1);
                    } else if (positiveWords.contains(word)) {
                        feature.setSentiment(Sentiment.POSITIVE);
                        feature.setSentimentPosition(feature.getFeaturePosition() - i - 1);
                    }
                }

                //search in after
                if (i < after.length) {
                    String word = after[i]; //normal search
                    if (negativeWords.contains(word)) {
                        feature.setSentiment(Sentiment.NEGATIVE);
                        feature.setSentimentPosition(feature.getFeaturePosition() + i + biGram + 1);
                    } else if (positiveWords.contains(word)) {
                        feature.setSentiment(Sentiment.POSITIVE);
                        feature.setSentimentPosition(feature.getFeaturePosition() + i + biGram + 1);
                    }
                }
            }
            if (feature.getSentiment() != null) {
                //we just found a sentiment, lets find the POS pattern
                extractPosPattern(sentence, feature);

            } else {
                //no sentiment, then its neutral
                feature.setSentiment(Sentiment.NEUTRAL);
            }


        }
    }


    public void extractPosPattern(String sentence, Feature feature) {

        String[] tokens = parser.getSentenceTokens(sentence); // get the sentence tokens (words)
        String pos[] = parser.getPOSTags(tokens); // get the POS tag for each sentence token
        String chunks[] = parser.getChunkTags(tokens, pos); // get the chunk tags for the sentence

        int biGram = 0;
        if (feature.getName().split(" ").length == 2) //if feature is a biGram, we need to remove it from the 'after' sub array
            biGram = 1;

        String posPattern;
        String tokenPattern; //debug only
        int featurePosition = feature.getFeaturePosition();
        int sentimentPosition = feature.getSentimentPosition();

        if (featurePosition < sentimentPosition) {
            posPattern = "FEATURE-" + String.join("-", Arrays.copyOfRange(pos, featurePosition + biGram + 1, sentimentPosition + 1));
            tokenPattern = "FEATURE-" + String.join("-", Arrays.copyOfRange(tokens, featurePosition + biGram + 1, sentimentPosition + 1));
        } else {
            posPattern = String.join("-", Arrays.copyOfRange(pos, sentimentPosition, featurePosition)) + "-FEATURE";
            tokenPattern = String.join("-", Arrays.copyOfRange(tokens, sentimentPosition, featurePosition)) + "-FEATURE";
        }


        feature.setPosPattern(posPattern);
    }




}
