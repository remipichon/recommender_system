/**
 * Used to evaluate the case-based recommendation algorithm
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        Evaluator eval = new Evaluator(recommender, reader);

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



    /**
     * Display confidence and frequency between co-occurring genres frequency computed in the dataset reader
     *
     * @param reader
     */
    static void displayCoOccurringGenreFrequencyAndConfidence(DatasetReader reader){
        System.out.println("**** Confidence and frequency between co-occurring genres frequency ***");
        HashMap<String, Double> coOccuringGenre = reader.getCoOccurringGenres();
        for (Map.Entry<String, Double> frequencyByGenre : coOccuringGenre.entrySet()) {
            System.out.println(frequencyByGenre.getKey()+"\t"+frequencyByGenre.getValue()+"\t"+
                    reader.getConfidenceXY().get(
                            frequencyByGenre.getKey()));
        }
    }

    /**
     * Display genres frequency
     *
     * @param reader
     */
    static void displayGenrePercentageOfTransaction(DatasetReader reader){
        System.out.println("**** genres percentage of transaction ***");
        HashMap<String, Double> supportX = reader.getSupportX();
        for (Map.Entry<String, Double> stringDoubleEntry : supportX.entrySet()) {
            System.out.println(stringDoubleEntry.getKey()+"\t"+stringDoubleEntry.getValue());
        }

        System.out.println("**** coOccurringGenres ***");
        HashMap<String, Double> coOccurringGenres = reader.getCoOccurringGenres();
        for (Map.Entry<String, Double> stringDoubleEntry : coOccurringGenres.entrySet()) {
            System.out.println(stringDoubleEntry.getKey()+"\t"+stringDoubleEntry.getValue());
        }
//
//        System.out.println("**** not coOccurringGenres ***");
//        HashMap<String, Double> coOccurringGenres = reader.g();
//        for (Map.Entry<String, Double> stringDoubleEntry : coOccurringGenres.entrySet()) {
//            System.out.println(stringDoubleEntry.getKey()+"\t"+stringDoubleEntry.getValue());
//        }

        System.out.println("**** genres likings X_Y (the increase in liking Y if X is liked) ***");
        HashMap<String, Double> liking = reader.getLiking();
        for (Map.Entry<String, Double> stringDoubleEntry : liking.entrySet()) {
            System.out.println(stringDoubleEntry.getKey()+"\t"+stringDoubleEntry.getValue());
        }

    }

//    static void displayAllReviewsWords(DatasetReader reader){
//        System.out.println("*** all review word ***");
//        List<String> allReviewWords = reader.getAllReviewWords();
//        for (String word : allReviewWords) {
//            System.out.println(word);
//        }
//
//    }

    static void displayAllMovieTfidf(DatasetReader reader){
        System.out.println("*** all tfidf word per movie ***");

        Map<Integer, Map<String, Double>> tfidfSparseMatrix = reader.getMatrix(); // <movieId, <word, TFIDvalue>>
        Set<String> allReviewWords = reader.getAllReviewWords();
        for (String word : allReviewWords) {
            String str = "";
            str += String.format("%-20s",word);

            for (Map.Entry<Integer, Map<String, Double>> integerMapEntry : tfidfSparseMatrix.entrySet()) {
                Integer movieID = integerMapEntry.getKey();
                str += String.format("%-10s",integerMapEntry.getValue().get(word));
            }

            System.out.println(str);
        }


    }

    static void evaluateAndPrintResult(CaseSimilarity caseSimilarity,String type, DatasetReader reader, Recommender recommender) {
        // evaluate the case-based recommender
        Evaluator eval = new Evaluator(recommender, reader);

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


    static void displayMovieReview(DatasetReader reader){
        Map<Integer, String> moviesReviews = reader.getMoviesReviews();

        for (String s : moviesReviews.values()) {
            System.out.println("******");
            System.out.println(s);
        }

    }
}
