package runner;

import model.Feature;
import model.FeatureSummary;
import model.Review;
import model.Wrapper;
import service.*;
import util.reader.DatasetReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullDataSet {

    public static void main(String[] args) {

        ReviewService reviewService = ReviewService.getInstance();
        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();
        OpinionService opinionService = OpinionService.getInstance();
        OutputService outputService = OutputService.getInstance();

//        String filename = "Digital Camera one review.txt"; // set the dataset filename
//        String distFolder  = "printer_one_review_test_cases";

//        String filename = "Digital Camera small real.txt"; // set the dataset filename
//        String distFolder  = "printer_test_cases";

        String filename = "Digital Camera.txt";
        String distFolder  = "digital_camera_cases";

        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class


        Wrapper wrapper = reviewService.extractFeatures(reader.getReviews());
        List<Feature> features = wrapper.features;

        opinionService.validPattern(features);

        opinionService.negationTerm(features);

        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.computeOutputPerProduct(features);
        outputService.computeFeatureCountForPopularity(computeOutputPerProduct,wrapper.featuresPerReview);

        outputService.generativeCSVFiles(distFolder,computeOutputPerProduct);

        outputService.storeFeatureSummariesPerProduct(computeOutputPerProduct, filename);

        //review count per product
        Map<String, Integer> reviewCountPerProduct = new HashMap<>();
        for (Review review : reader.getReviews()) {
            if (!reviewCountPerProduct.containsKey(review.getProductId()))
                reviewCountPerProduct.put(review.getProductId(), 0);
            reviewCountPerProduct.put(review.getProductId(), reviewCountPerProduct.get(review.getProductId()) + 1);
        }

        outputService.storeReviewCountPerProduct(reviewCountPerProduct, filename);

        reviewService.computeAndStorereviewCountAndMeanRatingPerProduct(reader.getReviews(),filename);


    }
}
