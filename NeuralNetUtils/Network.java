package NeuralNetUtils;

import java.util.ArrayList;
import java.util.List;
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
    int batchSize;
    int epochs;
    boolean debug;

    NetworkData data;
    Layer headLayer;
    Layer tailLayer;

    FileIO fileIO = new FileIO();


    // constructor sets neurons per layer
    public Network(int[] neuronsPerLayer) {
        this.neuronsPerLayer = neuronsPerLayer;
        this.layers = 0;
        this.learningRate = 0.10;
        this.data = new NetworkData();
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

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
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
        System.out.println("Number of epochs: " + epochs);
        System.out.println("Debug: " + (debug ? "on" : "off"));
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

        // set weights and biases
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

        // set epochs, learning rate
        this.epochs = 6;
        this.learningRate = 10.0;

        // hardcoded training data
        data.training.add(new TrainingData(
            new double[] {0,1,0,1},
            new double[] {0,1}
        ));

        data.training.add(new TrainingData(
            new double[] {1,0,1,0},
            new double[] {1,0}
        ));

        data.training.add(new TrainingData(
            new double[] {0,0,1,1},
            new double[] {0,1}
        ));

        data.training.add(new TrainingData(
            new double[] {1,1,0,0},
            new double[] {1,0}
        ));

        // add data to minibatches
        data.minibatches.add(new ArrayList<>(List.of(
            data.training.get(0),
            data.training.get(1)
        )));
        data.minibatches.add(new ArrayList<>(List.of(
            data.training.get(2),
            data.training.get(3)
        )));

        // set testing data
        data.testing.add(new double[]{0,1,0,1});
        data.testing.add(new double[]{1,0,1,0});
        data.testing.add(new double[]{0,0,1,1});
        data.testing.add(new double[]{1,1,0,0});
    }

    // traverses linked list and performs a forward pass.
    // after the pass, tailLayer.activations is the final output
    public void forwardPass() {
        Layer cur = headLayer.nextLayer;
        while (cur != null) {
            cur.calculateActivations();
            cur = cur.nextLayer;
        }
    }

    // traverses linked list backwards and calculates gradients
    public void backPropagation() {
        Layer cur = tailLayer;
        while (cur.prevLayer != null) {
            cur.calculateBiasGradient();
            cur.calculateWeightGradient();
            cur = cur.prevLayer;
        }
    }

    private void printCaseDebug(int caseNum, TrainingData sample) {
        System.out.print("    Case " + caseNum + "    input ");
        Linear.Vector.print(sample.input);
        System.out.print("    expected ");
        Linear.Vector.print(sample.expected);
        System.out.print("    output ");
        Linear.Vector.print(Linear.Matrix.getColumn(tailLayer.activations, 0));
        System.out.println();

        int count = 1;
        Layer cur = headLayer.nextLayer;
        while (cur != null) {
            System.out.printf("      Layer %d %-12s ", count, "activations");
            Linear.Matrix.print(cur.activations);
            count++;
            cur = cur.nextLayer;
        }

        count = layers - 1;
        cur = tailLayer;
        while (cur.prevLayer != null) {
            System.out.printf("      Layer %d %-12s ", count, "bias grad");
            Linear.Matrix.print(cur.biasGradient);
            System.out.println("      Layer " + count + " weight grad");
            Linear.Matrix.print(cur.weightGradient, "        ");
            cur = cur.prevLayer;
            count--;
        }
    }

    // computes forward pass, backprop, and weight/bias change for a given batch
    public void calculateNewWeightsAndBiases(ArrayList<TrainingData> batch, int batchNum) {

        // create bias and weight sums, skipping input layer
        int trainableLayers = layers - 1;
        double[][][] biasSums = new double[trainableLayers][][];
        double[][][] weightSums = new double[trainableLayers][][];

        // walk each layer and initialize sum matrices
        // each layer will have different size matrices,
        // so they're being stored as a 3d array
        int index = 0;
        Layer cur = headLayer.nextLayer;
        while (cur != null) {
            biasSums[index] = new double[cur.neurons][1];
            weightSums[index] = new double[cur.neurons][cur.prevLayer.neurons];
            cur = cur.nextLayer;
            index++;
        }

        // for each data sample in a batch:
        // set network's input, set expected output,
        // perform FP + BP,
        // then add to running bias/weight sum
        int caseNum = 1;
        for (TrainingData sample : batch) {
            setInputs(sample.input);
            Linear.Matrix.setColumn(tailLayer.expected, 0, sample.expected);
            forwardPass();
            backPropagation();
            if (debug) {
                printCaseDebug(caseNum, sample);
            }

            index = 0;
            cur = headLayer.nextLayer;
            while (cur != null) {
                biasSums[index] = Linear.Matrix.add(biasSums[index], cur.biasGradient);
                weightSums[index] = Linear.Matrix.add(weightSums[index], cur.weightGradient);
                cur = cur.nextLayer;
                index++;
            }
            caseNum++;
        }

        // calculating the new weights and biases
        // grabs the descent scalar, multiplies by running sums,
        // then sets weights and biases and prints results
        double gd = learningRate / batch.size();
        index = 0;
        int count = 1;
        cur = headLayer.nextLayer;
        if (debug) {
            System.out.println("  Minibatch " + batchNum + " updated weights");
        }
        while (cur != null) {
            double[][] weightProd = Linear.Matrix.scalarMultiply(weightSums[index], gd);
            double[][] biasProd = Linear.Matrix.scalarMultiply(biasSums[index], gd);
            cur.weights = Linear.Matrix.subtract(cur.weights, weightProd);
            cur.biases = Linear.Matrix.subtract(cur.biases, biasProd);
            if (debug) {
                System.out.println("    Layer " + count + " weights");
                Linear.Matrix.print(cur.weights, "      ");
                System.out.print("    Layer " + count + " biases  ");
                Linear.Matrix.print(cur.biases);
            }
            index++;
            count++;
            cur = cur.nextLayer;
        }
    }

    // for n epochs, run the training loop
    public void runTrainingLoop() {
        for (int e = 0; e < epochs; e++) {
            if (debug) System.out.println();
            System.out.println("Epoch " + (e + 1));
            int batchNum = 1;
            for (ArrayList<TrainingData> batch : data.minibatches) {
                if (debug) {
                    System.out.println();
                    System.out.println("  Minibatch " + batchNum);
                }
                calculateNewWeightsAndBiases(batch, batchNum);
                batchNum++;
            }
        }
    }

    // for each piece of data in the testing data,
    // run it through the network and print results
    public void runTestingLoop() {
        for (double[] testData : data.testing) {
            setInputs(testData);
            forwardPass();
            System.out.println("Input data: " + Arrays.toString(testData));
            System.out.println("Network result: "); Linear.Matrix.print(tailLayer.activations);
        }
    }
}


