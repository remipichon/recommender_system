package runner;

import model.Review;
import service.OutputService;
import util.reader.DatasetReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReviewCountAndRatingPerProduct {

    public static void main(String[] args) {
        String filename = "Digital Camera.txt";

        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class

        Map<String, Integer> reviewCountPerProduct = new HashMap<>();
        Map<String, Double> totalRatingPerProduct = new HashMap<>();
        for (Review review : reader.getReviews()) {
            //reviewCountPerProduct
            if (!reviewCountPerProduct.containsKey(review.getProductId()))
                reviewCountPerProduct.put(review.getProductId(), 0);
            reviewCountPerProduct.put(review.getProductId(), reviewCountPerProduct.get(review.getProductId()) + 1);

            //meanRatingPerProduct
            if (!totalRatingPerProduct.containsKey(review.getProductId()))
                totalRatingPerProduct.put(review.getProductId(), 0.0);
            totalRatingPerProduct.put(review.getProductId(), totalRatingPerProduct.get(review.getProductId()) + review.getReviewRating());
        }

        Map<String, Double> meanRatingPerProduct = new HashMap<>();
        for (Map.Entry<String, Double> stringDoubleEntry : totalRatingPerProduct.entrySet()) {
            String productId = stringDoubleEntry.getKey();
            Double totalRating = stringDoubleEntry.getValue();
            meanRatingPerProduct.put(productId,totalRating / reviewCountPerProduct.get(productId));
        }

        OutputService outputService = OutputService.getInstance();

        outputService.storeObject(reviewCountPerProduct, filename + "_review_count_per_product");
        outputService.storeObject(meanRatingPerProduct, filename + "_mean_rating_per_product");

        reviewCountPerProduct = outputService.restoreReviewCountPerProductFromFile(filename);
        meanRatingPerProduct = outputService.restoreMeanRatingPerProductFromFile(filename);

        System.out.println("reviewCountPerProduct");
        System.out.println(Arrays.asList(reviewCountPerProduct));
        System.out.println("meanRatingPerProduct");
        System.out.println(Arrays.asList(meanRatingPerProduct));


    }
}
