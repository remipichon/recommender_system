package runner;

import model.Review;
import service.RunnerService;
import util.reader.DatasetReader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class StatisticDataSet {

    public static void main(String[] args) {
        //        String filename = "Digital Camera one review.txt"; // set the dataset filename
//        String distFolder  = "printer_one_review_test_cases";

//        String filename = "Digital Camera small real.txt"; // set the dataset filename
//        String distFolder  = "printer_test_cases";

        String filename;
        String distFolder;
        String featureSetFilename;
        String prefixFileName;
        if (false) {
            filename = "Printer.txt";
            distFolder = "printer_cases";
            featureSetFilename = "Printer Features.txt";
            prefixFileName = "printer";
        } else {
            filename = "Digital Camera.txt";
            distFolder = "digital_camera_cases";
            featureSetFilename = "Digital Camera Features.txt";
            prefixFileName = "camera";
        }


        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat)nf;


        DatasetReader reader = new DatasetReader(filename); // create an instance of the DatasetReader class


        HashMap<String,Integer> reviewCountPerProduct = new HashMap<>();
        HashMap<String,Double> totalHelpfulnessPerProduct = new HashMap<>();
        HashMap<String,Double> totalReviewLengthPerProduct = new HashMap<>();


        ArrayList<Review> reviews = reader.getReviews();
        for (Review review : reviews) {
            String productId = review.getProductId();
            double helpfulness = review.getHelpfulness();
            int reviewLength = review.getReviewText().length();

            if(!reviewCountPerProduct.containsKey(productId))
                reviewCountPerProduct.put(productId,0);
            reviewCountPerProduct.put(productId,reviewCountPerProduct.get(productId) + 1);

            if(!totalHelpfulnessPerProduct.containsKey(productId))
                totalHelpfulnessPerProduct.put(productId,0.0);
            totalHelpfulnessPerProduct.put(productId,totalHelpfulnessPerProduct.get(productId) + helpfulness);

            if(!totalReviewLengthPerProduct.containsKey(productId))
                totalReviewLengthPerProduct.put(productId,0.0);
            totalReviewLengthPerProduct.put(productId,totalReviewLengthPerProduct.get(productId) + reviewLength);
        }

        HashMap<String,Double> averageHelpfulnessPerProduct = new HashMap<>();
        HashMap<String,Double> averageReviewLengthPerProduct = new HashMap<>();

        for (Map.Entry<String, Double> entry : totalReviewLengthPerProduct.entrySet()) {
            String productId = entry.getKey();
            Double total = entry.getValue();

            averageReviewLengthPerProduct.put(productId,total/reviewCountPerProduct.get(productId));
        }

        for (Map.Entry<String, Double> entry : totalHelpfulnessPerProduct.entrySet()) {
            String productId = entry.getKey();
            Double total = entry.getValue();

            averageHelpfulnessPerProduct.put(productId,total/reviewCountPerProduct.get(productId));
        }



        System.out.println("stat per product");
        System.out.println("productId\treview count\tavg helpfulness\tavg review length");
        for (Map.Entry<String, Integer> entry : reviewCountPerProduct.entrySet()) {
            String productId = entry.getKey();
            Integer count = entry.getValue();
            Double avgHelpfulness = averageHelpfulnessPerProduct.get(productId);
            Double avgReviewLength = averageReviewLengthPerProduct.get(productId);

            System.out.println(productId+"\t"+count+"\t"+df.format(avgHelpfulness)+"\t"+df.format(avgReviewLength));

        }

        //review count per product
        //average helpfulness per product
        //average of average helpfulness per product



    }

}
