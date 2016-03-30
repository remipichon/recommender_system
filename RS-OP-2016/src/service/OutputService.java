package service;

import model.Feature;
import model.FeatureSummary;
import model.Product;
import model.Review;

import java.io.*;
import java.util.*;

public class OutputService {
    public static final String REVIEW_COUNT_PER_PRODUCT = "_review_count_per_product";
    public static final String PRODUCT_SENTIMENT_RECOMMENDATION = "_product_sentiment_recommendation";
    public static final String MEAN_RATING_PER_PRODUCT = "_mean_rating_per_product";
    public static final String FEATURE_SUMMARIES_PER_PRODUCT = "_feature_summaries_per_product";
    private static OutputService instance;

    public static OutputService getInstance() {
        if (instance == null)
            instance = new OutputService();

        return instance;
    }


    private OutputService() {

    }


    public Map<String, List<FeatureSummary>> computeOutputPerProduct(List<Feature> features) {
        Map<String, HashMap<String, FeatureSummary>> tempResult = new HashMap<>(); //<productId, <featureName, FeatureSummary>> for a quicker access, use hashmap !

        for (Feature feature : features) {

            //create storage for product
            if (!tempResult.containsKey(feature.getProductId()))
                tempResult.put(feature.getProductId(), new HashMap<>());

            HashMap<String, FeatureSummary> featureSummaries = tempResult.get(feature.getProductId());
            //create storage for feature
            if (!featureSummaries.containsKey(feature.getName()))
                featureSummaries.put(feature.getName(), new FeatureSummary(feature.getName()));

            FeatureSummary featureSummary = featureSummaries.get(feature.getName());
            //update feature count
            switch (feature.getSentiment()) {
                case POSITIVE:
                    featureSummary.positiveCount++;
                    break;
                case NEGATIVE:
                    featureSummary.negativeCount++;
                    break;
                case NEUTRAL:
                    featureSummary.neutralCount++;
                    break;
            }

        }


        Map<String, List<FeatureSummary>> result = new HashMap<>(); //<productId, Output>

        for (Map.Entry<String, HashMap<String, FeatureSummary>> stringHashMapEntry : tempResult.entrySet()) {
            String productId = stringHashMapEntry.getKey();
            HashMap<String, FeatureSummary> featureSummaries = stringHashMapEntry.getValue();

            result.put(productId, new ArrayList<>(featureSummaries.values()));
        }

        System.out.println("Compute output per product done");

        return result;

    }

    public void generativeCSVFiles(String folderName, String prefixFileName, Map<String, List<FeatureSummary>> computeOutputPerProduct) {

        for (Map.Entry<String, List<FeatureSummary>> product : computeOutputPerProduct.entrySet()) {
            String productId = product.getKey();
            List<FeatureSummary> featureSummaries = product.getValue();
            generateCSVFile(folderName, prefixFileName, productId, featureSummaries);
        }

        System.out.println("All CSV files generated");
    }

    private void generateCSVFile(String folderName, String category, String productId, List<FeatureSummary> featureSummaries) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(folderName + File.separator + category + "_" + productId + ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.append("feature,pos,neg,neutral\n");

            for (FeatureSummary featureSummary : featureSummaries) {
                writer.append(featureSummary.getFeatureName() + "," + featureSummary.positiveCount + "," + featureSummary.negativeCount + "," + featureSummary.neutralCount + "\n");
            }


            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Store Java object to save computing time while developing the different tasks
     */

    public void storeReviewCountPerProduct(Map<String, Integer> reviewCountPerProduct, String filename) {
        this.storeObject(reviewCountPerProduct, filename + REVIEW_COUNT_PER_PRODUCT);
    }


    public void storeMeanRatingPerProduct(Map<String, Double> meanRatingPerProduct, String filename) {
        this.storeObject(meanRatingPerProduct, filename + MEAN_RATING_PER_PRODUCT);
    }

    public void storeFeatureSummariesPerProduct(Map<String, List<FeatureSummary>> outputs, String fileName) {
        storeObject(outputs, fileName + FEATURE_SUMMARIES_PER_PRODUCT);
    }

    public Map<String, List<FeatureSummary>> restoreFeatureSummariesPerProductFromFile(String fileName) {
        String name = fileName + FEATURE_SUMMARIES_PER_PRODUCT;
        Map<String, List<FeatureSummary>> outputs = null;
        ObjectInputStream ois = getObjectInputStream(name);

        try {
            outputs = (Map<String, List<FeatureSummary>>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("successfully restore " + name + "  from file");

        return outputs;
    }


    public Map<String, Integer> restoreReviewCountPerProductFromFile(String fileName) {
        String name = fileName + REVIEW_COUNT_PER_PRODUCT;
        Map<String, Integer> outputs = null;
        ObjectInputStream ois = getObjectInputStream(name);

        try {
            outputs = (Map<String, Integer>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("successfully restore " + name + "  from file");

        return outputs;
    }

    public Map<String, Double> restoreMeanRatingPerProductFromFile(String fileName) {
        fileName = fileName + MEAN_RATING_PER_PRODUCT;
        Map<String, Double> outputs = null;
        ObjectInputStream ois = getObjectInputStream(fileName);

        try {
            outputs = (Map<String, Double>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("successfully restore " + fileName + "  from file");

        return outputs;
    }


    public void storeObject(Object outputs, String fileName) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("intermediate_result" + File.separator + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(outputs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("successfully store " + fileName);
    }

    private ObjectInputStream getObjectInputStream(String fileName) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("intermediate_result" + File.separator + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ois;
    }


    public void computeFeatureCountForPopularity(Map<String, List<FeatureSummary>> computeOutputPerProduct, Map<Review, List<Feature>> featuresPerReview) {
        Set<String> featureAlreadyCountForAReview;
        for (List<Feature> featureList : featuresPerReview.values()) { //each review
            featureAlreadyCountForAReview = new HashSet<>(); //reset the flag

            for (Feature feature : featureList) { //each feature for this review
                if(featureAlreadyCountForAReview.contains(feature.getName())) { //if feature already count for this review, we skip it
                    continue;
                }

                //else, we need to find the corresponding feature summary and update the atLeastOncePerReviewCount
                List<FeatureSummary> featureSummaries = computeOutputPerProduct.get(feature.getProductId()); //get the feature for this product
                for (FeatureSummary featureSummary : featureSummaries) {
                    if(featureSummary.getFeatureName().equals(feature.getName())){ //we find the right feature summary
                        featureSummary.atLeastOncePerReviewCount++;
                        break;
                    }

                }

                featureAlreadyCountForAReview.add(feature.getName());
            }
        }

    }
}
