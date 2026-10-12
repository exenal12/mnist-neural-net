package NeuralNetUtils;

public class Menu {
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
}
