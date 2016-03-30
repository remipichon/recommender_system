package runner;

import model.Feature;
import model.FeatureSummary;
import model.Wrapper;
import service.*;
import util.nlp.Parser;
import util.reader.DatasetReader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OneReview {

    public static void main(String[] args) {

        ReviewService reviewService = ReviewService.getInstance();
        FeatureService featureService = FeatureService.getInstance();
        SentimentService sentimentService = SentimentService.getInstance();
        OpinionService opinionService = OpinionService.getInstance();
        OutputService outputService = OutputService.getInstance();

        String filename = "Digital Camera one review.txt"; // set the dataset filename
        String distFolder  = "printer_one_review_test_cases";


        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class

        List<Feature> features = reviewService.extractFeatures(reader.getReviews()).features;

        opinionService.validPattern(features);

        opinionService.negationTerm(features);

        Map<String, List<FeatureSummary>> computeOutputPerProduct = outputService.computeOutputPerProduct(features);

        outputService.generativeCSVFiles(distFolder,computeOutputPerProduct);


        Parser parser = Parser.getInstance();


        String sentence = reader.getReviews().get(0).getReviewText();

        String[] tokens = parser.getSentenceTokens(sentence); //same sentence for each features as one review only
        String pos[] = parser.getPOSTags(tokens);

        System.out.println(sentence);
        System.out.println(Arrays.toString(tokens));
        System.out.println(Arrays.toString(pos));
        System.out.println("POS pattern");
        for (Feature feature : features) {
            System.out.println(feature.getPosPattern());
        }



    }
}
