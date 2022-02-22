
import java.util.*;
import java.security.SecureRandom;

public class IXLLearning {

    private static final SecureRandom rand = Utilities.getSecureRandom();
    public static boolean DEBUG = false;
    /*
    Data Structure Design: Insert/Delete/GetRandom in O(1) (constant time)
    * Also do not store duplicate values
    * Note that List removal requires shuffling of the elements to keep the
      List memory contiguous. This means that removing an arbitrary element
      from a list as a O(N) operation, but removing the element at the *end*
      of the List is O(1).
    */
    // static?
    class EfficientDataStorage {
        Map<Integer, Integer> data;
        List<Integer> randomData;

        public EfficientDataStorage() {
            data = new HashMap<>();
            randomData = new ArrayList<>();
            // rand = new SecureRandom();
        }

        public void insertData(Integer i) {
            if (!data.containsKey(i)) {
                randomData.add(i);
                data.put(i, randomData.size() - 1);
            }
        }

        public Integer removeData(Integer i) {
            if (data.containsKey(i)) {
                Integer temp = randomData.remove(randomData.size() - 1);
                randomData.add(data.get(i), temp);
                data.put(temp, data.get(i));
                data.remove(i);
            }
            return null;
        }

        public Integer getRandomElement() {
            int randomIndex = rand.nextInt(randomData.size());
            return randomData.get(randomIndex);
        }
    }

    /*
    NOTE: Originally the problem was stated as starting byte is <= 127 or > 127 but I changed
    the test test to <= 0 or > 0 since a Java byte has a range of -128 to +127.

    Given a list of bytes representing 1 or 2-byte characters:
    * If the starting byte is <= 0 then it is a 1-byte character
    * If the starting byte is > 0 then it is the first byte of a 2-byte character
      - The 2nd byte can be -128 to +127
    Determine whether the last character is one or two bytes.
    */
    public static int lastCharacter(Byte[] listOfCharacters) {
        int index = 0;
        int characterSize = -1;
        while (index < listOfCharacters.length) {
            characterSize = listOfCharacters[index] > 0 ? 2 : 1;
            if (characterSize == 1) {
                if (DEBUG) { System.out.println("1-byte character: " + listOfCharacters[index]); }
                index++;
            }
            else if (index < listOfCharacters.length - 1) {
                if (DEBUG) {
                    System.out.println("2-byte character: " + listOfCharacters[index] + ", "
                            + listOfCharacters[index + 1]);
                }
                index += 2;
            }
            else {
                if (DEBUG) { System.out.println("Malformed character string"); }
                return -1;
            }
        }
        return characterSize;
    }

    private static final int MAX_BYTE_ARRAY_SIZE = 10000;
    private static byte[] randomBytes = new byte[MAX_BYTE_ARRAY_SIZE];
    private static void initializeRandomByteArray() { rand.nextBytes(randomBytes); }
    private static byte getRandomByte() { return randomBytes[rand.nextInt(MAX_BYTE_ARRAY_SIZE)]; }

    private static Byte[] generateRandomCharacterArray(int length) {
        List<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            byte nextByte = getRandomByte();
            // A Java byte has a range of -128 to +127, so adjusting the requirements to fit
            bytes.add(nextByte);
            if (nextByte > 0) {
                // Indicates a 2-byte character
                bytes.add(getRandomByte());
            }
        }
        return bytes.toArray(new Byte[length]);
    }

    // ToDo: Create test object class with character string and expected result
    public static void runLastCharacterTests(Byte[][] testData) {
        for (Byte[] testBytes : testData) {
            System.out.println("BYTE STRING: " + Utilities.arrayToString(testBytes));
            System.out.println("     RESULT: " + lastCharacter(testBytes) + "\n");
        }
    }

    // Negative tests
    static Byte[][] negativeAndEdgeCases = new Byte[][] {
            { -1 }, { 0 }, { 1 }, { }, { -120, -65, 33 }, { 53, -127, 38 }
    };
    public static void main(String[] argv) {
        // DEBUG = true;
        initializeRandomByteArray();
        final int numberOfPositiveTests = 10;
        Byte[][] positiveTests = new Byte[numberOfPositiveTests][];
        for (int i = 0; i < numberOfPositiveTests; i++) {
            positiveTests[i] = generateRandomCharacterArray(rand.nextInt(10));
        }
        runLastCharacterTests(Utilities.combineArrays(positiveTests, negativeAndEdgeCases));
    }
}
