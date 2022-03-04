
import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.security.SecureRandom;
import org.apache.logging.log4j.*;
import org.apache.commons.lang3.ArrayUtils;
import io.restassured.*;
import io.restassured.http.*;
import io.restassured.path.json.*;
import io.restassured.response.Response;

import static java.util.stream.Collectors.toList;

// Helper methods
public class Utilities {
    private static SecureRandom random;
    private static final Logger utilityLogger = LogManager.getLogger(Utilities.class);

    public static Logger getLogger(String loggerName) { return LogManager.getLogger(loggerName); }

    public static <T> String arrayToString(T[] array) { return Arrays.toString(array); }

    // ToDo: Rewrite to take N arrays (use varargs)
    public static <T> T[] combineArrays(T[] a1, T[] a2) { return ArrayUtils.addAll(a1, a2); }

    public static SecureRandom getSecureRandom() {
        if (random == null) {
            random = new SecureRandom();
        }
        return random;
    }

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

    // ToDo: Define base TestData class
    // abstract List<TestData> readTestData(fileName)

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

    public static Response sendToApiEndpoint(
            // Use com.jayway.jsonpath.DocumentContext for reading JSON data from files?
            // Method command, String urlEndpoint, DocumentContext jsonBody, int expectedStatus) {
            Method command, String urlEndpoint, String jsonBody, int expectedStatus) {
        // ToDo: Return a ValidatableResponse object?
        // String requestBody = jsonBody == null ? "{}" : jsonBody.jsonString();
        Response result = RestAssured.given().
                // header("Authorization", ApiUtils.getDefaultAuth()).
                contentType(ContentType.JSON).body(jsonBody).
                request(command, urlEndpoint);
        if (result.statusCode() != expectedStatus) {
            utilityLogger.error("Error in sendToApiEndpoint():");
            utilityLogger.error("Status: " + result.statusCode());
            utilityLogger.error("Method: " + command.name());
            utilityLogger.error("Response: " + result.body().asString());
            // ToDo: Is baseURI set here? Add an API to set it?
            utilityLogger.error("Base URL: " + RestAssured.baseURI);
            utilityLogger.error("Endpoint URL: " + urlEndpoint);
            utilityLogger.error("Request body: " + jsonBody);
            // utilityLogger.error("Request body: " + requestBody);
            throw new RuntimeException("API call failed (see errors, above)");
        }
        return result;
    }
}
