
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

// Helper methods
public class Utilities {
    // Open a file under the resources folder
    public static Scanner getFileScanner(String fileName) {
        try {
            return new Scanner(new File(Scanner.class.getResource(fileName).toURI()));
        }
        catch (Exception ex) {
            System.out.println("Error attempting to open file " + fileName);
        }
        return null;    // Why is return value required here?
    }

    // Try and combine all these in a switch/case statement?
    // public static <T> List<T> parseListFromString(String lineToParse) {
    public static List<Integer> parseIntegerListFromString(String lineToParse, String delimiterPattern) {
        return Stream.of(lineToParse.split(delimiterPattern)).map(Integer::parseInt).collect(toList());
    }

    public static List<String> parseStringListFromString(String lineToParse) {
        return parseStringListFromString(lineToParse, "\\s+");
    }

    public static List<String> parseStringListFromString(String lineToParse, String delimiterPattern) {
        return Stream.of(lineToParse.split(delimiterPattern)).collect(toList());
    }

    public static List<Integer> parseIntegerListFromString(String lineToParse) {
        return parseIntegerListFromString(lineToParse, "\\s+");
    }

    public static String[] parseStringArrayFromString(String lineToParse, String delimiterPattern) {
        return Stream.of(lineToParse.split(delimiterPattern)).toArray(String[]::new);
    }

    public static String[] parseStringArrayFromString(String lineToParse) {
        return parseStringArrayFromString(lineToParse, "\\s+");
    }
}
