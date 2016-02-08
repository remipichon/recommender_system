/**
 * A class to compute the case similarity between objects of type MovieCase
 * Uses the overlap feature similarity metric
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.cases.similarity;

import alg.cases.Case;
import alg.cases.MovieCase;
import alg.feature.similarity.FeatureSimilarity;

public class OverlapCaseAndSymetricSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
    final static double ACTOR_WEIGHT = 1; // the weight for feature actors
    final static double POPULARITY_WEIGHT = 0; // the weight for feature popularity (count ratings)
    final static double MEAN_RATING_WEIGHT = 1; // the weight for feature mean rating
    private double popularityWeight;
    private double meanRatingWeight;

    /**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
    public OverlapCaseAndSymetricSimilarity() {
    }

    /**
     * constructor to specify popularity and mean rating weight in order to disable one or two (if both set to 0 it
     * is the same as OverlapCaseSimilarity class.
     * @param popularityWeight
     * @param meanRatingWeight
     */
	public OverlapCaseAndSymetricSimilarity(double popularityWeight, double meanRatingWeight) {
        this.popularityWeight = popularityWeight;
        this.meanRatingWeight = meanRatingWeight;
    }

	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case (candidate)
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	public double getSimilarity(final Case c1, final Case c2)
	{
		MovieCase m1 = (MovieCase)c1;
		MovieCase m2 = (MovieCase)c2;

		double above = GENRE_WEIGHT * FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres()) +
				DIRECTOR_WEIGHT * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) +
                ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors()) +
                meanRatingWeight * FeatureSimilarity.symmetricSimilarity(m1.getMeanRating(), m2.getMeanRating()) +
                popularityWeight * FeatureSimilarity.symmetricSimilarity(new Double(m1.getPopularity()), new Double(m2.getPopularity()));

		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT + MEAN_RATING_WEIGHT + POPULARITY_WEIGHT;

		return (below > 0) ? above / below : 0;
	}
}
