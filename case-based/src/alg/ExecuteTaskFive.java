/**
 * Used to evaluate the case-based recommendation algorithm
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg;

import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.OverlapCaseGenreCoOccurringSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.recommender.MaxRecommender;
import alg.recommender.MaxRecommenderPersonalised;
import alg.recommender.Recommender;
import util.reader.DatasetReader;

import java.io.File;

public class ExecuteTaskFive extends Execute{
    public static void main(String[] args) {
        // set the paths and filenames of the training, test and movie metadata files and read in the data
        String trainFile = "dataset" + File.separator + "trainData.txt";
        String testFile = "dataset" + File.separator + "testData.txt";
        String movieFile = "dataset" + File.separator + "movies.txt";
        DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);
        reader.computeCoOccuringGenre();


        displayCoOccurringGenreFrequencyAndConfidence(reader);
        displayGenrePercentageOfTransaction(reader);

        // configure the case-based recommendation algorithm - set the case similarity and recommender
        CaseSimilarity overlapCaseSimilarity = new OverlapCaseGenreCoOccurringSimilarity(reader);
        Recommender recommender;


        recommender = new MaxRecommender(overlapCaseSimilarity, reader);
        evaluateAndPrintResult(overlapCaseSimilarity,"overlapCaseSimilarity | MaxRecommender | genres co-occurring ", reader, recommender);
    }
}
