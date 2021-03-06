/**
 * An class to compute the target user's predicted rating for the target item (item-based CF) using the simple average technique.
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package alg.ib.predictor;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;
import similarity.metric.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeightedAveragePredictor implements Predictor {
    /**
     * constructor - creates a new WeightedAveragePredictor object
     */
    public WeightedAveragePredictor() {
    }

    /**
     * @param userId         - the numeric ID of the target user
     * @param itemId         - the numerid ID of the target item
     * @param userProfileMap - a map containing user profiles
     * @param itemProfileMap - a map containing item profiles
     * @param neighbourhood  - a Neighbourhood object
     * @param simMap         - a SimilarityMap object containing item-item similarities
     * @returns the target user's predicted rating for the target item or null if a prediction cannot be computed
     */
    public Double getPrediction(final Integer userId, final Integer itemId, final Map<Integer, Profile> userProfileMap, final Map<Integer, Profile> itemProfileMap, final Neighbourhood neighbourhood, final SimilarityMap simMap) {
        double above = 0;
        double below = 0;
//        List<Double> weights = new ArrayList<Double>(); //used for experiment 1 to compute the standard deviation for every item similarity weight (see report)

        for (Integer targetItemId : userProfileMap.get(userId).getIds()) // iterate over the target user's items
        {
            if (neighbourhood.isNeighbour(itemId, targetItemId)) // the current item is in the neighbourhood
            {
                Double rating = userProfileMap.get(userId).getValue(targetItemId);
                Double weight = simMap.getSimilarity(itemId,targetItemId);
//                weights.add(weight); //used for experiment 1 to compute the standard deviation for every item similarity weight (see report)

                above += rating.doubleValue() * weight;
                below += Math.abs(weight.doubleValue());
            }
        }

//        Statistics statistics = new Statistics(weights);
//        double stdDev = statistics.getStdDev();
//        if(!Double.isNaN(stdDev)) { //used for experiment 1 to compute the standard deviation for every item similarity weight (see report)
//            System.out.println(stdDev);
//        }

        if (below > 0)
            return new Double((below > 0) ? above / below : 0);
        else
            return null;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}

