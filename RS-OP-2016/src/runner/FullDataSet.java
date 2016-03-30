package runner;

import service.RunnerService;

public class FullDataSet {

    public static void main(String[] args) {
        //        String filename = "Digital Camera one review.txt"; // set the dataset filename
//        String distFolder  = "printer_one_review_test_cases";

//        String filename = "Digital Camera small real.txt"; // set the dataset filename
//        String distFolder  = "printer_test_cases";

        String filename;
        String distFolder;
        String featureSetFilename;
        String prefixFileName;
        if (true) {
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


        RunnerService runnerService = RunnerService.getInstance();
        runnerService.readDatasetAndGenerateOutputs(distFolder, filename, featureSetFilename, prefixFileName);


    }

}
