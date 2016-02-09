/**
 * A class to read in and store user profile data and movie metadata. Also reads in test user profile data.
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package util.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;


import alg.casebase.Casebase;
import alg.cases.MovieCase;
import alg.cases.MovieRating;

public class DatasetReader {
    private Casebase cb; // stores case objects
    private Map<Integer, Map<Integer, Double>> userProfiles; // stores training user profiles <userId, <movieId, rating>>
    private Map<Integer, MovieRating> moviesRatings; // stores training movies mean rating and popularity (count rating)
    private Map<Integer, Map<Integer, Double>> testProfiles; // stores test user profiles

    /**
     * constructor - creates a new DatasetReader object
     *
     * @param trainFile - the path and filename of the file containing the training user profile data
     * @param testFile  - the path and filename of the file containing the test user profile data
     * @param movieFile - the path and filename of the file containing case metadata
     */
    public DatasetReader(final String trainFile, final String testFile, final String movieFile) {
        userProfiles = readUserProfiles(trainFile);
        moviesRatings = computeMovieRating(userProfiles);
        testProfiles = readUserProfiles(testFile);
        readCasebase(movieFile);
    }

    /**
     * @return the training user profile ids
     */
    public Set<Integer> getUserIds() {
        return userProfiles.keySet();
    }

    /**
     * @param id - the id of the training user profile to return
     * @return the training user profile
     */
    public Map<Integer, Double> getUserProfile(final Integer id) {
        return userProfiles.get(id);
    }

    /**
     * @return the test user profile ids
     */
    public Set<Integer> getTestIds() {
        return testProfiles.keySet();
    }

    /**
     * @param id - the id of the test user profile to return
     * @return the test user profile
     */
    public Map<Integer, Double> getTestProfile(final Integer id) {
        return testProfiles.get(id);
    }

    /**
     * @return the casebase
     */
    public Casebase getCasebase() {
        return cb;
    }

    /**
     * @return the movies ratings
     */
    public Map<Integer, MovieRating> getMoviesRatings() {
        return moviesRatings;
    }

    /**
     * @param filename - the path and filename of the file containing the user profiles
     * @return the user profiles
     */
    private Map<Integer, Map<Integer, Double>> readUserProfiles(final String filename) {
        Map<Integer, Map<Integer, Double>> map = new HashMap<Integer, Map<Integer, Double>>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line;
            br.readLine(); // read in header line

            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                if (st.countTokens() != 4) {
                    System.out.println("Error reading from file \"" + filename + "\"");
                    System.exit(1);
                }

                Integer userId = new Integer(st.nextToken());
                Integer movieId = new Integer(st.nextToken());
                Double rating = new Double(st.nextToken());
                String review = st.nextToken();

                Map<Integer, Double> profile = (map.containsKey(userId) ? map.get(userId) : new HashMap<Integer, Double>());
                profile.put(movieId, rating);
                map.put(userId, profile);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return map;
    }

    /**
     * creates the casebase
     *
     * @param filename - the path and filename of the file containing the movie metadata
     */
    private void readCasebase(final String filename) {
        cb = new Casebase();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line;

            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                if (st.countTokens() != 5) {
                    System.out.println("Error reading from file \"" + filename + "\"");
                    System.out.println(line);
                    System.out.println(st.countTokens());
                    System.exit(1);
                }

                Integer id = new Integer(st.nextToken());
                String title = st.nextToken();
                ArrayList<String> genres = tokenizeString(st.nextToken());
                ArrayList<String> directors = tokenizeString(st.nextToken());
                ArrayList<String> actors = tokenizeString(st.nextToken());

                MovieRating movieRating = moviesRatings.get(id);
                MovieCase movie = new MovieCase(id, title, genres, directors, actors, movieRating.getMeanRating(), movieRating.getPopularity());
                cb.addCase(id, movie);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * @param str - the string to be tokenized; ',' is the delimiter character
     * @return a list of string tokens
     */
    private ArrayList<String> tokenizeString(final String str) {
        ArrayList<String> al = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(str, ",");
        while (st.hasMoreTokens())
            al.add(st.nextToken().trim());

        return al;
    }

    /**
     * @param userProfiles
     * @return Movie Rating (mean rating and popularity) for each movies (represented as movieId)
     */
    private Map<Integer, MovieRating> computeMovieRating(Map<Integer, Map<Integer, Double>> userProfiles) {
        Map<Integer, MovieRating> ratingPerMovies = new HashMap<Integer, MovieRating>();

        for (Map<Integer, Double> moviesRatingsPerUser : userProfiles.values()) {
            for (Map.Entry<Integer, Double> movieIdRating : moviesRatingsPerUser.entrySet()) {
                Integer movieId = movieIdRating.getKey();
                Double rating = movieIdRating.getValue();

                if (!ratingPerMovies.containsKey(movieId)) {
                    ratingPerMovies.put(movieId, new MovieRating(movieId));
                }

                ratingPerMovies.get(movieId).addRating(rating);
            }
        }

        return ratingPerMovies;
    }
}
