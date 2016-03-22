package service;

import model.Feature;
import model.FeatureSummary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputService {
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
            if(!tempResult.containsKey(feature.getProductId()))
                tempResult.put(feature.getProductId(),new HashMap<>());

            HashMap<String, FeatureSummary> featureSummaries = tempResult.get(feature.getProductId());
            //create storage for feature
            if(! featureSummaries.containsKey(feature.getName()))
                featureSummaries.put(feature.getName(),new FeatureSummary(feature.getName()));

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

            result.put(productId,new ArrayList<>(featureSummaries.values()));
        }

        System.out.println("Compute output per product done");

        return result;

    }

    public void generativeCSVFiles(Map<String, List<FeatureSummary>> computeOutputPerProduct) {

        for (Map.Entry<String, List<FeatureSummary>> product : computeOutputPerProduct.entrySet()) {
            String productId = product.getKey();
            List<FeatureSummary> featureSummaries = product.getValue();
            generateCSVFile("camera",productId,featureSummaries);
        }

        System.out.println("All CSV files generated");
    }

    private void generateCSVFile(String category, String productId, List<FeatureSummary> featureSummaries) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("printer_cases" + File.separator + category + "_" + productId+".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.append("feature,pos,neg,neutral\n");

            for (FeatureSummary featureSummary : featureSummaries) {
                writer.append(featureSummary.getFeatureName() + "," + featureSummary.positiveCount + "," + featureSummary.negativeCount + "," + featureSummary.neutralCount+"\n");
            }


            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
