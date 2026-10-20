package NeuralNetUtils;

public class Linear{
    public static class Vector {

        public static void print(double[] vec) {
            System.out.print("[");
            for (double value : vec) {
                System.out.printf(" %8.5f", value);
            }
            System.out.print(" ]");
        }

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

    public static class Matrix {

        public static void print(double[][] mat) {
            print(mat, "");
        }

        public static void print(double[][] mat, String indent) {
            if (mat.length > 0 && mat[0].length == 1) {
                System.out.print(indent);
                Vector.print(getColumn(mat, 0));
                System.out.println();
                return;
            }
            for (double[] row : mat) {
                System.out.print(indent + "[");
                for (double value : row) {
                    System.out.printf(" %8.5f", value);
                }
                System.out.println(" ]");
            }
        }
    
        // returns the specified column as a 1D array/vector
        public static double[] getColumn(double[][] mat, int colNum){
           double[] resCol = new double[mat.length];
           
           for (int i = 0; i < mat.length; i++){
            resCol[i] = mat[i][colNum];
           }
           
           return resCol;
        }
    
        // sets a column of a matrix
        public static void setColumn(double[][] mat, int colNum, double[] values){
            for (int i = 0; i < mat.length; i++) {
                mat[i][colNum] = values[i];
            }
        }

        public static double[][] scalarMultiply(double[][] mat, double scalar) {
            double[][] res = new double[mat.length][mat[0].length];
            
            for (int i = 0; i < mat.length; i++) {
                for (int j = 0; j < mat[0].length; j++) {
                    res[i][j] = mat[i][j] * scalar;
                }
            }

            return res;
        }

        public static double[][] add(double[][] mat1, double[][] mat2) {
            int rows = mat1.length;
            int columns = mat1[0].length;
            
            // check size
            if (rows == 0 || mat2.length != rows) {
                throw new IllegalArgumentException("Incompatible matrix sizes");
            }

            for (int i = 0; i < rows; i++) {
                if (mat1[i].length != columns || mat2[i].length != columns) {
                    throw new IllegalArgumentException("Incompatible matrix sizes");
                }
            }

            double[][] sum = new double[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    sum[i][j] = mat1[i][j] + mat2[i][j];
                }
            }

            return sum;
        }

        public static double[][] subtract(double[][] mat1, double[][] mat2) {
            int rows = mat1.length;
            int columns = mat1[0].length;
            
            // check size
            if (rows == 0 || mat2.length != rows) {
                throw new IllegalArgumentException("Incompatible matrix sizes");
            }

            for (int i = 0; i < rows; i++) {
                if (mat1[i].length != columns || mat2[i].length != columns) {
                    throw new IllegalArgumentException("Incompatible matrix sizes");
                }
            }

            double[][] sum = new double[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    sum[i][j] = mat1[i][j] - mat2[i][j];
                }
            }

            return sum;
        }
    
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

        public static double[][] transpose(double[][] mat) {
            // create result matrix, swap row and column counts
            double[][] res = new double[mat[0].length][mat.length];

            // grab rows, set columns
            for (int i = 0; i < mat.length; i++) {
                setColumn(res, i, mat[i]);
            }

            return res;
        }

        public static double[][] hadamard(double[][] mat1, double[][] mat2){
            int rows = mat1.length;
            int columns = mat1[0].length;
            
            // check size
            if (rows == 0 || mat2.length != rows) {
                throw new IllegalArgumentException("Incompatible matrix sizes");
            }

            for (int i = 0; i < rows; i++) {
                if (mat1[i].length != columns || mat2[i].length != columns) {
                    throw new IllegalArgumentException("Incompatible matrix sizes");
                }
            }

            // iterate, multiply, and return
            double[][] prod = new double[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    prod[i][j] = mat1[i][j] * mat2[i][j];
                }
            }

            return prod;
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