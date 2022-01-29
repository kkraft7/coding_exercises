
import java.io.*;
import java.util.*;

public abstract class TestRunner<T> {
    // Got "IOException: Stream closed" attempting to open and close the BufferedReader between tests.
    private static BufferedReader reader;

    public static BufferedReader getStdinReader() {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        return reader;
    }

    public static void closeStdinReader() {
        try {
            reader.close();
        }
        catch (NullPointerException ex) {
            System.out.println("Reader is uninitialized - cannot close");
        }
        catch (IOException ex) {
            System.out.println("Exception closing reader: " + ex);
        }
    }

    abstract List<T> parseNextLine(String inputLine);

    abstract int runTest(List<T> testData);

    // ToDo: Pass test-specific runner function instead of defining runTest()?
    public void runTests(BufferedReader testReader) throws IOException {
        System.out.println("Running tests for: " + this.getClass().getSimpleName());
        // boolean notDone = true;
        String inputLine;

        // ToDo: Write prompt: "Enter empty line to quit"
        // ToDo: Or loop while line has correct format
        while (!(inputLine = testReader.readLine().replaceAll("\\s+$", "")).equals("")) {
            // System.out.println("Input line: " + inputLine + "\n");
            List<T> testData = parseNextLine(inputLine);
            // System.out.println("Size of input list: " + ar.size());
            int result = runTest(testData);
            System.out.println("Result = " + result);
        }
        System.out.println("Exiting test: " + this.getClass().getSimpleName());
    }
}
