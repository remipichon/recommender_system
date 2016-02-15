/**
 * Used to evaluate the case-based recommendation algorithm
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg;

import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.OverlapCaseCosineReviewSimilarity;
import alg.cases.similarity.OverlapCaseGenreCoOccurringSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.recommender.MaxRecommender;
import alg.recommender.Recommender;
import util.reader.DatasetReader;

import java.io.File;

public class ExecuteTaskSix extends Execute{
    public static void main(String[] args) {
        // set the paths and filenames of the training, test and movie metadata files and read in the data
        String trainFile = "dataset" + File.separator + "trainData.txt";
        String testFile = "dataset" + File.separator + "testData.txt";
        String movieFile = "dataset" + File.separator + "movies.txt";
        DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);

        System.out.println("reader ok");

       // displayMovieReview(reader);

        // configure the case-based recommendation algorithm - set the case similarity and recommender
        CaseSimilarity overlapCaseSimilarity = new OverlapCaseCosineReviewSimilarity();
//        CaseSimilarity overlapCaseSimilarity = new OverlapCaseSimilarity();
        Recommender recommender;


        recommender = new MaxRecommender(overlapCaseSimilarity, reader);
        evaluateAndPrintResult(overlapCaseSimilarity,"overlapCaseSimilarity | MaxRecommender | cosine similarity for reviews", reader, recommender);
    }
}
