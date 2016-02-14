/**
 * A class containing various feature similarity metric implementations
 *
 * Michael O'Mahony
 * 10/01/2013
 */

package alg.feature.similarity;

import util.reader.DatasetReader;

import java.util.*;

public class FeatureSimilarity {
    /**
     * computes exact match similarity between string feature values
     *
     * @param s1 - the first feature value
     * @param s2 - the second feature value
     * @return the similarity between string feature values
     */
    public static double exact(final String s1, final String s2) {
        if (s1.trim().compareToIgnoreCase(s2.trim()) == 0) return 1.0;
        else return 0.0;
    }

    /**
     * computes overlap similarity between set feature values
     *
     * @param s1 - the first feature value
     * @param s2 - the second feature value
     * @return the similarity between set feature values
     */
    public static double overlap(final Set<String> s1, final Set<String> s2) {
        int intersection = 0;

        for (String str : s1)
            if (s2.contains(str))
                intersection++;

        int min = (s1.size() < s2.size()) ? s1.size() : s2.size();
        return (min > 0) ? intersection * 1.0 / min : 0;
    }

    /**
     * computes overlap similarity between set feature values and check if genres likes each other
     *
     * @param genres1 - the first feature value
     * @param genres2 - the second feature value
     * @return the similarity between set feature values
     */
    public static double overlapImproved(DatasetReader reader, final Set<String> genres1, final Set<String> genres2) {
        int intersection = 0;

        for (String genre1 : genres1) {

            if (genres2.contains(genre1))
                intersection++;
            else {
                //perhaps genres are related
                for (String genre : genres2) {
                    //first, sort genres
                    List<String> sorted = new ArrayList<String>(Arrays.asList(genre1,genre));
                    Collections.sort(sorted);

                    //then get confidence if exist
                    Double confidence = reader.getConfidenceXY().get(sorted.get(0) + "_" + sorted.get(1));
                    if(confidence == null) continue;

                    //if the two genres are confident, it equal to a match
                    if(confidence > 0.9) {
                        intersection++;
                        break;
                    }

                }
            }
        }

        int min = (genres1.size() < genres2.size()) ? genres1.size() : genres2.size();
        return (min > 0) ? intersection * 1.0 / min : 0;
    }


    /**
     * computes distinct similarity between set feature values
     *
     * @param s1 - the first feature value
     * @param s2 - the second feature value
     * @return the distinct between set feature values
     */
    public static int distinct(final Set<String> s1, final Set<String> s2) {
        int distinct = 0;

        for (String str : s1)
            if (!s2.contains(str))
                distinct++;

       return distinct;
    }

    /**
     * Use if higher candidate feature value is desired
     *
     * @param candidate
     * @param target
     * @return
     */
    public static double asymmetricSimilarityHigherValue(Double candidate, Double target) {
        double sim;
        sim = 1 - Math.abs(target - candidate) / Math.max(target, candidate);
        return sim;
    }

    /**
     * Use if lower candidate feature value is desired
     *
     * @param candidate
     * @param target
     * @return
     */
    public static double asymmetricSimilarityLowerValue(Double candidate, Double target) {
        double sim;
        sim = 1 - Math.abs(target - candidate) / (target + Math.max(0, (target - candidate)));
        return sim;
    }

    /**
     * Use if for symmetric similarity
     *
     * @param candidate
     * @param target
     * @return
     */
    public static double symmetricSimilarity(Double candidate, Double target) {
        double sim;
        sim = 1 - Math.abs(target - candidate) / target;
        return sim;
    }

    /**
     * computes Jaccard similarity between set feature values
     *
     * @param s1 - the first feature value
     * @param s2 - the second feature value
     * @return the similarity between set feature values
     */
    public static double Jaccard(final Set<String> s1, final Set<String> s2) {
        int intersection = 0;

        for (String str : s1)
            if (s2.contains(str))
                intersection++;

        int union = s1.size() + s2.size() - intersection;
        return (union > 0) ? intersection * 1.0 / union : 0;
    }

}
