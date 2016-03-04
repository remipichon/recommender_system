/**
 * Compute the Pearson similarity between profiles
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package similarity.metric;

import profile.Profile;

import java.util.Set;

public class JaccardMetric implements SimilarityMetric {

    /**
     * constructor - creates a new JaccardMetric object
     */
    public JaccardMetric(){
    }

    /**
     * computes the similarity between profiles
     *
     * @param profile 1
     * @param profile 2
     */
    public double getSimilarity(final Profile p1, final Profile p2) {

        Set<Integer> common = p1.getCommonIds(p2);
        Set<Integer> union = p1.getUnionIds(p2);

        return new Double(common.size()) / union.size(); //jaccardIndex
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
