package service;


import model.Feature;
import model.Sentence;
import util.nlp.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FeatureService {
    private static FeatureService instance;

    public static FeatureService getInstance() {
        if (instance == null)
            instance = new FeatureService();

        return instance;
    }


    private HashSet<String> features;
    private Map<String, HashSet<String>> biGramMap;
    private Parser parser;

    private FeatureService() {
        features = new HashSet<>();
        biGramMap = new HashMap<String, HashSet<String>>();
        parser = Parser.getInstance();

        readBiGramAndFeature("Digital Camera Features.txt");
    }


    public void readBiGramAndFeature(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("feature sets" + File.separator + filename)));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length == 2) {

                    if (!biGramMap.containsKey(tokens[0]))
                        biGramMap.put(tokens[0], new HashSet<String>());
                    HashSet<String> set = biGramMap.get(tokens[0]);
                    set.add(tokens[1]);
                    biGramMap.put(tokens[0], set);

                } else {
                    features.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


    public List<Feature> extractBiGramAndFeature(String productId, Sentence sentence) {
        List<Feature> result = new ArrayList<>();

        //TODO improvement : one loop ?

       // System.out.println("\nSENTENCE: " + sentence);

        String[] tokens = sentence.getTokens(); // get the sentence tokens (words)
//        String pos[] = sentence.getPos(); // get the POS tag for each sentence token
//        String chunks[] = sentence.getChunks(); // get the chunk tags for the sentence

        boolean[] biGramfound = new boolean[tokens.length];

        // Check for the occurrence of bigram features in each sentence
        for (int i = 0; i < tokens.length; i++) {
            // The code below is not complete - e.g.should check that POS tags corresponding to the bigram terms follow the patterns
            // noun-noun or adjective-noun, and all tokens should be converted to lowercase ...
            //
            // Also need to ensure a given token is not counted twice as a feature - e.g. once as a single-noun feature and again
            // as a bigram feature. One way to do this is to create a boolean array (size equal to the number of tokens in the sentence)
            // and set elements to true if the corresponding terms have been already identified as features. Check for occurrence of bigram
            // features first, and then check for occurrence of single-noun features.

            if (i < tokens.length - 1 // need -1 because searching for bigram features...
                    && biGramMap.containsKey(tokens[i].toLowerCase())
                    && biGramMap.get(tokens[i].toLowerCase()).contains(tokens[i + 1].toLowerCase())) {

                biGramfound[i] = true;
                biGramfound[i + 1] = true;

                //System.out.println("BIGRAM: " + tokens[i] + " " + tokens[i + 1]);
                Feature feature = new Feature();
                feature.setName(tokens[i].toLowerCase() + " " + tokens[i + 1].toLowerCase());
                feature.setFeaturePosition(i);
                feature.setSentence(sentence);
                feature.setBiGramOffset(1);
                feature.setProductId(productId);

                result.add(feature);
            }
        }

        // Check for the occurrence of features in each sentence
        for (int i = 0; i < tokens.length; i++) {

            if (! biGramfound[i] && features.contains(tokens[i].toLowerCase())) {

                //System.out.println("FEATURE: " + tokens[i]);
                Feature feature = new Feature();
                feature.setName(tokens[i].toLowerCase());
                feature.setFeaturePosition(i);
                feature.setSentence(sentence);
                feature.setBiGramOffset(0);
                feature.setProductId(productId);

                result.add(feature);
            }
        }


        return result;
    }


}








