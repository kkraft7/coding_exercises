
public class LeetcodeProblems {

    /*
    2. Add Two Numbers:
    You are given two non-empty linked lists representing two non-negative integers.
    The digits are stored in reverse order, and each of their nodes contains a single digit
    (0 <= digit <= 9). Add the two numbers and return the sum as a linked list in the
    same format. You may assume the two numbers do not contain any leading zero (except
    the number 0 itself). The lists can be different sizes.

    My solutions differ in how they handle unequal-sized lists:
    1. Add zero-nodes to the shorter list
    2. Add zero values for null list nodes
    3. Short-circuit the calculation and compute remaining values with carry
    */

    // Linked list helper class
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
        // Return the String representing this number
        public String toNumber() {
            String value = "";
            for (ListNode ln = this; ln != null; ln = ln.next) {
                value = ln.val + value;
            }
            return value;
        }
        // Return the String representing this linked list
        public String toString() {
            String value = "";
            for (ListNode ln = this; ln != null; ln = ln.next) {
                value += (ln.val + (ln.next != null ? ", " : ""));
            }
            return value;
        }
    }

    // listArray contains the digits of a number in reverse order
    public static ListNode convertArrayToLinkedList(int[] listArray) {
        ListNode head = new ListNode(), listPtr = head;
        for (int i = 0; i < listArray.length - 1;  i++, listPtr = listPtr.next) {
            listPtr.val = listArray[i];
            listPtr.next = new ListNode();
        }
        listPtr.val = listArray[listArray.length - 1];
        return head;
    }

    // If one linked list is shorter add zero nodes at the end
    public static ListNode addTwoNumbersUseZeroNodes(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        ListNode head = result;
        int carry = 0;
        for (ListNode ptr1 = l1, ptr2 = l2; ptr1 != null || ptr2 != null; ptr1 = ptr1.next, ptr2 = ptr2.next) {

            ptr1 = (ptr1 == null ? new ListNode(0) : ptr1);
            ptr2 = (ptr2 == null ? new ListNode(0) : ptr2);

            result.val = (ptr1.val + ptr2.val + carry) % 10;
            carry = (ptr1.val + ptr2.val + carry) / 10;

            if (ptr1.next != null || ptr2.next != null) {
                result.next = new ListNode();
                result = result.next;
            }
        }
        result.next = (carry == 1 ? new ListNode(1) : null);
        return head;
    }

    // If one linked list is shorter substitute zero values
    public static ListNode addTwoNumbersUseZeroValues(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        ListNode head = result;
        int carry = 0;
        for (ListNode ptr1 = l1, ptr2 = l2; ptr1 != null || ptr2 != null;) {
            int val1 = (ptr1 != null ? ptr1.val : 0);
            int val2 = (ptr2 != null ? ptr2.val : 0);

            result.val = (val1 + val2 + carry) % 10;
            carry = (val1 + val2 + carry) / 10;

            ptr1 = (ptr1 != null ? ptr1.next : null);
            ptr2 = (ptr2 != null ? ptr2.next : null);

            if (ptr1 != null || ptr2 != null) {
                result.next = new ListNode();
                result = result.next;
            }
        }
        result.next = (carry == 1 ? new ListNode(1) : null);
        return head;
    }

    // If one linked list is shorter traverse the end of the longer one
    public static ListNode addTwoNumbersShortCircuit(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        ListNode head = result;
        int carry = 0;
        for (ListNode ptr1 = l1, ptr2 = l2; ptr1 != null || ptr2 != null; ptr1 = ptr1.next, ptr2 = ptr2.next) {
            // If one list is empty traverse the end of the other one until the carry value is zero
            if (ptr1 == null || ptr2 == null) {
                // ToDo: I am altering the value the of the non-empty list
                result.next = (ptr1 == null ? ptr2 : ptr1);
                while (result.next != null) {
                    int newValue = result.next.val + carry;
                    result.next.val = newValue % 10;
                    carry = newValue / 10;
                    result = result.next;
                    if (carry == 0) {
                        break;
                    }
                    // System.out.println("Head after: " + head.toString());
                }
                break;
            }

            result.val = (ptr1.val + ptr2.val + carry) % 10;
            carry = (ptr1.val + ptr2.val + carry) / 10;
            if (ptr1.next != null && ptr2.next != null) {
                result.next = new ListNode();
                result = result.next;
            }
        }
        result.next = (carry == 1 ? new ListNode(1) : null);
        return head;
    }

    public enum AddAlgorithm { ADD_ZERO_NODES, ADD_ZERO_VALUES, SHORT_CIRCUIT }
    public static String computeAnswerUsingAlgorithm(ListNode list1, ListNode list2, AddAlgorithm algorithm)
            throws Exception {
        switch (algorithm) {
            case ADD_ZERO_NODES:  return addTwoNumbersUseZeroNodes(list1, list2).toNumber();
            case ADD_ZERO_VALUES: return addTwoNumbersUseZeroValues(list1, list2).toNumber();
            case SHORT_CIRCUIT:   return addTwoNumbersShortCircuit(list1, list2).toNumber();
            default: throw new Exception("Unhandled add algorithm value: " + algorithm);
        }
    }

    public static void runTest(int[] l1, int[] l2, String expectedValue, AddAlgorithm algorithm) throws Exception {
        ListNode list1 = convertArrayToLinkedList(l1);
        ListNode list2 = convertArrayToLinkedList(l2);

        String answer = computeAnswerUsingAlgorithm(list1, list2, algorithm);
        // ToDo: Convert to an assertion
        if (answer.equals(expectedValue)) {
            System.out.println(list1.toNumber() + " + " + list2.toNumber() + " = " + answer);
        }
        else {
            System.out.println("TEST FAILED: Expected " + expectedValue + " but got " + answer);
        }
    }

    /*
    3. Longest Substring Without Repeating Characters
    Given a string s, find the length of the longest substring without repeating characters.
    Examples: abcabcbb => 3, bbbbb => 1, pwwkew => 3
    */
    public static int lengthOfLongestSubstring(String s) {
        int startIndex = 0;
        int endIndex = 0;
        int longestLength = 0;
        while (endIndex < s.length()) {
            int duplicateIndex = s.lastIndexOf(s.substring(startIndex, endIndex), s.charAt(endIndex));
            if (duplicateIndex == -1) {
                endIndex++;
            }
            else if (endIndex - startIndex + 1 > longestLength) {
                longestLength = endIndex - startIndex + 1;
                startIndex = endIndex = duplicateIndex + 1;
            }
        }
        return longestLength;
    }

    // ToDo: Check in updates to GitHub
    public static void main(String[] argv) throws Exception {

        // 2. Add Two Numbers
        // ToDo: define a test data object?
        for (AddAlgorithm algorithm : AddAlgorithm.values()) {
            System.out.println("Running tests using algorithm " + algorithm);
            runTest(new int[]{2, 4, 3}, new int[]{5, 6, 4}, "807", algorithm);
            runTest(new int[]{5, 6, 7}, new int[]{6, 4, 5}, "1311", algorithm);
            runTest(new int[]{2, 3}, new int[]{2, 3, 4}, "464", algorithm);
            runTest(new int[]{2, 6}, new int[]{3, 5, 9}, "1015", algorithm);
            runTest(new int[]{2, 6}, new int[]{3, 5, 9, 9}, "10015", algorithm);
            runTest(new int[]{1}, new int[]{9, 9, 9, 9}, "10000", algorithm);
        }
    }
}
