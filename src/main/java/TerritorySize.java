import java.io.*;
import java.net.URL;
import java.util.*;
import static org.junit.Assert.*;

/*
** For the Guidewire CASE (services) team coding challenge.
**
** Given an NxM matrix of integers (from 0-9 in this case), a territory
** is a group of idenitcal integer values, connected vertically or horizontally
** (but not diagonally). Return the maximum territory size.
*/
public class TerritorySize {

    // Position of index/square relative to current index
    public enum Position {
        LAST_ROW, LAST_COL, NEXT_ROW, NEXT_COL;

        public String getLookupIndex(int i, int j) {
            switch (this) {
                case LAST_COL: return i + "," + (j - 1);
                case NEXT_COL: return i + "," + (j + 1);
                case LAST_ROW: return (i - 1) + "," + j;
                case NEXT_ROW: return (i + 1) + "," + j;
            }
            return "";
        }
    }

    static class TerritoryData {  // Test data class
        private final int value;
        private final int maxSize;
        private final int[][] matrix;
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
                // ToDo: Move to Utility class?
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
        static int ID = 1;  // Mainly added for debugging
        String name;        // Mainly added for debugging
        Integer value;
        int size;
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

        public void addIndexes(int row, Set<Integer> columns) {
            for (int col : columns) {
                addIndex(row, col);
            }
        }

        // ToDo: Only ever going to be adding the current row to a territory for the previous row (can simplify?)
        public void mergeWith(Territory t) {
            if (t == null) {
                System.out.println("ERROR: ATTEMPTING TO MERGE WITH NULL TERRITORY!");
            }
            else if (t.getValue() != value) {
                System.out.println("ERROR: CANNOT MERGE TERRITORIES WITH DIFFERENT VALUES!");
                System.out.println("  Territory " + t.getName() + " has value " + t.getValue());
                System.out.println("  Territory " + name + " has value " + value);
                return;
            }
            for (Map.Entry<Integer, TreeSet<Integer>> row : t.getEntries()) {
                for (Integer col : row.getValue()) {
                    // ToDo: Check for existence first?
                    addIndex(row.getKey(), col);
                }
            }
        }
    }

    static class TerritoryMap {
        final int[][] territoryMatrix;
        Territory tMax;    // Largest territory found
        // ToDo: Can this be a Territory matrix?
        Map<String, Territory> territoryLookup;   // Maps a matrix index to a Territory

        public TerritoryMap(int[][] matrix) { this.territoryMatrix = matrix; }

        // Check the Territory at index (i, j) to see if there is a matching value at an adjacent index
        Territory checkAdjacentTerritory(int i, int j, Position p) {
            Territory t = territoryLookup.get(p.getLookupIndex(i, j));
            return t != null && t.getValue() == territoryMatrix[i][j] ? t : null;
        }

        /*
        ** This is the heart of the algorithm. Check the previous row and the previous column
        ** to see if there is a matching territory.
        */
        Territory addToTerritory(int i, int j) {
            // ToDo: Change name to previousCol? lastRow, lastColumn?
            Territory previousRow = checkAdjacentTerritory(i, j, Position.LAST_ROW),
                    previousColumn = checkAdjacentTerritory(i, j, Position.LAST_COL);
            if (previousColumn != null) {
                previousColumn.addIndex(i, j);
            }
            if (previousRow != null) {
                if (previousColumn == null) {
                    previousRow.addIndex(i, j);
                }
                else if (previousRow != previousColumn) {
                    System.out.println("Merging territory at index (" + i + "," + j + ") for value "
                            + previousRow.getValue());
                    previousRow.addIndexes(i, previousColumn.getRow(i));
                    // Update territory lookup map
                    for (int n : previousColumn.getRow(i)) {
                        territoryLookup.put(i + "," + n, previousRow);
                    }
                }
            }
            // Order matters here - must test previousRow first
            Territory territoryToAdd = previousRow != null ? previousRow :
                    previousColumn != null ? previousColumn : new Territory(territoryMatrix[i][j], i, j);
            territoryLookup.put(i + "," + j, territoryToAdd);
            return territoryToAdd;
        }

        void updateMaxTerritory(Territory t) {
            if (t == tMax) {
                System.out.println(t.getName() + ": Updated max territory size to " + t.getSize()
                        + " for value " + t.getValue());
            }
            else if (tMax == null || tMax.getSize() < t.getSize()) {
                System.out.println(t.getName() + ": Setting max territory size to " + t.getSize()
                        + " for value " + t.getValue());
                tMax = t;
            }
        }

        void resetData() {
            territoryLookup = new HashMap<>();
            tMax = null;
        }

        public Territory calculateTerritoryRecursive() {
            resetData();
            calculateTerritoryRecursive(0, 0);
            return tMax;
        }

        // At least in this case recursion is just a more complicated way of looping
        private void calculateTerritoryRecursive(int i, int j) {
            Territory territoryToAdd = addToTerritory(i, j);
            updateMaxTerritory(territoryToAdd);
            // This essentially turns the recursion into a "loop"
            if (j != territoryMatrix[0].length - 1) {
                calculateTerritoryRecursive(i , j + 1);
            }
            else if (i != territoryMatrix.length - 1) {
                calculateTerritoryRecursive(i + 1, 0);
            }
        }

        public Territory calculateTerritory() {
            resetData();
            for (int i = 0; i < territoryMatrix.length; i++) {
                for (int j = 0; j < territoryMatrix[i].length; j++) {
                    Territory territoryToAdd = addToTerritory(i, j);
                    updateMaxTerritory(territoryToAdd);
                }
            }
            return tMax;
        }
    }

    public static void checkResult(Territory largestTerritory, TerritoryData data) {
        if (largestTerritory != null) {
            System.out.println("Maximum territory is " + largestTerritory.getSize() + " for value "
                    + largestTerritory.getValue());
            assertEquals("Largest territory size", data.getMaxSize(), largestTerritory.getSize());
            assertEquals("Largest territory value", data.getValue(), largestTerritory.getValue());
        }
    }

    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3},
                           {1, 1, 3},
                           {1, 1, 1}};
        // ToDo: Update this to read multiple data sets
        List<TerritoryData> territoryDataList = TerritoryData.readTerritoryData();
        territoryDataList.add(new TerritoryData(1, 6, matrix1));
        for (TerritoryData data : territoryDataList) {
            TerritoryMap testDriver = new TerritoryMap(data.getMatrix());
            // System.out.println(data);

            System.out.println("\nDoing non-recursive test");
            Territory largestTerritory = testDriver.calculateTerritory();
            checkResult(largestTerritory, data);

            System.out.println("\nDoing recursive test");
            largestTerritory = testDriver.calculateTerritoryRecursive();
            checkResult(largestTerritory, data);
        }
    }
}
