package alg.cases;

/**
 * Created by remi on 03/02/16.
 */
public class MovieRating {
    Integer id;
    Double totalRating;
    Integer popularity;

    /**
     * constructor Create a new Movie Rating
     *
     * @param movieId
     */
    public MovieRating(Integer movieId) {
        this.id = movieId;
        this.popularity = new Integer(0);
        this.totalRating = new Double(0);
    }

    /**
     * Add a new rating to the movie rating
     *
     * @param addedRating value of the new rating
     */
    public void addRating(Double addedRating) {
        this.totalRating += addedRating;
        this.popularity++;
    }

    /**
     * Perform mean rating calculation
     *
     * @return mean rating
     */
    public Double getMeanRating() {
        return totalRating / popularity;
    }

    public Integer getPopularity() {
        return popularity;
    }

    @Override
    public String toString() {
        return
                "id " + id +
                        " popularity " + popularity +
                        " meanRating " + this.getMeanRating();
    }
}
