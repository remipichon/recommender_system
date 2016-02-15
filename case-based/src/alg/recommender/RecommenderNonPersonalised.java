/**
 * An abstract class to define a case-based recommender
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.recommender;

import alg.cases.similarity.CaseSimilarity;
import util.Matrix;
import util.reader.DatasetReader;

import java.util.ArrayList;
import java.util.Set;

public abstract class RecommenderNonPersonalised extends Recommender{
    private Matrix matrix; // a Matrix object to store case similarities

    /**
     * constructor - creates a new Recommender object
     *
     * @param caseSimilarity - an object to compute case similarity
     * @param reader         - an object to store user profile data and movie metadata
     */
    public RecommenderNonPersonalised(final CaseSimilarity caseSimilarity, final DatasetReader reader) {
        // compute all case similarities and store in a Matrix object
        this.matrix = new Matrix();
        Set<Integer> ids = reader.getCasebase().getIds();
        for (Integer rowId : ids)
            for (Integer colId : ids)
                if (rowId.intValue() != colId.intValue()) {
                    double sim = caseSimilarity.getSimilarity(reader.getCasebase().getCase(rowId), reader.getCasebase().getCase(colId));
                    if (sim > 0)
                        matrix.addElement(rowId, colId, sim);
                }

        System.out.println("Recommender constructor ok");
    }

    /**
     * returns the case similarity between two cases
     *
     * @param rowId - the id of the first case, candidate Id
     * @param colId - the id of the second case
     * @return the case similarity or null if the Matrix object does not contain the case similarity
     */
    public Double getCaseSimilarity(final Integer rowId, final Integer colId) {
        return matrix.getElement(rowId, colId);
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
