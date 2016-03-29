package service;

import model.Feature;
import model.FeatureSummary;
import model.Product;
import util.reader.DatasetReader;

import java.io.*;
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

    public void generativeCSVFiles(String folderName, Map<String, List<FeatureSummary>> computeOutputPerProduct) {

        for (Map.Entry<String, List<FeatureSummary>> product : computeOutputPerProduct.entrySet()) {
            String productId = product.getKey();
            List<FeatureSummary> featureSummaries = product.getValue();
            generateCSVFile(folderName,"camera",productId,featureSummaries);
        }

        System.out.println("All CSV files generated");
    }

    private void generateCSVFile(String folderName, String category, String productId, List<FeatureSummary> featureSummaries) {
        FileWriter writer = null;
        try {
            writer = new FileWriter( folderName + File.separator + category + "_" + productId+".csv");
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

    public Map<String, List<FeatureSummary>> restoreMapOutputsFromFile(String fileName) {
        Map<String, List<FeatureSummary>> outputs = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("dataset" + File.separator + fileName + "_outputs.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputs = (Map<String, List<FeatureSummary>>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("successfully restore " + fileName +" ouputs features datam from file");

        return outputs;
    }

    public Map<String, Integer> restoreReviewCountPerProductFromFile(String fileName) {
        String name = fileName + "_review_count_per_product";
        Map<String, Integer> outputs = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("dataset" + File.separator + name + "_outputs.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputs = (Map<String, Integer>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("successfully restore " + fileName +" ouputs features datam from file");

        return outputs;
    }

    public Map<String, Double> restoreMeanRatingPerProductFromFile(String fileName) {
        fileName = fileName + "_mean_rating_per_product";
        Map<String, Double> outputs = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("dataset" + File.separator + fileName + "_outputs.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputs = (Map<String, Double>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("successfully restore " + fileName +" ouputs features datam from file");

        return outputs;
    }

    public List<Product> restoreProductFromFile(String fileName) {
        fileName = fileName + "_product_sentiment_summary";
        List<Product> outputs = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("dataset" + File.separator + fileName + "_outputs.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputs = (List<Product>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("successfully restore " + fileName +" ouputs features datam from file");

        return outputs;
    }

    public void storeMapOutputsFromFile(Map<String, List<FeatureSummary>> outputs, String fileName) {
        storeObject(outputs, fileName);
    }


    public void storeObject(Object outputs, String fileName) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("dataset" + File.separator + fileName + "_outputs.txt");
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
    }
}
