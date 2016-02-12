/**
 * A class to evaluate a case-based recommender
 * 
 * Michael O'Mahony
 * 20/01/2011
 */

package util.evaluator;

import java.util.*;

import alg.cases.Case;
import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import util.reader.DatasetReader;
import alg.recommender.Recommender;

public class Evaluator 
{
    private final CaseSimilarity caseSimilarity;
    private Map<Integer,ArrayList<Integer>> recommendations; // stores the ranked list of recommended case ids for each test user
	private DatasetReader reader; // stores user profile data and movie metadata
    private Map<String, ArrayList<Integer>> intersectionPerTopN;

    /**
	 * constructor - creates a new Evaluator object
     * @param recommender - an object to define a case-based recommender
     * @param reader - an object to store user profile data and movie metadata
     * @param overlapCaseSimilarity
     */
	public Evaluator(final Recommender recommender, final DatasetReader reader, CaseSimilarity caseSimilarity)
	{
		recommendations = new HashMap<Integer,ArrayList<Integer>>();
        intersectionPerTopN = new HashMap<String, ArrayList<Integer>>();
		this.reader = reader;
        this.caseSimilarity = caseSimilarity;
		
		int counter = 0;
		for(Integer userId: reader.getTestIds())
		{
			if(++counter %100 == 0) System.out.print(".");
			recommendations.put(userId, recommender.getRecommendations(userId, reader));
		}
		System.out.println();
	}

    public double getDiversity(int topN){
        double sumDiversity = 0;
        Case c1;
        Case c2;
        int N;
        double down, upper, userDiversity;

        System.out.println("user diversity");
        for(Integer userId: recommendations.keySet()) {
            ArrayList<Integer> intersection = getIntersection(userId,recommendations.get(userId), reader.getTestProfile(userId), topN); //is integer a list of id ?

            N = intersection.size();
            if(N == 0){
                continue;
            }
            if(N == 1){
                continue;
            }
            down = N * (N - 1);


            for (Integer ri : intersection) {
                c1 = reader.getCasebase().getCase(ri);
                for (Integer rj : intersection) {
                    if (ri.equals(rj)) continue;
                    c2 = reader.getCasebase().getCase(rj);

                    upper = 1 - caseSimilarity.getSimilarity(c1, c2);
                    sumDiversity += upper;
                }
            }

            userDiversity = sumDiversity / down;
            System.out.println(userDiversity);

            sumDiversity += userDiversity;
        }

        return sumDiversity / recommendations.keySet().size();
    }
	
	/**
	 * computes the mean precision (over all test users) for a given recommendation list size provided by the case-based recommender
	 * @param topN - the size of the recommendation list
	 * @return the precision @ topN
	 */
	public double getPrecision(final int topN)
	{
		double sum = 0;
		
		for(Integer userId: recommendations.keySet())
		{	
			int size = getIntersection(userId,recommendations.get(userId), reader.getTestProfile(userId), topN).size();
			sum += (topN > 0) ? size * 1.0 / topN : 0;
		}
		
		return (recommendations.keySet().size() > 0) ? sum / recommendations.keySet().size() : 0;
	}

	/**
	 * computes the mean recall (over all test users) for a given recommendation list size provided by the case-based recommender
	 * @param topN - the size of the recommendation list
	 * @return the recall @ topN
	 */
	public double getRecall(final int topN)
	{
		double sum = 0;
		
		for(Integer userId: recommendations.keySet())
		{	
			int size = getIntersection(userId,recommendations.get(userId), reader.getTestProfile(userId), topN).size();
			sum += (reader.getTestProfile(userId).size() > 0) ? size * 1.0 / reader.getTestProfile(userId).size() : 0;
		}
		
		return (recommendations.keySet().size() > 0) ? sum / recommendations.keySet().size() : 0;
	}
	
	/**
	 * gets the intersection between two sets
	 * @param recommendations - the ranked list of recommended case ids
	 * @param testProfile - the user test profile
	 * @param topN - the size of the recommendation list
	 * @return the intersection between two sets
	 */
	private ArrayList<Integer> getIntersection(Integer userId, final ArrayList<Integer> recommendations, final Map<Integer,Double> testProfile, final int topN)
	{

        if(intersectionPerTopN.containsKey(topN+userId))
            return intersectionPerTopN.get(topN+userId);

        //filter to get only the most recommendable


        int threshold = 10;
        //we take threshold times more recommendations
        ArrayList<Integer> recommendationsBigSet = new ArrayList<Integer>(recommendations.subList(0,Math.min(recommendations.size() ,topN) * threshold));

        //we filter to get 10 recommendations which maximise diversity

        //les topN derniers
        //recommendationsFiltered = new ArrayList<Integer>(recommendationsBigSet.subList(recommendationsBigSet.size()-topN,recommendationsBigSet.size()));

        //topN random
        Random randomGenerator = new Random();
        int randomIndex;
//        Integer randomRecommendation;
        List<Integer> recommendationsFiltered = new ArrayList<Integer>();

        for (int i = 0; i < topN; i++) {
            randomIndex = randomGenerator.nextInt(recommendationsBigSet.size());
            Integer randomRecommendation = recommendationsBigSet.get(randomIndex);
            recommendationsFiltered.add(randomRecommendation);
            recommendationsBigSet.remove(randomIndex);
        }


        ArrayList<Integer> intersection = new ArrayList<Integer>();
        for(int i = 0; i < recommendationsFiltered.size() && i < topN * 10; i++) //ten times more recommendation
		{
			Integer id = recommendationsFiltered.get(i);
			if(testProfile.containsKey(id))
				intersection.add(id);
		}


        intersectionPerTopN.put(""+topN+userId,intersection);

		return intersection;
	}	
}
