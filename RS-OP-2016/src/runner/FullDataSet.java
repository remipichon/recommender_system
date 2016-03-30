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
        //        String filename = "Digital Camera one review.txt"; // set the dataset filename
//        String distFolder  = "printer_one_review_test_cases";

//        String filename = "Digital Camera small real.txt"; // set the dataset filename
//        String distFolder  = "printer_test_cases";

        String filename = "Printer.txt";
        String distFolder  = "printer_cases";
        String featureSetFilename = "Printer Features.txt";
        String prefixFileName = "printer";
//
//        String filename = "Digital Camera.txt";
//        String distFolder  = "digital_camera_cases";
//        String featureSetFilename = "Digital Camera Features.txt";
//        String prefixFileName = "camera";



        RunnerService runnerService = RunnerService.getInstance();
        runnerService.readDatasetAndGenerateOutputs(distFolder,filename,featureSetFilename, prefixFileName);


    }

}
