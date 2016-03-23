package runner;

import model.Feature;
import model.Review;
import service.FeatureService;
import service.OpinionService;
import service.ReviewService;
import service.SentimentService;

import java.util.List;

public class OneFullReviewWithNegationTerm {

    public static void main(String[] args) {

        ReviewService reviewService = ReviewService.getInstance();
        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();
        OpinionService opinionService = OpinionService.getInstance();



        Review review = new Review();
        review.setReviewText("It doesn't provides amazing pictures quality and is perfect for amateur photographers. " +
                "The battery life is truly not only outstanding, I am not even going to buy a spare battery. " +
                "It's weight is heavier than the D60, but that's not a problem. " +
                "The video recording and autofocus are limited, however. " +
                "The viewfinder is also not really poor.");


        List<Feature> features = reviewService.extractFeatures(review);

        opinionService.negationTerm(features);


        System.out.println("EXPECTED");
        System.out.println("picture quality is NEGATIVE");
        System.out.println("battery life is NEGATIVE");
        System.out.println("spate battery is NEGATIVE");
        System.out.println("video recording is NEGATIVE");
        System.out.println("autofocus is NEGATIVE");
        System.out.println("viewfinder is POSITIVE");


        for (Feature feature : features) {
            System.out.println(feature);
        }


    }
}
