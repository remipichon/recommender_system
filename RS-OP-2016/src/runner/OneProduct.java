package runner;

import model.Feature;
import model.FeatureSummary;
import model.Wrapper;
import service.*;
import util.reader.DatasetReader;

import java.util.List;
import java.util.Map;

public class OneProduct {

    public static void main(String[] args) {

        ReviewService reviewService = ReviewService.getInstance();
        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();
        OpinionService opinionService = OpinionService.getInstance();
        OutputService outputService = OutputService.getInstance();

        String filename = "Digital Camera small.txt"; // set the dataset filename
        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class


        Wrapper wrapper = reviewService.extractFeatures(reader.getReviews());
        List<Feature> features = wrapper.features;




        System.out.println("Features extracted " + features.size());

        opinionService.validPattern(features);

        opinionService.negationTerm(features);


        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.computeOutputPerProduct(features);

        outputService.computeFeatureCountForPopularity(computeOutputPerProduct,wrapper.featuresPerReview);


        outputService.generativeCSVFiles("printer_one_product_test_cases",computeOutputPerProduct);


    }
}
