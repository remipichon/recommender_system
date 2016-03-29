package model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Product implements Serializable{
    private final Double meanRating;
    private String id;
    private List<FeatureSummary> featureSummaries;
    private Map<String,Double> featurePolarities;
    private Map<String,Double> featureSentiments;
    private List<Product> recommendations;
    private Integer reviewCount;
    private Double ratingLift;
    private Double querySimilarity;

    public Product(String id, List<FeatureSummary> featureSummaries, Integer reviewCount, Double meanRating) {
        this.id = id;
        this.featureSummaries = featureSummaries;
        this.reviewCount = reviewCount;
        this.meanRating = meanRating;
    }

    public Double getRatingLift() {
        return ratingLift;
    }

    public void setRatingLift(Double ratingLift) {
        this.ratingLift = ratingLift;
    }

    public String getId() {
        return id;
    }

    public List<FeatureSummary> getFeatureSummaries() {
        return featureSummaries;
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

    public Map<String, Double> getFeaturePopularities() {
        return featurePolarities;
    }

    public void setFeaturePolarities(Map<String, Double> featurePolarities) {
        this.featurePolarities = featurePolarities;
    }

    public Double getMeanRating() {
        return meanRating;
    }

    public Double getQuerySimilarity() {
        return querySimilarity;
    }

    public void setQuerySimilarity(Double querySimilarity) {
        this.querySimilarity = querySimilarity;
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
