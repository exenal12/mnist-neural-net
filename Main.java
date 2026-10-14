import java.util.Scanner;
import NeuralNetUtils.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the amount of neurons per layer you want.\nThe amount of values entered dictates how many layers there are.\nSeparate counts with commas (i.e. 1,2,3).");

        String neuronCounts = scanner.nextLine();
        Network neuralNet = NeuralNetUtils.Menu.createNewNetwork(neuronCounts);

        neuralNet.createLayers();
        scanner.close();
    }

}