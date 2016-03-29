package service;

import model.FeatureSummary;
import model.Product;
import util.ScoredThingDsc;

import java.util.*;

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

    private Double getScore(Product query, Product candidate) {
        //using w, sim(Q,P), sent(p,p)
        return (1 - w) * this.getSimilarity(query, candidate) + w * (this.getSentiment(query, candidate) + 1) / 2;
    }

    private static double cosine(Product query, Product candidate) {
        // F(Q) u F(C)
        List<String> features = new ArrayList<>(query.getFeaturePopularities().keySet());
        features.addAll(candidate.getFeaturePopularities().keySet());


        Map<String, Double> queryPop = query.getFeaturePopularities();
        Map<String, Double> candidatePop = candidate.getFeaturePopularities();

        double numerator = 0;
        double denominator;
        double denominatorCandidate = 0;
        double denominatorTarget = 0;

        //compute numerator
        for (String feature : features) {
            if (!candidatePop.containsKey(feature)) continue; // it would be something * 0 = 0
            if (!queryPop.containsKey(feature)) continue; // it would be something * 0 = 0
            numerator += queryPop.get(feature) * candidatePop.get(feature);
        }

        //compute denominator
        for (Double aDouble : queryPop.values()) {
            denominatorCandidate += Math.pow(aDouble, 2);
        }
        denominatorCandidate = Math.sqrt(denominatorCandidate);
        for (Double aDouble : candidatePop.values()) {
            denominatorTarget += Math.pow(aDouble, 2);
        }
        denominatorTarget = Math.sqrt(denominatorTarget);
        denominator = denominatorCandidate * denominatorTarget;

        return numerator / denominator;
    }

    public Double getSimilarity(Product query, Product candidate) {
        // cosine apply to popularity
        return this.cosine(query, candidate);
    }

    private Double getSentiment(Product query, Product candidate) {
        //TODO sum getSenitment(p) for both

        //B1

        //better(fi,Q,C) = ( sent(fi,Q) - sent(fi,C) )/ 2
        //sent(Q,C) = avg(better(all fi)
        Double better = new Double(0);
        Integer betterCount = 0;

        for (Map.Entry<String, Double> stringDoubleEntry : query.getFeatureSentiments().entrySet()) {
            String featureName = stringDoubleEntry.getKey();
            Double querySentiment = stringDoubleEntry.getValue();

            if (!candidate.getFeatureSentiments().containsKey(featureName)) continue;

            Double candidateSentiment = candidate.getFeatureSentiments().get(featureName);

            better += (1.0 * (querySentiment - candidateSentiment) / 2);
            betterCount++;
        }
        return better / betterCount;
    }


    public void setSentimentAndPopularity(List<Product> products) {
        for (Product product : products) {
            product.setFeaturePolarities(this.computeFeaturePolarities(product));
            product.setFeatureSentiments(this.computeFeatureSentiments(product));
        }
    }

    private Map<String, Double> computeFeatureSentiments(Product product) {
        Map<String, Double> result = new HashMap<>(); //<FeatureName, sentiment>
        for (FeatureSummary featureSummary : product.getFeatureSummaries()) {
            Double sentiment;
            if (featureSummary.positiveCount + featureSummary.negativeCount == 0)
                sentiment = 0.0;
            else
                sentiment = 1.0 * (featureSummary.positiveCount - featureSummary.negativeCount) / (featureSummary.positiveCount + featureSummary.negativeCount);
            result.put(featureSummary.getFeatureName(), sentiment);
        }
        return result;
    }

    private Map<String, Double> computeFeaturePolarities(Product product) {
        Map<String, Double> result = new HashMap<>();
        for (FeatureSummary featureSummary : product.getFeatureSummaries()) {

            Integer sumSentiment = featureSummary.positiveCount + featureSummary.negativeCount + featureSummary.neutralCount;
            Integer numberReviews = product.getReviewCount();

            Double popularity = 1.0 * sumSentiment / numberReviews;
            result.put(featureSummary.getFeatureName(), popularity);
        }
        return result;
    }

    public void setRecommendations(List<Product> products, int topN) {
        for (Product query : products) {
            SortedSet<ScoredThingDsc> sortedRecommendations = new TreeSet<ScoredThingDsc>();


            for (Product candidate : products) {
                if (candidate.equals(query)) continue;

                //compute score and store it in the sorted list
                Double score = this.getScore(query, candidate);
                sortedRecommendations.add(new ScoredThingDsc(score, candidate)); // add the score for the current recommendation candidate case to the set
            }

            // sort the candidate recommendation cases by score (in descending order) and return as recommendations
            ArrayList<Product> recommendations = new ArrayList<>();

            for (Iterator<ScoredThingDsc> it = sortedRecommendations.iterator(); it.hasNext(); ) {
                ScoredThingDsc st = it.next();
                recommendations.add((Product) st.thing);
                if (recommendations.size() > 10) break;
            }
            query.setRecommendations(recommendations);
        }

    }
}
