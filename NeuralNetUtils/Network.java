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

                // print weights
                Linear.Matrix.print(weights);
            }

        }

    }

    Random random = new Random();
    int[] neuronsPerLayer;
    int layers;

    Layer headLayer;
    Layer tailLayer;

    public Network(int[] neuronsPerLayer) {
        this.neuronsPerLayer = neuronsPerLayer;
        this.layers = 0;
    }


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

        layers++;
        // after adding, initialize the layer
        newLayer.init(random);
    }

    public void createLayers() {
        for (int i = 0; i < neuronsPerLayer.length; i++) {
            // for each layer, create and append to network linked list
            addLayer(neuronsPerLayer[i]);
        }
    }

}
