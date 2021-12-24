import java.io.*;
import java.net.URL;
import java.util.*;

/*
For Guidewire coding test

Represents a matrix of size NxN as an array of size N^2
*/
public class TerritorySize {
    public static int calculateLargestTerritory(TerritoryData data) {
        int largestTerritory = 0;
        for (int i = 0; i < data.matrix.length; i++) {
            for (int j = 0; j < data.matrix[i].length; j++) {
                int territory = calculateTerritory(data.matrix[i][j], i, j, data.matrix);
                if (territory > largestTerritory) {
                    largestTerritory = territory;
                    System.out.println("Setting largest territory to " + largestTerritory + " for value "
                            + data.matrix[i][j]);
                }
            }
        }
        return largestTerritory;
    }

/*
    public static int calculateLargestTerritory(int[][] matrix) {
        // int [][] matrix = vectorToMatrix(A);
        int largestTerritory = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int territory = calculateTerritory(matrix[i][j], i, j, matrix);
                if (territory > largestTerritory) {
                    largestTerritory = territory;
                    System.out.println("Setting largest territory to " + largestTerritory + " for value " + matrix[i][j]);
                }
            }
        }
        return largestTerritory;
    }
*/

    // Use memoization (or similar) to skip squares that have already been seen
    public static int calculateTerritory(int value, int i, int j, int [][] matrix) {
        if (i >= 0 && i < matrix.length && j >= 0 && j < matrix.length && value == matrix[i][j]) {
            // Looks like this is double (or triple etc.) counting
            return 1
                    + calculateTerritory(value, i + 1, j, matrix)
                    // + calculateTerritory(value, i - 1, j, matrix)
                    + calculateTerritory(value, i, j + 1, matrix)
                    // + calculateTerritory(value, i + 1, j + 1, matrix);
                    // Adding this line causes a StackOverflowError
                    + calculateTerritory(value, i, j - 1, matrix);
        }
        else {
            // Looks like this version never gets here
            return 0;
        }
    }

    static class TerritoryData {
        final int value;
        final int maxSize;
        final int[][] matrix;
        private boolean[][] checked;
        private static final URL inputFilePath = TerritoryData.class.getResource("territoryData.txt");
        // private static final String inputFileName = "territoryData.txt";
        private static final File inputFile = new File("territoryData.txt");
        // private static Scanner fileReader;

        public TerritoryData(int value, int maxSize, int[][] matrix) {
            this.value = value;
            this.maxSize = maxSize;
            this.matrix = matrix;
            this.checked = new boolean[matrix.length][matrix.length];
        }

        public int getValue() { return value; }
        public int getMaxSize() { return maxSize; }
        public int[][] getMatrix() { return matrix; }
        public void setChecked(int i, int j) { checked[i][j] = true; }
        // The default boolean value is false
        public boolean isChecked(int i, int j) { return checked[i][j]; }

        // ToDo: Pass data file?
        // ToDo: Check for valid input format?
        public static List<TerritoryData> readTerritoryData() {
            List<TerritoryData> dataList = new ArrayList<>();
            try {
                // System.out.println("Input file path is " + inputFilePath);
                Scanner fileReader = new Scanner(new File(inputFilePath.toURI()));
                while (fileReader.hasNextLine()) {
                    if (!fileReader.hasNext("\\d+")) {
                        fileReader.nextLine();  // Skip comments, blank lines, etc.
                        continue;
                    }
                    int value = fileReader.nextInt();
                    int size = fileReader.nextInt();
                    int rows = fileReader.nextInt();
                    int columns = fileReader.nextInt();
                    int[][] matrix = new int[rows][columns];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            matrix[i][j] = fileReader.nextInt();
                        }
                    }
                    dataList.add(new TerritoryData(value, size, matrix));
                }
            }
            catch (Exception ex) {
                // ToDo: Describe valid input format?
                System.out.println("EXCEPTION SCANNING INPUT FILE: " + ex.getMessage());
            }
            return dataList;
        }

        public String toString() {
            return "\nTerritory value: " + value + "\n"
                    + "Territory size : " + maxSize + "\n"
                    // Convert one-line matrix string to display one line per row
                    + "Matrix:\n" + String.join("\n ", Arrays.deepToString(matrix).split("], "));
        }
    }

    // ToDo: Add code to read TerritoryData from an external file
    // ToDo: Save (memoize?) squares we've already looked at
    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3},
                           {1, 1, 3},
                           {1, 1, 1}};
        calculateLargestTerritory(new TerritoryData(1, 6, matrix1));
        // calculateLargestTerritory(matrix1);
        List<TerritoryData> territoryDataList = TerritoryData.readTerritoryData();
        for (TerritoryData data : territoryDataList) {
            System.out.println(data);
        }
    }
}
