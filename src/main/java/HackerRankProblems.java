
import java.io.*;
import java.util.*;

public class HackerRankProblems {

    /*
    There is a large pile of socks that must be paired by color. Given an array of integers representing the color of
    each sock, determine how many pairs of socks with matching colors there are.
    Example:
    n == 7
    ar = 1 2 1 2 1 3 2

    Constraints:
    1 <= n <= 100
    1 <= ar[i] <= 100
    */
    static class SockMerchant extends TestRunner<Integer> {

        List<Integer> parseNextLine(String nextLine) {
            return Utilities.parseIntegerListFromString(nextLine);
        }

        int runTest(List<Integer> colors) {
            int[] sockColors = new int[100];
            int numberOfPairs = 0;
            for (Integer color : colors) {
                sockColors[color - 1]++;
                if (sockColors[color - 1] % 2 == 0) {
                    numberOfPairs++;
                }
            }
            return numberOfPairs;
        }
    }

    /*
    An avid hiker keeps meticulous records of their hikes. During the last hike that took exactly  steps,
    for every step it was noted if it was an uphill, , or a downhill,  step. Hikes always start and end
    at sea level, and each step up or down represents a  unit change in altitude. We define the following terms:
    * A mountain is a sequence of consecutive steps above sea level, starting with a step up from sea level
      and ending with a step down to sea level.
    * A valley is a sequence of consecutive steps below sea level, starting with a step down from sea level
      and ending with a step up to sea level.
     Given the sequence of up and down steps during a hike, find and print the number of valleys walked through.

    Examples:
    D D U U U U D D => 1 valley
    U D D D U D U U U D => 1 valley
    D D U U U U D D D U => 2 valleys
    D U D U D U => 3 valleys
    */
    public static int countingValleys(String path) {
        int level = 0;
        int numberOfValleys = 0;
        for (int i = 0; i < path.length(); i++) {
            int step = path.charAt(i) == 'D' ? -1 : 1;
            if (level == 0 && step == -1) {
                numberOfValleys++;
            }
            level += step;
        }
        return numberOfValleys;
    }

    static class CountingValleys extends TestRunner<String> {

        List<String> parseNextLine(String nextLine) {
            return Utilities.parseStringListFromString(nextLine);
        }

        int runTest(List<String> hikingPath) {
            return countingValleys(String.join("", hikingPath));
        }
    }

    /*
    There is a new mobile game that starts with consecutively numbered clouds. Some of the clouds are
    thunderheads and others are cumulus. The player can jump on any cumulus cloud having a number
    that is equal to the number of the current cloud plus  or . The player must avoid the thunderheads.
    Determine the minimum number of jumps it will take to jump from the starting postion to the last cloud.
    It is always possible to win the game.

    For each game, you will get an array of clouds numbered 0 if they are safe or 1 if they must be avoided.

    Examples:
    0 1 0 0 0 1 0 => 3
    0 0 1 0 0 1 0 => 4
    */
    static class CloudJumps extends TestRunner<Integer> {

        List<Integer> parseNextLine(String nextLine) {
            return Utilities.parseIntegerListFromString(nextLine);
        }

        int runTest(List<Integer> clouds) {
            int jumps = 0;
            int cloud = 0;
            while (cloud < clouds.size() - 1) {
                if (cloud + 2 < clouds.size() && clouds.get(cloud + 2) == 0) {
                    cloud += 2;
                }
                else {
                    cloud += 1;
                }
                jumps++;
            }
            return jumps;
        }
    }

    public static void main(String[] argv) throws Exception {
        BufferedReader testReader = TestRunner.getStdinReader();
        TestRunner<?>[] hackerRankTests = new TestRunner[] {
                new SockMerchant(), new CountingValleys(), new CloudJumps()
        };
        for (TestRunner<?> test : hackerRankTests) {
            test.runTests(testReader);
        }
        TestRunner.closeStdinReader();
        // testReader.close();
    }
}

