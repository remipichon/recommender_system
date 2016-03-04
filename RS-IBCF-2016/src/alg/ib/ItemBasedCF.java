/**
 * A class to implement the item-based collaborative filtering algorithm
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package alg.ib;

import alg.CFAlgorithm;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.Predictor;
import similarity.SimilarityMap;
import similarity.metric.SimilarityMetric;
import util.reader.DatasetReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemBasedCF implements CFAlgorithm {
    private Predictor predictor; // the predictor technique
    private Neighbourhood neighbourhood; // the neighbourhood technique
    private DatasetReader reader; // dataset reader
    private SimilarityMap simMap; // similarity map - stores all item-item similarities

    /**
     * constructor - creates a new UserBasedCF object
     *
     * @param predictor     - the predictor technique
     * @param neighbourhood - the neighbourhood technique
     * @param metric        - the item-item similarity metric
     * @param reader        - dataset reader
     */
    public ItemBasedCF(final Predictor predictor, final Neighbourhood neighbourhood, final SimilarityMetric metric, final DatasetReader reader) {
        this.predictor = predictor;
        this.neighbourhood = neighbourhood;
        this.reader = reader;
        this.simMap = new SimilarityMap(reader.getItemProfiles(), metric); // compute all item-item similarities
        this.neighbourhood.computeNeighbourhoods(simMap); // compute the neighbourhoods for all items
    }

    /**
     * @param userId - the target user ID
     * @param itemId - the target item ID
     * @returns the target user's predicted rating for the target item or null if a prediction cannot be computed
     */
    public Double getPrediction(final Integer userId, final Integer itemId) {
        return predictor.getPrediction(userId, itemId, reader.getUserProfiles(), reader.getItemProfiles(), neighbourhood, simMap);
    }

    public double averageNeighbourhoodSize() {
        Map<Integer, Set<Integer>> neighbourhoodMap = this.neighbourhood.neighbourhoodMap;
        int sum = 0;
        for (Set<Integer> integers : neighbourhoodMap.values()) {
            sum +=integers.size();
        }

        return sum / neighbourhoodMap.values().size();
    }
}