/**
 * An interface to compute the similarity between profiles
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package similarity.metric;

import profile.Profile;

public interface SimilarityMetric {
    /**
     * @param p1
     * @param p2
     * @returns the similarity between two profiles
     */
    public double getSimilarity(final Profile p1, final Profile p2);
}
