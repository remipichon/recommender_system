package examples;

import model.Feature;
import model.Review;
import service.FeatureService;
import service.ReviewService;
import service.SentimentService;

import java.util.List;

public class TestSentimentForOneFullReview {

    public static void main(String[] args) {

        ReviewService reviewService = ReviewService.getInstance();
        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();


        Review review = new Review();
        review.setReviewText("It provides amazing pictures quality and is perfect for amateur photographers. " +
                "The battery life is truly outstanding, I am not even going to buy a spare battery. " +
                "It's weight is heavier than the D60, but that's not a problem. " +
                "The video recording and autofocus are limited, however. " +
                "The viewfinder is also really poor.");


        List<Feature> features = reviewService.extractFeatures(review);


        for (Feature feature : features) {
            System.out.println(feature);
        }


    }
}
