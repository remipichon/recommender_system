/**
 * Used to evaluate the case-based recommendation algorithm
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg;

import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.recommender.MaxRecommenderPersonalised;
import alg.recommender.Recommender;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

import java.io.File;

public class ExecuteTaskFour extends Execute{
    public static void main(String[] args) {
        // set the paths and filenames of the training, test and movie metadata files and read in the data
        String trainFile = "dataset" + File.separator + "trainData.txt";
        String testFile = "dataset" + File.separator + "testData.txt";
        String movieFile = "dataset" + File.separator + "movies.txt";
        DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);

        // configure the case-based recommendation algorithm - set the case similarity and recommender
        CaseSimilarity overlapCaseSimilarity = new OverlapCaseSimilarity();
        Recommender recommender;


        recommender = new MaxRecommenderPersonalised(overlapCaseSimilarity, reader);

        // evaluate the case-based recommender
        Evaluator eval = new Evaluator(recommender, reader, overlapCaseSimilarity);

        //evaluateAndPrintResult(eval, "overlapCaseSimilarity | MaxRecommender", reader, recommender);

        evaluateAndPrintResultForDiversity(overlapCaseSimilarity, eval, "overlapCaseSimilarity | MaxRecommender", reader, recommender);
    }


}
