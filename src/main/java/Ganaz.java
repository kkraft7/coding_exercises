
public class Ganaz {
    static int missingInt(int[] intList) {
        // Has to be at least size 2!
        int lastInt = intList[0];
        for (int i = 1; i < intList.length; i++) {
            if (intList[i] == lastInt + 2) {
                return lastInt + 1;
            }
            lastInt = intList[i];
        }
        return -1;
    }

    // Could O(log(N))
    // Binary search (keep splitting problem in half)
    static int missingInt2(int[] intList) {
        return -1;
    }

    public static void main(String argv[]) {
        int[] testInput = new int[] { 1, 2, 3, 4, 6, 7 };
        System.out.println("Missing number is " + missingInt(testInput));
        testInput = new int[] { 1, 3 };
        System.out.println("Missing number is " + missingInt(testInput));
        testInput = new int[] { 2 };
        System.out.println("Missing number is " + missingInt(testInput));
    }
}
