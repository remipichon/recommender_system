package alg.ib;

import alg.ib.predictor.DeviationPredictor;
import alg.ib.predictor.Predictor;
import alg.ib.predictor.SimpleAveragePredictor;
import alg.ib.predictor.WeightedAveragePredictor;
import similarity.metric.*;

/**
 * Created by remi on 24/02/16.
 */
public class ExecuteParams {

    Predictor predictor;
    SimilarityMetric metric;



    public void SimpleAveragePredictor_PearsonMetric(){
        predictor = new SimpleAveragePredictor();
        metric = new PearsonMetric();
    }

    public void WeightedAveragePredictor_CosineMetric(){
        predictor = new WeightedAveragePredictor();
        metric = new CosineMetric();
    }

    public void SimpleAveragePredictor_CosineMetric(){
        predictor = new SimpleAveragePredictor();
        metric = new CosineMetric();
    }

    public void DeviationPredictor_CosineMetric(){
        predictor = new DeviationPredictor();
        metric = new CosineMetric();
    }


    public void DeviationPredictor_PearsonMetric() {
        predictor = new DeviationPredictor();
        metric = new PearsonMetric();
    }

    public void DeviationPredictor_PearsonSignifianceWeightMetric() {
        predictor = new DeviationPredictor();
        metric = new PearsonSignifianceWeightMetric();
    }

    public void DeviationPredictor_JaccardMetric() {
        predictor = new DeviationPredictor();
        metric = new JaccardMetric();

    }
}
