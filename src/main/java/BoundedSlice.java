
/*
 ** This is an exercise from Codility
*/
import java.util.*;
// import java.lang.Math;
// import java.util.stream.*;

public class BoundedSlice {
    static final int MAX_SLICES = 1000000000;

    public static int countBoundedSlices(int K, int[] A) {
        // This solution times out for large data sets (O(N^2))
        int numberOfSlices = A.length;
        for (int i = 0; i < A.length - 1; i++) {
            int min = A[i];
            int max = A[i];
            // for (int j = i + 1; j < A.length && Math.abs(A[i] - A[j]) <= K; j++) {
            for (int j = i + 1; j < A.length; j++) {
                if (numberOfSlices == MAX_SLICES) {
                    break;
                }
                if (A[j] < min) {
                    min = A[j];
                }
                if (A[j] > max) {
                    max = A[j];
                }
                if (max - min <= K) {
                    System.out.println("Incrementing slices for (i, j) = (" + i + ", " + j + ")");
                    numberOfSlices++;
                }
                else {
                    break;
                }
                // This solution times out for large data sets (O(N^3))
/*
                List<Integer> sortedSlice
                        = Arrays.stream(Arrays.stream(A, i, j + 1).toArray()).boxed().sorted().collect(Collectors.toList());
                System.out.println("sortedSlice = " + sortedSlice);
                if (Math.abs(sortedSlice.get(0) - sortedSlice.get(sortedSlice.size() - 1)) <= K) {
                    System.out.println("Incrementing slices for (i, j) = (" + i + ", " + j + ")");
                    numberOfSlices++;
                }
                else {
                    break;
                }
*/
            }
        }
        return numberOfSlices;
    }

    public static void main(String[] args) {
        int[] testData = {3, 5, 7, 6, 3};
        System.out.println("countBoundedSlices(2, " + Arrays.toString(testData) + ") = "
                + countBoundedSlices(2, testData));
    }
}
