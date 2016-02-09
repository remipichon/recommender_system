/**
 * An abstract class to define a case-based recommender
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.recommender;

import alg.cases.similarity.CaseSimilarity;
import util.FeaturesWeight;
import util.Matrix;
import util.reader.DatasetReader;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public abstract class RecommenderPersonalised extends Recommender{

    private final CaseSimilarity caseSimilarity;
    private final DatasetReader reader;

    /**
     * constructor - creates a new RecommenderPersonalised object
     *
     * Do not pre-compute and store case similarity as it depends on user profile
     *
     * @param caseSimilarity - an object to compute case similarity
     * @param reader         - an object to store user profile data and movie metadata
     */
    public RecommenderPersonalised(final CaseSimilarity caseSimilarity, final DatasetReader reader) {
        this.caseSimilarity = caseSimilarity;
        this.reader = reader;
    }

    /**
     * returns the case similarity between two cases
     *
     * @param featuresWeight - features weight to tweak features importance
     * @param rowId - the id of the first case, candidate Id
     * @param colId - the id of the second case
     * @return the case similarity or null if the case similarity is not relevant
     */
    public Double getCaseSimilarity(FeaturesWeight featuresWeight, final Integer rowId, final Integer colId) {
        if (rowId.intValue() != colId.intValue()) {
            double sim = caseSimilarity.getSimilarity(featuresWeight,reader.getCasebase().getCase(rowId), reader.getCasebase().getCase(colId));
            if (sim > 0)
                return sim;
        }
        return null;
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
