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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.FeaturesWeight;
import util.reader.DatasetReader;

import java.io.Reader;

public class OverlapCaseCosineReviewSimilarity implements CaseSimilarity {
    final static double GENRE_WEIGHT = 1; // the weight for feature genres
    final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
    final static double ACTOR_WEIGHT = 1; // the weight for feature actors
    final static double REVIEW_WEIGHT = 1; // the weight for feature actors
    private final DatasetReader reader;

    /**
     * constructor - creates a new OverlapCaseSimilarity object
     */
    public OverlapCaseCosineReviewSimilarity(DatasetReader reader) {
        this.reader = reader;
    }

    /**
     * computes the similarity between two cases
     *
     * @param c1 - the first case (candidate)
     * @param c2 - the second case
     * @return the similarity between case c1 and case c2
     */
    public double getSimilarity(final Case c1, final Case c2) {
        MovieCase m1 = (MovieCase) c1;
        MovieCase m2 = (MovieCase) c2;

        double above = GENRE_WEIGHT * FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres()) +
                DIRECTOR_WEIGHT * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) +
                ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors()) +
                REVIEW_WEIGHT * FeatureSimilarity.cosine(reader, m1, m2);

        double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT + REVIEW_WEIGHT;

        return (below > 0) ? above / below : 0;
    }


    @Override
    public double getSimilarity(FeaturesWeight featuresWeight, Case c1, Case c2) {
      throw new NotImplementedException();
    }
}
