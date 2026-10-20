package NeuralNetUtils;
import java.util.ArrayList;

/*
name: Alyx Braud
assignment: MNIST Neural Net Pt. 1
Date: 10/20/2026
*/

public class NetworkData {
    // using an ArrayList due to dynamic sizing
    // this will make loading data much easier
    ArrayList<TrainingData> training = new ArrayList<>();
    ArrayList<TrainingData> testing = new ArrayList<>();
    ArrayList<ArrayList<TrainingData>> minibatches = new ArrayList<>();

}
