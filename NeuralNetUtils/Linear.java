package NeuralNetUtils;
import java.util.Arrays;


/**
 * Vector and matrix arithmetic.
 *
 * @author Alyx
 */
public class Linear{
    /**
     * Operations on vectors.
     *
     * @author Alyx
     */
    public static class Vector {
        /**
         * Returns the dot product of two vectors of equal length
         *
         * @param vec1 first vector
         * @param vec2 second vector
         * @return the result of the dot product, as a {@code double}
         * @throws IllegalArgumentException if the vectors have different lengths
         */
        public static double dotProduct(double[] vec1, double[] vec2) {
            // check size
            if (vec1.length != vec2.length) {
                throw new IllegalArgumentException("Vectors must be the same length");
            }
    
            double sum = 0;
    
            for (int i=0; i < vec1.length; i++) {
                sum += vec1[i] * vec2[i];
            }
            return sum;
        }
    }

    /**
     * Operations on matrices.
     *
     * @author Alyx
     */
    public static class Matrix {
        /**
         * Prints each row of a matrix on its own line
         *
         * @param mat matrix to print
         */
        public static void print(double[][] mat) {
            for (double[] row : mat){
                System.out.println(Arrays.toString(row));
            }
        }
    
        /**
         * Copies one column of a matrix into a new vector
         *
         * @param mat matrix to read
         * @param colNum zero-based column index
         * @return specified column, from top to bottom
         */
        public static double[] getColumn(double[][] mat, int colNum){
           double[] resCol = new double[mat[colNum].length];
           
           for (int i = 0; i < mat[colNum].length; i++){
            resCol[i] = mat[i][colNum];
           }
           
           return resCol;
        }
    
    
    
        /**
         * Multiplies two matrices. {@code mat1} must have as many columns as {@code mat2} has rows
         *
         * @param mat1 left matrix
         * @param mat2 right matrix
         * @return the product matrix
         * @throws IllegalArgumentException if the matrices have incompatible sizes
         */
        public static double[][] multiply(double[][] mat1, double[][] mat2) {
            // check size
            if (mat1[0].length != mat2.length) {
                throw new IllegalArgumentException("Incompatible matrix sizes");
            }
    
            double[][] res = new double[mat1.length][mat2[0].length];
    
            for (int i = 0; i < mat1.length; i++) {
                for (int j = 0; j < mat2[0].length; j++) {
                    res[i][j] = Vector.dotProduct(mat1[i], getColumn(mat2, j));
                }
            }
    
            return res;
        }
    }


    public static void main(String[] args) {
        double[] arr1 = {1, 3};
        double[] arr2 = {1, 3};

        double[][] mat1 = { {1, 2}, {1, 2} };
        double[][] mat2 = { {2, 1}, {2, 1} };

        double[][] prod = Linear.Matrix.multiply(mat1, mat2);
        System.out.println(Linear.Vector.dotProduct(arr1, arr2));
        Linear.Matrix.print(prod);
    }

}