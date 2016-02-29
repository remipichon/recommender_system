/**
 * Compute the Pearson similarity between profiles
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package similarity.metric;

import profile.Profile;

import java.util.Set;

public class CosineMetric implements SimilarityMetric {
    /**
     * constructor - creates a new CosineMetric object
     */
    public CosineMetric() {
    }

    /**
     * computes the similarity between profiles
     *
     * @param profile 1
     * @param profile 2
     */
    public double getSimilarity(final Profile p1, final Profile p2) {
        double sum_r1_sq = 0;
        double sum_r2_sq = 0;
        double sum_r1_r2 = 0;


        Set<Integer> p1Ids = p1.getIds();
        for (Integer id : p1Ids) {
            sum_r1_sq += Math.pow(p1.getValue(id).doubleValue(),2);
        }
        Set<Integer> p2Ids = p1.getIds();
        for (Integer id : p2Ids) {
            sum_r2_sq += Math.pow(p2.getValue(id).doubleValue(),2);
        }


        Set<Integer> common = p1.getCommonIds(p2);
        for (Integer id : common) { //all common rated movies
            double r1 = p1.getValue(id).doubleValue();
            double r2 = p2.getValue(id).doubleValue();

            sum_r1_r2 += r1 * r2;
        }

        double above = (common.size() > 0) ? sum_r1_r2  : 0;
        double below = (common.size() > 0) ? Math.sqrt(sum_r1_sq) * Math.sqrt(sum_r2_sq) : 0;
        return (below > 0) ? above / below : 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
