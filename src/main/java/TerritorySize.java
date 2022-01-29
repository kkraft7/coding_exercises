import java.io.*;
import java.net.URL;
import java.util.*;

/*
For Guidewire CASE (services) team coding test.
ToDo: Is this still true? Represents a matrix of size NxN as an array of size N^2
*/
public class TerritorySize {
    // Try recursion starting at 0 instead of looping
    public static int calculateLargestTerritory(TerritoryData data) {
        int largestTerritory = 0;
        // ToDo: Shouldn't need to loop for the recursive solution?
        for (int i = 0; i < data.matrix.length; i++) {
            for (int j = 0; j < data.matrix[i].length; j++) {
                int territory = calculateTerritoryRecursive(data.matrix[i][j], i, j, data.matrix);
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
    public static int calculateTerritoryRecursive(int value, int i, int j, int [][] matrix) {
        if (i >= 0 && i < matrix.length && j >= 0 && j < matrix.length && value == matrix[i][j]) {
            // Looks like this is double (or triple etc.) counting
            return 1
                    + calculateTerritoryRecursive(value, i + 1, j, matrix)
                    // + calculateTerritory(value, i - 1, j, matrix)
                    + calculateTerritoryRecursive(value, i, j + 1, matrix)
                    // + calculateTerritory(value, i + 1, j + 1, matrix);
                    // Adding this line causes a StackOverflowError
                    + calculateTerritoryRecursive(value, i, j - 1, matrix);
        }
        else {
            // Looks like this version never gets here
            return 0;
        }
    }

    static class TerritoryData {  // Test data class
        final int value;
        final int maxSize;
        final int[][] matrix;
        private static final URL inputFilePath = TerritoryData.class.getResource("territoryData.txt");

        public TerritoryData(int value, int maxSize, int[][] matrix) {
            this.value = value;
            this.maxSize = maxSize;
            this.matrix = matrix;
        }

        public int getValue() { return value; }
        public int getMaxSize() { return maxSize; }
        public int[][] getMatrix() { return matrix; }

        // ToDo: Pass data file?
        public static List<TerritoryData> readTerritoryData() {
            List<TerritoryData> dataList = new ArrayList<>();
            try {
                // System.out.println("Input file path is " + inputFilePath);
                Scanner fileReader = new Scanner(new File(inputFilePath.toURI()));
                // ToDo: Test whether this can read multiple data sets
                while (fileReader.hasNextLine()) {
                    if (!fileReader.hasNext("\\d+")) {
                        fileReader.nextLine();  // Skip comments, blank lines, etc.
                        continue;
                    }
                    // This next section defines the expected input format
                    // ToDo: Check for valid input format?
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

    static class Territory {
        static int ID = 1;   // Mainly for debugging purposes
        Integer value;
        int size;
        String name;  // Mainly added for debugging
        HashMap<Integer, TreeSet<Integer>> indexes;

        public Territory(int newValue, int i, int j) {
            value = newValue;
            indexes = new HashMap<Integer, TreeSet<Integer>>();
            name = "T" + value + "_" + ID++;
            addIndex(i, j);
        }

        public void addIndex(int i, int j) {
            if (!indexes.containsKey(i)) {
                indexes.put(i, new TreeSet<Integer>());
            }
            // ToDo: Check that it doesn't already exist?
            indexes.get(i).add(j);
            size++;
            // System.out.println(name + ": Size set to " + size);
        }

        public int getSize() { return size; }
        public int getValue() { return value; }
        public String getName() { return name; }
        public Set<Map.Entry<Integer, TreeSet<Integer>>> getEntries() { return indexes.entrySet(); }
        public TreeSet<Integer> getRow(int i) { return indexes.get(i); }

        public boolean contains(int i, int j) {
            return indexes != null && indexes.containsKey(i) && indexes.get(i).contains(j);
        }

        // ToDo: Only ever going to be adding the current row to a territory for the previous row (can simplify?)
        // public void addIndexes(int row, int[] columns)
        // public void addIndexes(Territory t)
        public void mergeWith(Territory t) {
            // ToDo: Null check for t
            if (t.getValue() != value) {
                System.out.println("WARNING MERGING TERRITORIES WITH DIFFERENT VALUES!");
                System.out.println("  Territory " + t.getName() + " has value " + t.getValue());
                System.out.println("  Territory " + name + " has value " + value);
            }
            for (Map.Entry<Integer, TreeSet<Integer>> row : t.getEntries()) {
                for (Integer col : row.getValue()) {
                    // ToDo: Check for existence first?
                    addIndex(row.getKey(), col);
                }
            }
        }
    }

    public static Territory calculateTerritory(TerritoryData data) {
        Territory maxT = null;
        // Use this in recursion to mark checked indexes?
        HashMap<String, Territory> indexToTerritory = new HashMap<>();
        Scanner reader = new Scanner(System.in); // ToDo: Move to utility class (pauseForStdin())

        for (int i = 0; i < data.matrix.length; i++) {
            for (int j = 0; j < data.matrix[i].length; j++) {
                // Rename to prevRow, prevCol? lastRow, lastCol?
                Territory newTerritory = null, tRow = indexToTerritory.get((i - 1) + "," + j),
                        tCol = indexToTerritory.get(i + "," + (j - 1));
                if (tCol != null && tCol.getValue() == data.matrix[i][j]) {
                    System.out.println("Adding column index at (" + i + "," + j + ") for value " + tCol.getValue());
                    tCol.addIndex(i, j);
                    newTerritory = tCol;
                }
                if (tRow != null && tRow.getValue() == data.matrix[i][j]) {
                    if (newTerritory == null) {
                        tRow.addIndex(i, j);
                    }
                    // This means that value at (i,j) is part of larger column territory which hasn't
                    // already been merged with the row territory above it
                    else if (tRow != tCol) {
                        System.out.println("Merging territory at index (" + i + "," + j + ") for value " + tRow.getValue());
                        tRow.mergeWith(newTerritory);

                        // Update territory map
                        if (tCol.getValue() == data.matrix[i][j]) {
                            for (int c : tCol.getRow(i)) {
                                System.out.println("Adding entry for territory " + tRow.getName() + " at index (" + i + "," + c + ")");
                                indexToTerritory.put(i + "," + c, tRow);
                            }
                        }
                    }
                    newTerritory = tRow;
                }
                if (newTerritory == null) {
                    newTerritory = new Territory(data.matrix[i][j], i, j);
                }
                if (maxT == null || newTerritory.getSize() > maxT.getSize()) {
                    System.out.println("Setting max size to " + newTerritory.getSize());
                    maxT = newTerritory;
                }
                indexToTerritory.put(i + "," + j, newTerritory);
                // reader.nextLine();  // For debugging
            }
        }
        return maxT;
    }

    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3},
                           {1, 1, 3},
                           {1, 1, 1}};
        // calculateLargestTerritory(new TerritoryData(1, 6, matrix1));
        // calculateLargestTerritory(matrix1);
        // ToDo: Update this to read multiple data sets
        List<TerritoryData> territoryDataList = TerritoryData.readTerritoryData();
        for (TerritoryData data : territoryDataList) {
            System.out.println(data);
            Territory largestTerritory = calculateTerritory(data);
            if (largestTerritory != null) {
                System.out.println("Maximum territory is " + largestTerritory.getSize() + " for value "
                        + largestTerritory.getValue());
            }
        }
    }
}
