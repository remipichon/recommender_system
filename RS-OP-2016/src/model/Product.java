package model;

import java.util.List;
import java.util.Map;

public class Product {
    private String id;
    private List<FeatureSummary> featureSummaries;
    private Double popularity;
    private Double sentiment;
    private Map<String,Double> featurePolarities;
    private Map<String,Double> featureSentiments;
    private List<Product> recommendations;
    private Integer reviewCount;

    public Product(String id, List<FeatureSummary> featureSummaries, Integer reviewCount) {
        this.id = id;
        this.featureSummaries = featureSummaries;
        this.reviewCount = reviewCount;
    }

    public String getId() {
        return id;
    }

    public List<FeatureSummary> getFeatureSummaries() {
        return featureSummaries;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Double getSentiment() {
        return sentiment;
    }

    public void setSentiment(Double sentiment) {
        this.sentiment = sentiment;
    }

    public List<Product> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Product> recommendations) {
        this.recommendations = recommendations;
    }

    public Map<String, Double> getFeatureSentiments() {
        return featureSentiments;
    }

    public void setFeatureSentiments(Map<String, Double> featureSentiments) {
        this.featureSentiments = featureSentiments;
    }

    public Map<String, Double> getFeaturePolarities() {
        return featurePolarities;
    }

    public void setFeaturePolarities(Map<String, Double> featurePolarities) {
        this.featurePolarities = featurePolarities;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

//    @Override
//    public String toString() {
//        String ret = id;
//
//        for (FeatureSummary featureSummary : featureSummaries) {
//            ret += "\n"+featureSummary;
//        }
//
//        return ret;
//    }

    @Override
    public String toString() {
       return id;
    }
}
