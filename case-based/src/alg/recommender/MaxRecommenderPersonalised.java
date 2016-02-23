/**
 * A class to define a case-based recommender.
 * The scoring function used to rank recommendation candidates is the mean similarity to the target user's profile cases.
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.recommender;

import alg.casebase.Casebase;
import alg.cases.Case;
import alg.cases.MovieCase;
import alg.cases.similarity.CaseSimilarity;
import alg.feature.similarity.FeatureSimilarity;
import util.FeaturesWeight;
import util.ScoredThingDsc;
import util.reader.DatasetReader;

import java.util.*;

public class MaxRecommenderPersonalised extends RecommenderPersonalised {
    /**
     * constructor - creates a new MaxRecommender object
     *
     * @param caseSimilarity - an object to compute case similarity
     * @param reader         - an object to store user profile data and movie metadata
     */
    public MaxRecommenderPersonalised(final CaseSimilarity caseSimilarity, final DatasetReader reader) {
        super(caseSimilarity, reader);
    }

    /**
     * returns a ranked list of recommended case ids with dynamic weight according to user profiles
     *
     * @param userId - the id of the target user
     * @param reader - an object to store user profile data and movie metadata
     * @return the ranked list of recommended case ids
     */
    public ArrayList<Integer> getRecommendations(final Integer userId, final DatasetReader reader) {
        SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>();

        // get the target user profile
        Map<Integer, Double> profile = reader.getUserProfile(userId); //<movieId, rating>

        // #profilemovies
        int userProfileReviewsCount = profile.size();

        MovieCase movie;
        Set<String> distinctUserGenres = new HashSet<String>();
        Set<String> distinctUserDirectors = new HashSet<String>();

        for (Integer movieId : profile.keySet()) {
            movie = (MovieCase) reader.getCasebase().getCase(movieId);
            distinctUserGenres.addAll(movie.getGenres());
            distinctUserDirectors.addAll(movie.getDirectors());
        }

        // #distinct genres
        int distinctGenresCount = distinctUserGenres.size();
        // #distinct directors
        int distinctDirectorsCount = distinctUserDirectors.size();

        // W = 1- #distinct /(#profilemovies)
        double genresWeight = 1 - distinctGenresCount * 1.0 / userProfileReviewsCount;
        double directorsWeight = 1 - distinctDirectorsCount * 1.0 / userProfileReviewsCount;

        if(genresWeight < 0) genresWeight = 0;
        if(directorsWeight < 0) directorsWeight = 0;
        

        FeaturesWeight featuresWeight = new FeaturesWeight(directorsWeight,genresWeight);

        // get the ids of all recommendation candidate cases
        Set<Integer> candidateIds = reader.getCasebase().getIds();

        // compute a score for all recommendation candidate cases
        for (Integer candidateId : candidateIds) {
            if (!profile.containsKey(candidateId)) // check that the candidate case is not already contained in the user profile
            {
                double max = 0;

                // iterate over all the target user profile cases and compute a score for the current recommendation candidate case
                for (Integer id : profile.keySet()) {

                    //Double sim = super.getCaseSimilarity(profile,candidateId, id);
                    Double sim = super.getCaseSimilarity(featuresWeight,candidateId, id);

                    if (sim != null && sim > max)
                        max = sim;

                }
                ss.add(new ScoredThingDsc(max, candidateId)); // add the score for the current recommendation candidate case to the set

            }
        }

        // sort the candidate recommendation cases by score (in descending order) and return as recommendations
        ArrayList<Integer> recommendationIds = new ArrayList<Integer>();

        for (Iterator<ScoredThingDsc> it = ss.iterator(); it.hasNext(); ) {
            ScoredThingDsc st = it.next();
            recommendationIds.add((Integer) st.thing);
        }

        return recommendationIds;
    }
}
