package model;

import java.io.Serializable;

public class FeatureSummary implements Serializable{
    private String featureName;
    public Integer positiveCount;
    public Integer negativeCount;
    public Integer neutralCount;
    public Integer atLeastOncePerReviewCount;

    public FeatureSummary(String name) {
        featureName = name;
        positiveCount = 0;
        negativeCount = 0;
        neutralCount = 0;
        atLeastOncePerReviewCount = 0;
    }

    public String getFeatureName() {
        return featureName;
    }

    @Override
    public String toString() {
        return String.format("%-20s", featureName) + "|" + String.format("%-8s", positiveCount) + "|" + String.format("%-8s", negativeCount) + "|" + String.format("%-8s", neutralCount + "| => "+atLeastOncePerReviewCount);
    }
}
