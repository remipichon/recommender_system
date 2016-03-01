/**
 * Used to evaluate the item-based CF algorithm 
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package alg.ib;

import alg.ib.neighbourhood.NearestNeighbourhood;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.Predictor;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

import java.io.File;

public class ExecuteIB_Exp_3 {
    public static void main(String[] args) {
        // configure the item-based CF algorithm - set the predictor, neighbourhood and similarity metric ...
        ExecuteParams executeParams1 = new ExecuteParams();
        ExecuteParams executeParams2 = new ExecuteParams();
        ExecuteParams executeParams30 = new ExecuteParams();
        ExecuteParams executeParams31 = new ExecuteParams();
        ExecuteParams executeParams32 = new ExecuteParams();
        ExecuteParams executeParams33 = new ExecuteParams();
        ExecuteParams executeParams34 = new ExecuteParams();
        ExecuteParams executeParams41 = new ExecuteParams();
        ExecuteParams executeParams42 = new ExecuteParams();
        ExecuteParams executeParams43 = new ExecuteParams();

        //*** PARAMS
        executeParams1.DeviationPredictor_CosineMetric();
        executeParams2.DeviationPredictor_PearsonMetric();
        executeParams30.DeviationPredictor_PearsonSignifianceWeightMetric(1);
        executeParams31.DeviationPredictor_PearsonSignifianceWeightMetric(5);
        executeParams32.DeviationPredictor_PearsonSignifianceWeightMetric(50);
        executeParams33.DeviationPredictor_PearsonSignifianceWeightMetric(80);
        executeParams34.DeviationPredictor_PearsonSignifianceWeightMetric(100);
        executeParams41.DeviationPredictor_JaccardMetric(50);
        executeParams42.DeviationPredictor_JaccardMetric(70);
        executeParams43.DeviationPredictor_JaccardMetric(100);
        //***

       computeOne(executeParams1);
        computeOne(executeParams2);
        computeOne(executeParams30,"N = 1");
        computeOne(executeParams31,"N = 5");
        computeOne(executeParams32,"N = 50");
//        computeOne(executeParams33,"N = 80");
        computeOne(executeParams34,"N = 100");
        computeOne(executeParams41, "T = 50");
        computeOne(executeParams42, "T = 70");
        computeOne(executeParams43, "T = 100");
    }

    /**
     * Just a wrapper for computeOne(ExecuteParams executeParams, String extraComment)
     *
     * @param executeParams
     */
    private static void computeOne(ExecuteParams executeParams) {
        computeOne(executeParams, "");
    }

    /**
     * @param executeParams
     * @param extraComment  to be displayed in the ouput
     */
    private static void computeOne(ExecuteParams executeParams, String extraComment) {
        Predictor predictor = executeParams.predictor;
        Neighbourhood neighbourhood = new NearestNeighbourhood(70);
        SimilarityMetric metric = executeParams.metric;

        // set the paths and filenames of the item file, train file and test file ...
        String itemFile = "ML dataset" + File.separator + "u.item";
        String trainFile = "ML dataset" + File.separator + "u.train";
        String testFile = "ML dataset" + File.separator + "u.test";

        // set the path and filename of the output file ...
        String outputFile = "results" + File.separator + "predictions.txt";

        ////////////////////////////////////////////////
        // Evaluates the CF algorithm (do not change!!):
        // - the RMSE and coverage are output to screen
        // - output file is created
        DatasetReader reader = new DatasetReader(itemFile, trainFile, testFile);
        ItemBasedCF ibcf = new ItemBasedCF(predictor, neighbourhood, metric, reader);

        Evaluator eval = new Evaluator(ibcf, reader.getTestData());
        eval.writeResults(outputFile);

        String params = executeParams.metric.getName();
        System.out.println(params + ((extraComment.equals("")) ? "" : " | " + extraComment));

        Double RMSE = eval.getRMSE();
        if (RMSE != null) System.out.println("RMSE: \t" + RMSE);

        for (int i = 1; i <= 5; i++) {
            RMSE = eval.getRMSE(i);
            if (RMSE != null) System.out.printf("RMSE (true rating = %d): %.6f\n", i, RMSE);
        }

        double coverage = eval.getCoverage();
        System.out.println("coverage: \t" + coverage);
    }
}
