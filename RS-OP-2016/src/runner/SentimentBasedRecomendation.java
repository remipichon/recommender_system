package runner;

import model.FeatureSummary;
import model.Product;
import service.OutputService;
import service.RecommendationService;
import service.SentimentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SentimentBasedRecomendation {

    public static void main(String[] args) {
        OutputService outputService = OutputService.getInstance();
        RecommendationService recommendationService = RecommendationService.getInstance();

        recommendationService.setW(0.5);
        int topN = 10;

        String filename = "Digital Camera.txt";

        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.restoreMapOutputsFromFile(filename);
        Map<String, Integer> reviewCountPerProduct = outputService.restoreReviewCountPerProductFromFile(filename);


        List<Product> products = new ArrayList<>();

        for (Map.Entry<String, List<FeatureSummary>> entry : computeOutputPerProduct.entrySet()) {
            String productId = entry.getKey();
            List<FeatureSummary> featureSummaries = entry.getValue();
            products.add(new Product(productId, featureSummaries, reviewCountPerProduct.get(productId),0.0));
        }


        recommendationService.setSentimentAndPopularity(products);

        recommendationService.setRecommendations(products, topN);

        outputService.storeObject(products, filename + "_product_sentiment_summary");


        System.out.println("recommendations for  " + products.get(0).getId() + " : "+products.get(0).getRecommendations());
        System.out.println("recommendations for  " + products.get(1).getId() + " : "+products.get(1).getRecommendations());
        System.out.println("recommendations for  " + products.get(2).getId() + " : "+products.get(2).getRecommendations());
        System.out.println("recommendations for  " + products.get(3).getId() + " : "+products.get(3).getRecommendations());
        System.out.println("recommendations for  " + products.get(4).getId() + " : "+products.get(4).getRecommendations());
        System.out.println("recommendations for  " + products.get(5).getId() + " : "+products.get(5).getRecommendations());
//        System.out.println("--------");
//        for (Product product : products.get(0).getRecommendations()) {
//            System.out.println(product.getId());
//            System.out.println("--");
//        }

    }
}
