package service;

import model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class RecommendationService {

    private static RecommendationService instance;

    public static RecommendationService getInstance() {
        if (instance == null)
            instance = new RecommendationService();

        return instance;
    }


    private RecommendationService() {

    }


    private Double w;

    public void setW(Double w) {
        this.w = w;
    }

    private Double getScore(Product query, Product candidate){
        //TODO using w, sim, sent(p,p)
        return 0.0;
    }

    private Double getSimilarity(Product query, Product candidate){
        //TODO cosine with pop
        return 0.0;
    }

    private Double getSentiment(Product product){
        //TODO
        return 0.0;
    }

    private Double getSentiment(Product query, Product candidate){
        //TODO sum getSenitment(p) for both
        return 0.0;
    }

    private Double getPopularity(Product product){
        //TODO
        return 0.0;
    }

    public void setSentimentAndPopularity(List<Product> products) {
        for (Product product : products) {
            product.setPopularity(this.getPopularity(product));
            product.setSentiment(this.getSentiment(product));
        }
    }

    public void setRecommendations(List<Product> products, int topN) {
        for (Product query : products) {
            SortedSet<Product> recommendations;

            for (Product candidate : products) {
                if(candidate.equals(query))continue;

                //tODO compute score and store it in the sorted list

            }

            //TODO store topN first product recommendation in product reco list (cf previous assignment)
        }
    }
}
