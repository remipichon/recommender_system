package runner;

import model.Review;
import service.OutputService;
import util.reader.DatasetReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NumberReviewsPerProduct {

    public static void main(String[] args) {
        String filename = "Digital Camera.txt";
        String distFolder = "printer_cases";

        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class

        Map<String, Integer> reviewCountPerProduct = new HashMap<>();
        for (Review review : reader.getReviews()) {
            if (!reviewCountPerProduct.containsKey(review.getProductId()))
                reviewCountPerProduct.put(review.getProductId(), 0);
            reviewCountPerProduct.put(review.getProductId(), reviewCountPerProduct.get(review.getProductId()) + 1);
        }

        OutputService outputService = OutputService.getInstance();

        outputService.storeObject(reviewCountPerProduct, filename + "_review_count_per_product");

        reviewCountPerProduct = outputService.restoreReviewCountPerProductFromFile(filename);

        System.out.println(Arrays.asList(reviewCountPerProduct));


    }
}
