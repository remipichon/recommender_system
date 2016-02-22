/**
 * A class to evaluate a case-based recommender
 * 
 * Michael O'Mahony
 * 20/01/2011
 */

package util.evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import alg.cases.Case;
import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import util.reader.DatasetReader;
import alg.recommender.Recommender;

public class Evaluator 
{
	private Map<Integer,ArrayList<Integer>> recommendations; // stores the ranked list of recommended case ids for each test user
	private DatasetReader reader; // stores user profile data and movie metadata
	
	/**
	 * constructor - creates a new Evaluator object
	 * @param recommender - an object to define a case-based recommender
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public Evaluator(final Recommender recommender, final DatasetReader reader)
	{
		recommendations = new HashMap<Integer,ArrayList<Integer>>();
		this.reader = reader;
		
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

        CaseSimilarity caseSimilarity = new OverlapCaseSimilarity();

        System.out.println("user diversity");
        for(Integer userId: recommendations.keySet()) {
            ArrayList<Integer> intersection = getIntersection(recommendations.get(userId), reader.getTestProfile(userId), topN); //is integer a list of id ?

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
			int size = getIntersection(recommendations.get(userId), reader.getTestProfile(userId), topN).size();
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
			int size = getIntersection(recommendations.get(userId), reader.getTestProfile(userId), topN).size();
			sum += (reader.getTestProfile(userId).size() > 0) ? size * 1.0 / reader.getTestProfile(userId).size() : 0;
		}
		
		return (recommendations.keySet().size() > 0) ? sum / recommendations.keySet().size() : 0;
	}
	
	/**
	 * gets the intersection between two sets
	 * @param recs - the ranked list of recommended case ids
	 * @param testProfile - the user test profile
	 * @param topN - the size of the recommendation list
	 * @return the intersection between two sets
	 */
	private ArrayList<Integer> getIntersection(final ArrayList<Integer> recs, final Map<Integer,Double> testProfile, final int topN)
	{
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for(int i = 0; i < recs.size() && i < topN; i++)
		{
			Integer id = recs.get(i);
			if(testProfile.containsKey(id))
				intersection.add(id);
		}

		return intersection;
	}


}
