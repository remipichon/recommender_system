/**
 * A class to read in and store product reviews.
 * 
 * Michael O'Mahony
 * 20/02/2013
 */

package util.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Review;

public class DatasetReader 
{
	private ArrayList<Review> reviews; // data structure to store all reviews
	private Set<String> productIds; // data structure to store all distinct product ids

	/**
	 * default constructor - creates a new DatasetReader object
	 */
	public DatasetReader()
	{
		reviews = new ArrayList<Review>();
		productIds = new HashSet<String>();
	}
	
	/**
	 * default constructor - creates a new DatasetReader object
	 * @param filename - the name of the file containing the reviews
	 */
	public DatasetReader(final String filename)
	{
		this();
		readReviews(filename);
	}
	
	/**
	 * @return the reviews
	 */
	public ArrayList<Review> getReviews() {
		return reviews;
	}

	/**
	 * @return the product ids
	 */
	public Set<String> getProductIds() {
		return productIds;
	}
	
	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(final ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	/** 
	 * reads reviews from a file
	 * @param filename - the path and filename of the file containing the reviews
 	 */
	private void readReviews(final String filename) 
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File("dataset" + File.separator + filename)));
			String line;
			
			while ((line = br.readLine()) != null) 
			{
				String[] tokens = line.split(";;");
				if(tokens.length != 12)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}
				
				String reviewId = tokens[0];
				String reviewerUrl = tokens[1];
				
				String productCategory = tokens[2];
				String productId = tokens[3];
				String productName = tokens[4];
				
				String reviewTitle = tokens[5];
				String reviewText = tokens[6];
				double reviewRating = Double.parseDouble(tokens[7]);
				String reviewDate = tokens[8];
				
				int posVotes = Integer.parseInt(tokens[9]);
				int totalVotes = Integer.parseInt(tokens[10]);
				double helpfulness = Double.parseDouble(tokens[11]);
				
				reviews.add(new Review(reviewId, reviewerUrl, productCategory, productId, productName, reviewTitle, reviewText, reviewRating, reviewDate, posVotes, totalVotes, helpfulness));
				productIds.add(productId);
			}
			
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
