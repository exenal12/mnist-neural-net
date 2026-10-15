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
        double[][] activations;
        double[][] biases;
        double[][] weights;
        double[][] biasGradient;
        double[][] weightGradient;
        double[][] expected;

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
            activations = new double[neurons][1];
            biases = new double[neurons][1];
            biasGradient = new double[neurons][1];
            
            // create weights only if not the first layer
            if (prevLayer != null) {
                if (nextLayer == null) {
                    // init expected bias if and only if on the final layer
                    expected = new double[neurons][1];
                }
                // xavier initialization for weight generation
                double weightScale = Math.sqrt(6.0 / (prevLayer.neurons + neurons));

                // rows = # of neurons in this layer
                // cols = # of neurons in prev layer
                weights = new double[neurons][prevLayer.neurons];
                weightGradient = new double[neurons][prevLayer.neurons];

                // fill weights
                for (int i = 0; i < weights.length; i++) {
                    for (int j = 0; j < weights[i].length; j++) {
                        weights[i][j] = (random.nextDouble() * 2 - 1) * weightScale;
                    }
                }
            }
        }

        private void calculateActivations() {
            // zMatrix = (W x A) + B
            double[][] zMatrix = Linear.Matrix.add(
                Linear.Matrix.multiply(this.weights, prevLayer.activations),
                this.biases
            );

            // apply sigmoid function
            for (int i = 0; i < neurons; i++) {
                this.activations[i][0] = sigmoid(zMatrix[i][0]);
            }
        }

        private void calculateBiasGradient() {
            // create matrix of all 1s
            double[][] oneMatrix = new double[this.activations.length][this.activations[0].length];
            for (double[] row : oneMatrix) {
                Arrays.fill(row, 1);
            }

            double[][] oneMinusAct = Linear.Matrix.subtract(oneMatrix, this.activations);
            
            if (nextLayer == null) {
                // if on final layer, the calculation is different
                double[][] actMinusExpected = Linear.Matrix.subtract(this.activations, this.expected);
                double[][] hada = Linear.Matrix.hadamard(actMinusExpected, this.activations);
                double[][] grad = Linear.Matrix.hadamard(hada, oneMinusAct);
                this.biasGradient = grad;    
            } else if (prevLayer != null && nextLayer != null) {
                // hidden layers
                double[][] nextWeightxNextBiasG = Linear.Matrix.multiply(Linear.Matrix.transpose(this.nextLayer.weights), this.nextLayer.biasGradient);
                double[][] hada = Linear.Matrix.hadamard(nextWeightxNextBiasG, this.activations);
                double[][] grad = Linear.Matrix.hadamard(hada, oneMinusAct);
                this.biasGradient = grad;
            }
        }

        private void calculateWeightGradient() {
            if (prevLayer != null) {
                // dont calculate on input layer
                double [][] grad = Linear.Matrix.multiply(this.biasGradient, Linear.Matrix.transpose(this.prevLayer.activations));
                this.weightGradient = grad;
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
        Linear.Matrix.setColumn(headLayer.activations, 0, inputs);
    }

    public void printInputs() {
        Linear.Matrix.print(headLayer.activations);
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
                Linear.Matrix.print(cur.biases);
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
        System.out.println ("Inputs: ");
        printInputs();
        System.out.println("Expected outputs: ");
        Linear.Matrix.print(tailLayer.expected);
    }

    public void part1Setup(){
        // hardcoded layer setup for part 1
        Layer layer0 = headLayer;
        Layer layer1 = layer0.nextLayer;
        Layer layer2 = layer1.nextLayer;
        layer1.biases = new double[][]{{0.1}, {-0.36}, {-0.31}};
        layer1.weights = new double[][]{
            {-0.21, 0.72, -0.25, 1},
            {-0.94, -0.41, -0.47, 0.63},
            {0.15, 0.55, -0.49, -0.75},
        };
        layer2.biases = new double[][]{{0.16}, {-0.46}};
        layer2.weights = new double[][]{
            {0.76, 0.48, -0.73},
            {0.34, 0.89, -0.23},
        };
        layer2.expected = new double[][]{{0.0}, {1.0}};
    }

    // traverses linked list and performs a forward pass.
    // after the pass, tailLayer.activations is the final output
    public void forwardPass() {
        int count = 1;
        Layer cur = headLayer.nextLayer;
        while (cur != null) {
            cur.calculateActivations();
            System.out.println("Layer " + count + " activations:");
            Linear.Matrix.print(cur.activations);
            System.out.println("Layer " + count + " weights:");
            Linear.Matrix.print(cur.weights);
            count++;
            cur = cur.nextLayer;
        }
        Linear.Matrix.print(tailLayer.activations);
    }

    // traverses linked list backwards and calculates gradients
    public void backPropagation() {
        int count = layers-1;
        Layer cur = tailLayer;
        while (cur.prevLayer != null) {
            cur.calculateBiasGradient();
            cur.calculateWeightGradient();
            System.out.println("Layer " + count + " gradients: ");
            System.out.println("Bias: "); Linear.Matrix.print(cur.biasGradient);
            System.out.println("Weight: "); Linear.Matrix.print(cur.weightGradient);
            cur = cur.prevLayer;
            count--;
        }
    }
}


