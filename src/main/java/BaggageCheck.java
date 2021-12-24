/*
For Guidewire coding test
*/

public class BaggageCheck {
    public static String baggageCheck(int bagCount, int[] weights, int[] limits) {
        int weightTotal = 0;
        int weightLimit = 0;
        for (int i = 0; i < bagCount; i++) {
            weightTotal += weights[i];
            weightLimit += limits[i];
        }
        return weightTotal > weightLimit ? "Yes" : "No";
    }
}
