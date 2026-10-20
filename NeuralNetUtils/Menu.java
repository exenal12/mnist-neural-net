package NeuralNetUtils;
import java.util.Scanner;

/*
name: Alyx Braud
assignment: MNIST Neural Net Pt. 1
Date: 10/20/2026
*/

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean running = true;
    private static Network neuralNet = null;


    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static double[][] parseMatrixInput(String input) {
        // count semicolons; semicolons = rows
        String[] rows = input.split(";");
        String[][] cells = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            cells[i] = rows[i].trim().split("\\s+");
        }
        // creates new matrix using calculated values
        // rows.length = # of rows
        // cells[0].length is the number of values in the first row after splitting on spaces
        // this is the number of columns
        double[][] mat = new double[cells.length][cells[0].length];

        for (int i = 0; i < mat.length; i++) {
            // i = row number
            if (cells[i].length != mat[i].length) {
                throw new IllegalArgumentException("Rows must have the same number of values");
            }
            for (int j = 0; j < mat[i].length; j++) {
                // j = col number
                mat[i][j] = Double.parseDouble(cells[i][j]);
            }
        }

        return mat;
    }

    public static Network createNewNetwork(String input) {
        // count commas
        String[] splitInput = input.split(",");
        int[] neuronsPerLayer = new int[splitInput.length];

        // convert to int[]
        for (int i = 0; i < splitInput.length; i++) {
            neuronsPerLayer[i] = Integer.parseInt(splitInput[i].trim());
        }

        return new Network(neuronsPerLayer);
    }

    public static void mainMenu() {
        while (running) {
            clearScreen();
            System.out.println("MAIN MENU\n\n");
            System.out.println("1. Create network");
            if (neuralNet != null) System.out.println("2. Network options");
            System.out.println("3. Setup for Part 1");
            if (neuralNet != null) System.out.println("4. Start training the network");
            if (neuralNet != null) System.out.println("5. Run testing data");
            System.out.println("0. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    networkPrompt();
                    break;
                case "2":
                    if (neuralNet != null) networkOptions();
                    break;
                case "3":
                    part1Setup();
                    break;
                case "4":
                    if (neuralNet != null) {
                        clearScreen();
                        neuralNet.runTrainingLoop();
                        System.out.println("Press Enter to continue.");
                        scanner.nextLine();
                    }
                    break;
                case "5":
                    if (neuralNet != null) {
                        clearScreen();
                        neuralNet.runTestingLoop();
                        System.out.println("Press Enter to continue.");
                        scanner.nextLine();
                    }
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Press Enter to continue.");
                    scanner.nextLine();
            }
        }
    }

    private static void part1Setup() {
        clearScreen();
        System.out.println("This will set up the network with weights, biases, learning rate, epochs, and training/testing according to part 1 of the assignment.");
        System.out.println("Type yes or no to continue.\n\n");
        System.out.print("> ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "yes":
                neuralNet = new Network(new int[]{4,3,2});
                neuralNet.createLayers();
                neuralNet.part1Setup();
                neuralNet.setLearningRate(10.0);
                neuralNet.setInputs(new double[]{0,1,0,1});
                System.out.println("Network has been set up. Press Enter to continue.");
                scanner.nextLine();
                break;
            case "no":
                return;
            default:
                System.out.println("Invalid choice. Press enter to continue.");
                scanner.nextLine();
        }
    }

    private static void networkPrompt() {
        clearScreen();
        System.out.println("NETWORK CREATION\n");
        System.out.println("Enter the amount of neurons per layer you want.");
        System.out.println("The amount of values entered dictates how many layers there are.");
        System.out.println("Separate counts with commas (i.e. 1,2,3).");
        System.out.print("> ");

        String neuronCounts = scanner.nextLine().trim();
        try {
            neuralNet = createNewNetwork(neuronCounts);
        } catch (NumberFormatException e) {
            System.out.println("Enter whole numbers separated by commas. Press Enter to continue.");
            scanner.nextLine();
            return;
        }

        neuralNet.createLayers();
        System.out.println("Network created. Press Enter to continue.");
        scanner.nextLine();
    }

    private static void networkOptions() {
        while (running) {
            clearScreen();
            System.out.println("NETWORK OPTIONS\n\n");
            System.out.println("1. View weights and biases");
            System.out.println("2. View network info");
            System.out.println("3. Set learning rate");
            System.out.println("4. Set inputs");
            System.out.println("5. Set number of epochs");
            System.out.println("6. Toggle debug (currently " + (neuralNet.debug ? "on" : "off") + ")");
            System.out.println("0. Return to main menu");
            System.out.print("> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    clearScreen();
                    neuralNet.printWeightsAndBiases();
                    System.out.println("\n\nPress Enter to continue.");
                    scanner.nextLine();
                    break;
                
                case "2":
                    clearScreen();
                    neuralNet.printNetworkInfo();
                    System.out.println("\n\nPress Enter to continue.");
                    scanner.nextLine();
                    break;
                case "3":
                    clearScreen();
                    setNewLearningRate();
                    break;
                case "4":
                    clearScreen();
                    setInputs();
                    break;
                case "5":
                    clearScreen();
                    setEpochs();
                    break;
                case "6":
                    clearScreen();
                    toggleDebug();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Press Enter to continue.");
                    scanner.nextLine();

            }
        }   
    }

    private static void setInputs() {
        int expectedCount = neuralNet.neuronsPerLayer[0];
        System.out.println("Enter " + expectedCount + " input values separated by commas (i.e. 0.1,0.2,0.3):");
        System.out.print("> ");
        String[] values = scanner.nextLine().split(",");

        if (values.length != expectedCount) {
            System.out.println("Expected " + expectedCount + " values, but got " + values.length + ".");
        } else {
            try {
                double[] inputs = new double[expectedCount];
                for (int i = 0; i < expectedCount; i++) {
                    inputs[i] = Double.parseDouble(values[i].trim());
                }
                neuralNet.setInputs(inputs);
                System.out.println("Inputs set.");
            } catch (NumberFormatException e) {
                System.out.println("Enter valid numbers separated by commas.");
            }
        }

        System.out.println("Press Enter to continue.");
        scanner.nextLine();
    }

    private static void setNewLearningRate() {
        System.out.println("Enter a new learning rate (i.e. 0.2):");
        System.out.print("> ");
        String input = scanner.nextLine();
        try {
            double newRate = Double.parseDouble(input.trim());
            neuralNet.setLearningRate(newRate);
            System.out.println("Learning rate set to " + newRate);
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }

        System.out.println("Press Enter to continue.");
        scanner.nextLine();
    }

    private static void toggleDebug() {
        neuralNet.setDebug(!neuralNet.debug);
        System.out.println("Debug is now " + (neuralNet.debug ? "on" : "off") + ".");
        System.out.println("Press Enter to continue.");
        scanner.nextLine();
    }

    private static void setEpochs() {
        System.out.println("Enter number of epochs to train for: ");
        System.out.print("> ");
        String input = scanner.nextLine();
        try {
            int newEpochs = Integer.parseInt(input.trim());
            neuralNet.setEpochs(newEpochs);
            System.out.println("Network set to " + newEpochs + " epochs");
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number. Must be an integer.");
        }
        
        System.out.println("Press Enter to continue.");
        scanner.nextLine();
    }
}
