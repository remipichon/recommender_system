package service;

import model.BetterType;
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
    private BetterType betterType;

    public void setBetterType(BetterType betterType) {
        this.betterType = betterType;
    }

    public void setW(Double w) {
        this.w = w;
    }

    private Double getScore(Product query, Product candidate) {
        //using w, sim(Q,P), sent(p,p)
        return (1 - w) * this.getSimilarity(query, candidate) + w * (this.getSentiment(query, candidate) + 1) / 2;
    }

    private static double cosine(Product query, Product candidate) {
        // F(Q) u F(C)
        Set<String> features = new HashSet<>(query.getFeaturePopularities().keySet());
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
        switch (betterType) {
            case B1:
                return getB1Sentiment(query, candidate);
            case B2:
                return getB2Sentiment(query, candidate);
        }
        return null;
    }

    private Double getB1Sentiment(Product query, Product candidate) {
        Double better = new Double(0);
        Integer betterCount = 0;

        for (Map.Entry<String, Double> stringDoubleEntry : query.getFeatureSentiments().entrySet()) {
            String featureName = stringDoubleEntry.getKey();
            Double querySentiment = stringDoubleEntry.getValue();

            if (!candidate.getFeatureSentiments().containsKey(featureName))
                continue; //here the  product's features intersection is made

            Double candidateSentiment = candidate.getFeatureSentiments().get(featureName);

            better += (1.0 * (querySentiment - candidateSentiment) / 2);
            betterCount++;  //here we get only the product features intersection size
        }
        return better / betterCount;
    }

    private Double getB2Sentiment(Product query, Product candidate) {
        Double better = new Double(0);

        Set<String> unionFeatures = new HashSet<>(query.getFeatureSentiments().keySet());
        unionFeatures.addAll(candidate.getFeatureSentiments().keySet());

        Double querySentiment;
        Double candidateSentiment;

        for (String feature : unionFeatures) {
            querySentiment = query.getFeatureSentiments().get(feature);
            candidateSentiment = candidate.getFeatureSentiments().get(feature);
            if (querySentiment == null) querySentiment = 0.0;
            if (candidateSentiment == null) candidateSentiment = 0.0;

            better += (1.0 * (querySentiment - candidateSentiment) / 2);

        }

        return better / unionFeatures.size();
    }


    public void setSentimentAndPopularity(List<Product> products) {
        for (Product product : products) {
            product.setFeaturePolarities(this.computeFeaturePopularities(product));
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

    private Map<String, Double> computeFeaturePopularities(Product product) {
        Map<String, Double> result = new HashMap<>();
        for (FeatureSummary featureSummary : product.getFeatureSummaries()) {

//            Integer sumSentiment = featureSummary.positiveCount + featureSummary.negativeCount + featureSummary.neutralCount;
            Integer sumSentiment = featureSummary.atLeastOncePerReviewCount;
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
                if (recommendations.size() > topN) break;
            }
            query.setRecommendations(recommendations);
        }

    }
}
