/**
 * Compute the Pearson similarity between profiles
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package similarity.metric;

import profile.Profile;

import java.util.Set;

public class PearsonJaccardSignifianceWeightMetric implements SimilarityMetric {
    private final int N;

    /**
     * constructor - creates a new PearsonSignifianceWeightMetric object
     * @param N significant weight threshold
     */
    public PearsonJaccardSignifianceWeightMetric(final int N) {
        this.N = N;
    }

    /**
     * computes the similarity between profiles
     *
     * @param profile 1
     * @param profile 2
     */
    public double getSimilarity(final Profile p1, final Profile p2) {
        double sum_r1 = 0;
        double sum_r1_sq = 0;
        double sum_r2 = 0;
        double sum_r2_sq = 0;
        double sum_r1_r2 = 0;

        Set<Integer> common = p1.getCommonIds(p2);
        for (Integer id : common) { //all common rated movies
            double r1 = p1.getValue(id).doubleValue();
            double r2 = p2.getValue(id).doubleValue();

            sum_r1 += r1;
            sum_r1_sq += r1 * r1;
            sum_r2 += r2;
            sum_r2_sq += r2 * r2;
            sum_r1_r2 += r1 * r2;
        }

        double above = (common.size() > 0) ? sum_r1_r2 - (sum_r1 * sum_r2) / common.size() : 0;
        double below = (common.size() > 0) ? Math.sqrt((sum_r1_sq - (sum_r1 * sum_r1) / common.size()) * (sum_r2_sq - (sum_r2 * sum_r2) / common.size())) : 0;
        double weigh =  (below > 0) ? above / below : 0;


        Set<Integer> union = p1.getUnionIds(p2);


        int N = 50; //threshold
        boolean condition = (common.size() < N);
        double jaccardIndex = new Double(common.size()) / union.size();

        return (condition)? weigh * jaccardIndex  : weigh;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
