package runner;

import model.BetterType;
import model.FeatureSummary;
import model.Product;
import service.OutputService;
import service.RecommendationService;
import service.RunnerService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Experiment {

    public static void main(String[] args) {

//        String filename = "Digital Camera.txt";

        String filename = "Printer.txt";


        RunnerService runnerService = RunnerService.getInstance();

        RecommendationService recommendationService = RecommendationService.getInstance();

        recommendationService.setBetterType(BetterType.B1);
        System.out.println("Better B1");
        runnerService.experiment(filename);

        recommendationService.setBetterType(BetterType.B2);
        System.out.println("Better B2");
        runnerService.experiment(filename);

    }

}
