/**
 * An abstract class to define a case-based recommender
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.recommender;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import util.Matrix;
import util.reader.DatasetReader;
import alg.cases.similarity.CaseSimilarity;

public abstract class Recommender {
    public Recommender() {
    }

    /**
     * returns a ranked list of recommended case ids
     *
     * @param userId - the id of the target user
     * @param reader - an object to store user profile data and movie metadata
     * @return the ranked list of recommended case ids
     */
    public abstract ArrayList<Integer> getRecommendations(final Integer userId, final DatasetReader reader);

}
