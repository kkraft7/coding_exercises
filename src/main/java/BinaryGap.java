
/*
** This is an exercise from Codility
*/
public class BinaryGap {
    public static int maxGap(int N) {
        int maxGap = 0;
        int currentGap = 0;
        boolean found1 = false;
        for (int div = N; div > 0; div /= 2) {
            int remainder = div % 2;
            // System.out.println("Remainder = " + remainder);
            if (remainder == 1) {
                found1 = true;
                if (currentGap > maxGap) {
                    // System.out.println("Setting maxGap to " + currentGap);
                    maxGap = currentGap;
                }
                currentGap = 0;
            }
            else if (found1) {
                currentGap++;
                // System.out.println("currentGap = " + currentGap);
            }
        }
        return maxGap;
    }

    private static String convertToBinary(int N) {
        String binaryValue = "";
        for (int div = N; div > 0; div /= 2) {
            binaryValue = (div % 2) + binaryValue;
        }
        return binaryValue;
    }

    public static void main(String[] args) {
        // int[] testValues = { 0, 2, 9, 20, 21, 529, 561892, 74901729, 1376796946 };
        int[] testValues = { 1376796946 };
        for (int testValue : testValues) {
            System.out.println("convertToBinary(" + testValue + ") = " + convertToBinary(testValue));
            System.out.println("maxGap(" + testValue + ") = " + maxGap(testValue));
        }
    }
}
