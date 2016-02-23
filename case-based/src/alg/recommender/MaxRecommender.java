/**
 * A class to define a case-based recommender.
 * The scoring function used to rank recommendation candidates is the mean similarity to the target user's profile cases.
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.recommender;

import alg.cases.similarity.CaseSimilarity;
import util.ScoredThingDsc;
import util.reader.DatasetReader;

import java.util.*;

public class MaxRecommender extends RecommenderNonPersonalised {
    /**
     * constructor - creates a new MaxRecommender object
     *
     * @param caseSimilarity - an object to compute case similarity
     * @param reader         - an object to store user profile data and movie metadata
     */
    public MaxRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader) {
        super(caseSimilarity, reader);
    }

    /**
     * returns a ranked list of recommended case ids
     *
     * @param userId - the id of the target user
     * @param reader - an object to store user profile data and movie metadata
     * @return the ranked list of recommended case ids
     */
    public ArrayList<Integer> getRecommendations(final Integer userId, final DatasetReader reader) {
        SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>();

        // get the target user profile
        Map<Integer, Double> profile = reader.getUserProfile(userId);

        // get the ids of all recommendation candidate cases
        Set<Integer> candidateIds = reader.getCasebase().getIds();

        // compute a score for all recommendation candidate cases
        for (Integer candidateId : candidateIds) {
            if (!profile.containsKey(candidateId)) // check that the candidate case is not already contained in the user profile
            {
                //System.out.print("_");
                double max = 0;

                // iterate over all the target user profile cases and compute a score for the current recommendation candidate case
                for (Integer id : profile.keySet()) {
                    //System.out.print("*");
                    Double sim = super.getCaseSimilarity(candidateId, id);
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
