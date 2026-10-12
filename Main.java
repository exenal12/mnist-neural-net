import java.util.Scanner;
import NeuralNetUtils.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a matrix. Separate values with numbers, and end each row with a semicolon.");

        String unparsedMatrix = scanner.nextLine();
        System.out.println("Unparsed matrix: " + unparsedMatrix);
        NeuralNetUtils.Menu.parseMatrixInput(unparsedMatrix);

        scanner.close();
    }

}