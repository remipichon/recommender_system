package examples;

import model.Feature;
import model.FeatureSummary;
import service.*;
import util.reader.DatasetReader;

import java.util.List;
import java.util.Map;

public class TestExtractSentimentForARealDataSetOfReview {

    public static void main(String[] args) {

        ReviewService reviewService = ReviewService.getInstance();
        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();
        OpinionService opinionService = OpinionService.getInstance();
        OutputService outputService = OutputService.getInstance();

        //String filename = "Digital Camera small real.txt"; // set the dataset filename
        String filename = "Digital Camera.txt"; // set the dataset filename
        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class


        List<Feature> features = reviewService.extractFeatures(reader.getReviews());


        //TODO set everything to neutral
        opinionService.validPattern(features);

        opinionService.negationTerm(features);

        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.computeOutputPerProduct(features);

        outputService.generativeCSVFiles(computeOutputPerProduct);

    }
}
