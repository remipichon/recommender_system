package util;

/**
 * Created by remi on 09/02/16.
 */
public class FeaturesWeight {
    private double directorsWeight;
    private double genresWeight;

    public FeaturesWeight(double directorsWeight, double genresWeight) {
        this.directorsWeight = directorsWeight;
        this.genresWeight = genresWeight;
    }

    public double getDirectorsWeight() {
        return directorsWeight;
    }

    public double getGenresWeight() {
        return genresWeight;
    }
}
