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

import java.io.*;
import java.util.Map;

public class ExecuteTaskSix extends Execute {
    public static void main(String[] args) {
        boolean restoreMatrixFromFile = true; //if true, read TFIDF and binary from pre-computed file, if false, compute it and store it in a file (15 minutes long)


        // set the paths and filenames of the training, test and movie metadata files and read in the data
        String trainFile = "dataset" + File.separator + "trainData.txt";
        String testFile = "dataset" + File.separator + "testData.txt";
        String movieFile = "dataset" + File.separator + "movies.txt";
        DatasetReader reader = new DatasetReader(trainFile, testFile, movieFile);





        System.out.println("reader finished");

        // displayMovieReview(reader);

        // configure the case-based recommendation algorithm - set the case similarity and recommender
        CaseSimilarity overlapCaseSimilarity = new OverlapCaseCosineReviewSimilarity(reader);
        Recommender recommender;


        if (restoreMatrixFromFile)
            restoreMatrix(reader, "TFIDF");
        else {
            reader.computeTFIDF();
            storeMatrix(reader, "TFIDF");
        }
        recommender = new MaxRecommender(overlapCaseSimilarity, reader);
        evaluateAndPrintResult(overlapCaseSimilarity, "overlapCaseSimilarity | MaxRecommender | cosine similarity for reviews with TFIDF", reader, recommender);

        if (restoreMatrixFromFile)
            restoreMatrix(reader, "binary");
        else {
            reader.computeBinary();
            storeMatrix(reader, "binary");
        }
        recommender = new MaxRecommender(overlapCaseSimilarity, reader);
        evaluateAndPrintResult(overlapCaseSimilarity, "overlapCaseSimilarity | MaxRecommender | cosine similarity for reviews with binary", reader, recommender);
    }

    private static void restoreMatrix(DatasetReader reader, String fileName) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("dataset" + File.separator + fileName + "Matrix.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader.setTfidfSparseMatrix((Map<Integer, Map<String, Double>>) ois.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void storeMatrix(DatasetReader reader, String fileName) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("dataset" + File.separator + fileName + "Matrix.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(reader.getTfidfSparseMatrix());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
