package NeuralNetUtils;
import java.util.Scanner;
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
            System.out.println("0. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    networkPrompt();
                    break;
                case "2":
                    networkOptions();
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
            System.out.println("2. Return to main menu");
            System.out.println(">");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("WIP");
                    scanner.nextLine();
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Invalid choice. Press Enter to continue.");
                    scanner.nextLine();

            }
        }   
    }
}
