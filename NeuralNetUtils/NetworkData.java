package NeuralNetUtils;
import java.util.ArrayList;

public class NetworkData {
    // using an ArrayList due to dynamic sizing
    // this will make loading data much easier
    ArrayList<TrainingData> training = new ArrayList<>();
    ArrayList<double[]> testing = new ArrayList<>();
    ArrayList<ArrayList<TrainingData>> minibatches = new ArrayList<>();

}
