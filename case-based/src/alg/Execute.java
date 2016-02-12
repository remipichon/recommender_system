/**
 * Used to evaluate the case-based recommendation algorithm
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg;

import java.io.File;
import java.util.Map;

import alg.cases.MovieRating;
import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.JaccardCaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.recommender.Recommender;
import alg.recommender.MeanRecommender;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class Execute {
    public static void main(String[] args) {
        // set the paths and filenames of the training, test and movie metadata files and read in the data
        String trainFile = "dataset" + File.separator + "trainData.txt";
        String testFile = "dataset" + File.separator + "testData.txt";
        String movieFile = "dataset" + File.separator + "movies.txt";
        DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);

        // configure the case-based recommendation algorithm - set the case similarity and recommender
        CaseSimilarity caseSimilarity = new OverlapCaseSimilarity();
        JaccardCaseSimilarity jaccardCaseSimilarity = new JaccardCaseSimilarity();
        Recommender recommender = new MeanRecommender(jaccardCaseSimilarity, reader);

        // evaluate the case-based recommender
        Evaluator eval = new Evaluator(recommender, reader, jaccardCaseSimilarity);

        System.out.println("topN\tRecall\tPrecision");
        for (int topN = 5; topN <= 50; topN += 5) //the size of the recommendation list
            System.out.println(topN + "\t" + eval.getRecall(topN) + "\t" + eval.getPrecision(topN));
    }

    /**
     * Display movies ratings already computed in the dataset reader
     *
     * @param reader
     */
    static void displayMoviesRatings(DatasetReader reader) {
        System.out.println("**** Movies ratings ***");
        Map<Integer, MovieRating> moviesRatings = reader.getMoviesRatings();
        for (MovieRating movieRating : moviesRatings.values()) {
            System.out.println(movieRating);
        }
    }

    static void evaluateAndPrintResult(CaseSimilarity caseSimilarity,String type, DatasetReader reader, Recommender recommender) {
        // evaluate the case-based recommender
        Evaluator eval = new Evaluator(recommender, reader, caseSimilarity);

        System.out.println(type);
        System.out.println("topN\tRecall\tPrecision for " + type);
        for (int topN = 5; topN <= 50; topN += 5) //the size of the recommendation list
            System.out.println(topN + "\t" + eval.getRecall(topN) + "\t" + eval.getPrecision(topN));
    }

    static void evaluateAndPrintResult(Evaluator eval, CaseSimilarity caseSimilarity,String type, DatasetReader reader, Recommender recommender) {
        // evaluate the case-based recommender
        System.out.println(type);
        System.out.println("topN\tRecall\tPrecision for " + type);
        for (int topN = 5; topN <= 50; topN += 5) //the size of the recommendation list
            System.out.println(topN + "\t" + eval.getRecall(topN) + "\t" + eval.getPrecision(topN));
    }

    static void evaluateAndPrintResultForDiversity(CaseSimilarity caseSimilarity,Evaluator eval, String type, DatasetReader reader, Recommender recommender) {

        System.out.println("** diversity **");
        System.out.println("topN\tDiversity for " + type);
        for (int topN = 5; topN <= 50; topN += 5) {//the size of the recommendation list
            System.out.println(topN + "\t" + eval.getDiversity(topN));
        }
    }
}
