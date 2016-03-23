package runner;

import model.Feature;
import model.FeatureSummary;
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


        List<Feature> features = reviewService.extractFeatures(reader.getReviews());


        System.out.println("Features extracted "+features.size());

        //TODO set everything to neutral, to test with huuuge data
        opinionService.validPattern(features);

        opinionService.negationTerm(features);


        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.computeOutputPerProduct(features);

        outputService.generativeCSVFiles(computeOutputPerProduct);


        System.out.println(String.format("%-20s", "Feature") + "|" + String.format("%-8s", "Positive") + "|" + String.format("%-8s", "Negative") + "|" + String.format("%-4s", "Neutral"));
        for (FeatureSummary featureSummary : computeOutputPerProduct.get("ONE")) {
            System.out.println(featureSummary.toString());
        }



    }
}
