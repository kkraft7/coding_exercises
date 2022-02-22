

import java.util.*;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import java.io.BufferedReader;
import io.restassured.http.*;
import io.restassured.response.Response;
import com.google.gson.*;

public class CodingProblems {

    public static void main(String[] argv) {

        // For Plastiq:
        int[] testValues = new int[] { -1, 0, 1, 2, 11, 57, 100001, Integer.MAX_VALUE,
                Integer.MAX_VALUE/2, Integer.MAX_VALUE/2 + 1, Integer.MAX_VALUE/2 - 1,
                -Integer.MAX_VALUE/2, -Integer.MAX_VALUE/2 - 1
        };
        runTests(testValues);

/*
        // For Treasury Prime (1)
        String inputLine;
        BufferedReader testReader = TestRunner.getStdinReader();
        while (!(inputLine = testReader.readLine().replaceAll("\\s+$", "")).equals("")) {
            List<String> testData = Utilities.parseStringListFromString(inputLine);
            System.out.println("Reverse of " + testData + " = " + reverseArray(testData));
        }
        TestRunner.closeStdinReader();

        // For Treasury Prime (2)
        // totalTransactions(1, "debit");
*/
    }

    // Coding problem for Plastiq
    public static int timesTwo(int n) {
        // Need to explote how the overflow behaves
        long result = (long)n * 2;
        return Math.abs(result) > Integer.MAX_VALUE ? -1 : (int)result;
/*
        try {
            // Note: rather than throwing an error for large numbers the values
            // just wrap around MAX_VALUE if you use an int to store the result
            return n*2;
        }
        catch (Exception ex) {
            System.out.println("Exception in timesTwo() for input value n: " + ex);
        }
        return -1;
*/
    }

    public static int[] runTests(int[] testValues) {
        int[] results = new int[testValues.length];
        for (int i = 0; i < testValues.length; i++) {
            results[i] = timesTwo(testValues[i]);
            System.out.println("timesTwo(" + testValues[i] + ") = " + results[i]);
            if (results[i] == -1) {
                // Exploring overflow value behavior:
                System.out.println("  Overflow value " + testValues[i]*2);
            }
        }
        return results;
    }

    // Treasury Prime
    public static List<String> reverseArray(List<String> data) {
        List<String> result = new ArrayList<>();
        for (String elem : data) {
            result.add(0, elem);
        }
        return result;
    }

    // Used this (instead of the one in Utilities) for copy/paste to solution browser window,
    // since it wouldn't recognise my local close
    public static Response sendToApiEndpoint(
            Method command, String urlEndpoint, String jsonBody, int expectedStatus) {
        return RestAssured.given().contentType(ContentType.JSON).body(jsonBody).request(command, urlEndpoint);
    }

    public static List<List<Integer>> totalTransactions(int locationId, String transactionType) {
        List<List<Integer>> userTransactions = new ArrayList<>();
        Map<Integer, Double> transactionMap = new TreeMap<>();
        String requestUrlBase = "https://jsonmock.hackerrank.com/api/transactions/search?txnType=" +
                transactionType + "&page=";
        Response result = Utilities.sendToApiEndpoint(Method.GET, requestUrlBase + 1, "", 200);
        // System.out.println("Result = " + result.asPrettyString());
        System.out.println("Searching for " + transactionType + " transactions from location ID " + locationId);
        int totalPages = result.jsonPath().getInt("total_pages");
        System.out.println("Total pages = " + totalPages);
        for (int page = 1; page <= totalPages; page++) {
            System.out.println("Processing page " + page + " of " + totalPages);
            result = Utilities.sendToApiEndpoint(Method.GET, requestUrlBase + page, "", 200);
            List<JsonObject> users = result.jsonPath().getList("data", JsonObject.class);
            for (JsonObject json : users) {
                // System.out.println(json.toString());
                String txnType = json.get("txnType").getAsString();
                int locId = json.getAsJsonObject("location").get("id").getAsInt();
                if (txnType.equals(transactionType) && locId == locationId) {
                    Integer userID = json.get("userId").getAsInt();
                    String amount = json.get("amount").getAsString().replace("$", "").replace(",", "");
                    Double decimalAmount = Double.parseDouble(amount);
                    System.out.println("  User ID: " + userID);
                    System.out.println("  Amount = " + decimalAmount);
                    if (!transactionMap.containsKey(userID)) {
                        transactionMap.put(userID, decimalAmount);
                    } else {
                        transactionMap.put(userID, transactionMap.get(userID) + decimalAmount);
                    }

                }
            }
        }
        for (Integer id : transactionMap.keySet()) {
            List<Integer> userData = new ArrayList<>();
            userData.add(0, id);
            userData.add(1, transactionMap.get(id).intValue());
            System.out.println("Amount for user ID " + id + ": " + transactionMap.get(id).intValue());
            userTransactions.add(userData);
        }
        return userTransactions;
    }
}
