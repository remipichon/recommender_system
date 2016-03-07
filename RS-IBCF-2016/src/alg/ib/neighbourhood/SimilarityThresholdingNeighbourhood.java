/**
 * A class that implements the "nearest neighbourhood" technique (item-based CF)
 *
 * Michael O'Mahony
 * 20/01/2011
 */

package alg.ib.neighbourhood;

import profile.Profile;
import similarity.SimilarityMap;
import util.ScoredThingDsc;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SimilarityThresholdingNeighbourhood extends Neighbourhood {
    private final double L; // threshold L, neighbours for the neighbourhood all have similarity <= L

    /**
     * constructor - creates a new SimilarityThresholdingNeighbourhood object
     *
     * @param L -threshold L, neighbours for the neighbourhood all have similarity <= L
     */
    public SimilarityThresholdingNeighbourhood(final double L) {
        super();
        this.L = L;
    }

    /**
     * stores the neighbourhoods for all items in member Neighbour.neighbourhoodMap
     *
     * @param simMap - a map containing item-item similarities
     */
    public void computeNeighbourhoods(final SimilarityMap simMap) {
        for (Integer itemId : simMap.getIds()) // iterate over each item
        {
            Profile profile = simMap.getSimilarities(itemId); // get the item similarity profile
            if (profile != null) {
                for (Integer id : profile.getIds()) // iterate over each item in the profile
                {
                    double sim = profile.getValue(id);
                    if(Math.abs(sim) > L)
                        this.add(itemId, id);
                }
            }
        }
    }
}
