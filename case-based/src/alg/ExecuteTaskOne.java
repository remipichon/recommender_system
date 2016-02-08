/**
 * Used to evaluate the case-based recommendation algorithm
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg;

import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.JaccardCaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.recommender.MaxRecommender;
import alg.recommender.MeanRecommender;
import alg.recommender.Recommender;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

import java.io.File;

public class ExecuteTaskOne {
    public static void main(String[] args) {
        // set the paths and filenames of the training, test and movie metadata files and read in the data
        String trainFile = "dataset" + File.separator + "trainData.txt";
        String testFile = "dataset" + File.separator + "testData.txt";
        String movieFile = "dataset" + File.separator + "movies.txt";
        DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);

        // configure the case-based recommendation algorithm - set the case similarity and recommender
        CaseSimilarity overlapCaseSimilarity = new OverlapCaseSimilarity();
        JaccardCaseSimilarity jaccardCaseSimilarity = new JaccardCaseSimilarity();

        Recommender recommender;


        recommender = new MaxRecommender(overlapCaseSimilarity, reader);
        evaluateAndPrintResult("overlapCaseSimilarity | MaxRecommender", reader, recommender);

        recommender = new MaxRecommender(jaccardCaseSimilarity, reader);
        evaluateAndPrintResult("jaccardCaseSimilarity | MaxRecommender", reader, recommender);

        recommender = new MeanRecommender(overlapCaseSimilarity, reader);
        evaluateAndPrintResult("overlapCaseSimilarity | MeanRecommender", reader, recommender);

        recommender = new MeanRecommender(jaccardCaseSimilarity, reader);
        evaluateAndPrintResult("jaccardCaseSimilarity | MeanRecommender", reader, recommender);


    }

    private static void evaluateAndPrintResult(String type, DatasetReader reader, Recommender recommender) {
        // evaluate the case-based recommender
        Evaluator eval = new Evaluator(recommender, reader);

        System.out.println(type);
        System.out.println("topN\tRecall\tPrecision for " + type);
        for (int topN = 5; topN <= 50; topN += 5) //the size of the recommendation list
            System.out.println(topN + "\t" + eval.getRecall(topN) + "\t" + eval.getPrecision(topN));
    }
}
