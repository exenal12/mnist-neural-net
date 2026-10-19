package NeuralNetUtils;

public class TrainingData {
    double[] input;
    double[] expected;

    public TrainingData(double[] input, double[] expected) {
        this.input = input;
        this.expected = expected;
    }
}