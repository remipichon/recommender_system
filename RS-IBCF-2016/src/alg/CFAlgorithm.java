/**
 * A collaborative filtering algorithm interface
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package alg;

public interface CFAlgorithm {
    /**
     * @param userId - the target user ID
     * @param itemId - the target item ID
     * @returns the target user's predicted rating for the target item or null if a prediction cannot be computed
     */
    public Double getPrediction(final Integer userId, final Integer itemId);
}
