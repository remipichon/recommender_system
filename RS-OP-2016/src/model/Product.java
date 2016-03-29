package model;

import java.util.List;

public class Product {
    private String id;
    private List<FeatureSummary> featureSummaries;
    private Double popularity;
    private Double sentiment;
    private List<Product> recommendations;

    public Product(String id, List<FeatureSummary> featureSummaries) {
        this.id = id;
        this.featureSummaries = featureSummaries;
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

    @Override
    public String toString() {
        String ret = id;

        for (FeatureSummary featureSummary : featureSummaries) {
            ret += "\n"+featureSummary;
        }

        return ret;
    }
}
