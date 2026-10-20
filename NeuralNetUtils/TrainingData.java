package NeuralNetUtils;

/*
name: Alyx Braud
assignment: MNIST Neural Net Pt. 1
Date: 10/20/2026
*/

public class TrainingData {
    double[] input;
    double[] expected;

    public TrainingData(double[] input, double[] expected) {
        this.input = input;
        this.expected = expected;
    }
}