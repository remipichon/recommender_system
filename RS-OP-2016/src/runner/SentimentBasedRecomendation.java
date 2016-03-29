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

    public static void main(String[] args){
        OutputService outputService = OutputService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();
        RecommendationService recommendationService = RecommendationService.getInstance();

        recommendationService.setW(0.5);
        int topN = 10;


        String filename = "Digital Camera.txt";

        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.restoreMapOutputsFromFile(filename);

        List<Product> products = new ArrayList<>();

        for (Map.Entry<String, List<FeatureSummary>> entry : computeOutputPerProduct.entrySet()) {
            String productId = entry.getKey();
            List<FeatureSummary> featureSummaries = entry.getValue();
            products.add(new Product(productId,featureSummaries));
        }


        recommendationService.setSentimentAndPopularity(products);

        recommendationService.setRecommendations(products, topN);

        System.out.println("recommendations for  "+products.get(0));
        System.out.println("--------");
        for (Product product : products.get(0).getRecommendations()) {
            System.out.println(product);
            System.out.println("--");
        }

    }
}
