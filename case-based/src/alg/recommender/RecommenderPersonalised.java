/**
 * An abstract class to define a case-based recommender
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.recommender;

import alg.cases.Case;
import alg.cases.MovieCase;
import alg.cases.similarity.CaseSimilarity;
import alg.feature.similarity.FeatureSimilarity;
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
     * @param profile - user profile
     * @param rowId - the id of the first case, candidate Id
     * @param colId - the id of the second case
     * @return the case similarity or null if the case similarity is not relevant
     */
    public Double getCaseSimilarity(Map<Integer, Double> profile, final Integer rowId, final Integer colId) {

        // #profilemovies
        int userProfileReviewsCount = profile.size();
        int distinctGenresCount;
        int distinctDirectorsCount;
        double genresWeight;
        double directorsWeight;


        if (rowId.intValue() != colId.intValue()) {
            Case c1 = reader.getCasebase().getCase(rowId);
            Case c2 = reader.getCasebase().getCase(colId);

            // #distinct genres
            distinctGenresCount = FeatureSimilarity.distinct(((MovieCase) c1).getGenres(),((MovieCase) c2).getGenres());
            // #distinct directors
            distinctDirectorsCount = FeatureSimilarity.distinct(((MovieCase) c1).getDirectors(),((MovieCase) c2).getDirectors());;

            // W = 1- #distinct /(#profilemovies)
            genresWeight = 1 - distinctGenresCount * 1.0 / userProfileReviewsCount;
            directorsWeight = 1 - distinctDirectorsCount * 1.0 / userProfileReviewsCount;

            double sim = caseSimilarity.getSimilarity(new FeaturesWeight(directorsWeight,genresWeight), c1, c2);
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
