/**
 * This class provides an example of how to run the experiments: in this example, RMSE and coverage performance
 * versus neighbourhood size is investigated using the nearest neighbourhood, simple average predictor, and 
 * Pearson correlation similarity approaches.
 *
 * Michael O'Mahony
 * 20/02/2016
 */

package alg.ib;

import alg.ib.neighbourhood.NearestNeighbourhood;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.neighbourhood.SimilarityThresholdingNeighbourhood;
import alg.ib.predictor.Predictor;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

import java.io.File;

public class ExecuteIB_Iterate_Exp_2 {
    public static void main(String[] args) {
        ExecuteParams executeParams = new ExecuteParams();

        //*** PARAMS
        executeParams.DeviationPredictor_JaccardMetric();
        //***
        String params = " | "+executeParams.predictor.getName()+ " | "+executeParams.metric.getName();
        System.out.println("  SimilarityThresholdingNeighbourhood\trmse"+params+"\t  coverage"+params+"\t  neighbourhood size");


        for (double threshold = 0; threshold <= 1.05; threshold += 0.05) {
            // configure the item-based CF algorithm - set the predictor, neighbourhood and similarity metric ...
            Predictor predictor = executeParams.predictor;
            Neighbourhood neighbourhood = new SimilarityThresholdingNeighbourhood(threshold);
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

            System.out.println(threshold + "\t"+ibcf.averageNeighbourhoodSize());

            Double RMSE = eval.getRMSE();
            double rmse = (RMSE != null) ? RMSE.doubleValue() : -1;
            double coverage = eval.getCoverage();
            //System.out.println(threshold + "\t" + rmse + "\t" + coverage + "\t"+ ibcf.averageNeighbourhoodSize());
        }
    }
}
