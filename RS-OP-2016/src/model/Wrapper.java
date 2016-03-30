package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wrapper {
    public List<Feature> features;
    public Map<Review,List<Feature>> featuresPerReview;

    public Wrapper(List<Feature> features, Map<Review, List<Feature>> featuresPerReview) {
        this.features = features;
        this.featuresPerReview = featuresPerReview;
    }
}
