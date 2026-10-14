package NeuralNetUtils;

import java.util.Arrays;
import java.util.Random;

// the Network class is a doubly-linked list of Layer objects
public class Network {
    // Layer class will be where the math happens
    private class Layer {
        int neurons;
        Layer prevLayer;
        Layer nextLayer;
        double[] activations;
        double[] biases;
        double[][] weights;
        
        // simple layer constructor
        Layer(int neurons) {
            this.neurons = neurons;
            this.prevLayer = null;
            this.nextLayer = null;
        }

        private static double sigmoid(double zValue) {
            return (1.0 / (1.0 + (Math.pow(Math.E, (-zValue)))));
        }

        // will be called during Network creation
        private void init(Random random){
            // create activations, biases arrays
            activations = new double[neurons];
            biases = new double[neurons];
            
            // create weights only if not the first layer
            if (prevLayer != null) {
                // xavier initialization for weight generation
                double weightScale = Math.sqrt(6.0 / (prevLayer.neurons + neurons));

                // rows = # of neurons in this layer
                // cols = # of neurons in prev layer
                weights = new double[neurons][prevLayer.neurons];

                // fill weights
                for (int i = 0; i < weights.length; i++) {
                    for (int j = 0; j < weights[i].length; j++) {
                        weights[i][j] = (random.nextDouble() * 2 - 1) * weightScale;
                    }
                }
            }
        }

        private void calculateActivations() {
            // due to how i coded my matrix functions, must convert activations/biases to single-column matrices
            double[][] prevActivations = new double[prevLayer.activations.length][1];
            double[][] biasMatrix = new double[neurons][1];

            for (int i = 0; i < prevActivations.length; i++) {
                prevActivations[i][0] = prevLayer.activations[i];
            }

            for (int i = 0; i < neurons; i++) {
                biasMatrix[i][0] = this.biases[i];
            }

            // zMatrix = (W x A) + B
            double[][] zMatrix = Linear.Matrix.add(
                Linear.Matrix.multiply(this.weights, prevActivations),
                biasMatrix
            );

            for (int i = 0; i < neurons; i++) {
                this.activations[i] = sigmoid(zMatrix[i][0]);
            }
        }
    }

    Random random = new Random();
    int[] neuronsPerLayer;
    int layers;
    double learningRate;

    Layer headLayer;
    Layer tailLayer;

    // constructor sets neurons per layer
    public Network(int[] neuronsPerLayer) {
        this.neuronsPerLayer = neuronsPerLayer;
        this.layers = 0;
        this.learningRate = 0.10;
    }

    // adds layer to linked list
    public void addLayer(int neurons) {
        Layer newLayer = new Layer(neurons);

        if (headLayer == null) {
            headLayer = newLayer;
            tailLayer = newLayer;
            newLayer.init(random);
            layers++;
            return;
        }

        tailLayer.nextLayer = newLayer;
        newLayer.prevLayer = tailLayer;
        tailLayer = newLayer;

        // increase count and init layer
        layers++;
        newLayer.init(random);
    }
    
    public void createLayers() {
        // length of neuronsPerLayer dictates number of layers
        for (int i = 0; i < neuronsPerLayer.length; i++) {
            // for each layer, create and append to network linked list
            addLayer(neuronsPerLayer[i]);
        }
    }
    
    // sets activations on first layer
    public void setInputs(double[] inputs) {
        headLayer.activations = inputs;
    }

    public void printInputs() {
        Linear.Vector.print(headLayer.activations);
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
    // traverse linked list and print
    public void printWeightsAndBiases() {
        int counter = 0;
        Layer cur = headLayer;
        while (cur != null) {
            System.out.println("Layer " + counter);
            System.out.println("Weights: ");
            
            if (cur.weights == null) {
                System.out.println("No weights for this layer");
            } else {
                Linear.Matrix.print(cur.weights);
            }
            
            System.out.println("Biases: ");
            if (cur.biases == null) {
                System.out.println("No biases for this layer");
            } else {
                Linear.Vector.print(cur.biases);
            }
            counter++;
            cur = cur.nextLayer;
        }
    }

    public void printNetworkInfo() {
        System.out.println("Learning rate: " + learningRate);
        System.out.println("Layer count: " + layers);
        for (int i = 0; i < layers; i++) {
            System.out.println("Layer " + i + " neuron count: " + neuronsPerLayer[i]);
        }
        System.out.print("Inputs: ");
        printInputs();
    }

    public void part1Setup(){
        // hardcoded layer setup for part 1
        Layer layer0 = headLayer;
        Layer layer1 = layer0.nextLayer;
        Layer layer2 = layer1.nextLayer;
        layer1.biases = new double[]{0.1, -0.36, -0.31};
        layer1.weights = new double[][]{
            {-0.21, 0.72, -0.25, 1},
            {-0.94, -0.41, -0.47, 0.63},
            {0.15, 0.55, -0.49, -0.75},
        };
        layer2.biases = new double[]{0.16, -0.46};
        layer2.weights = new double[][]{
            {0.76, 0.48, -0.73},
            {0.34, 0.89, -0.23},
        };
    }

    public void forwardPass() {
        Layer cur = headLayer.nextLayer;
        while (cur != null) {
            cur.calculateActivations();
            cur = cur.nextLayer;
        }
        Linear.Vector.print(tailLayer.activations);
    }

}


