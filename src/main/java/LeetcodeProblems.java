
public class LeetcodeProblems {

    /*
    You are given two non-empty linked lists representing two non-negative integers.
    The digits are stored in reverse order, and each of their nodes contains a single digit.
    Add the two numbers and return the sum as a linked list. You may assume the two numbers
    do not contain any leading zero, except the number 0 itself. The lists can be different sizes.
    */
    // Linked list helper class
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
        public String toNumber() {
            String value = "";
            for (ListNode ln = this; ln != null; ln = ln.next) {
                // System.out.println("Adding value " + ln.val);
                value = ln.val + value;
            }
            return value;
        }
    }

    // listArray contains the digits of a number in reverse order
    public static ListNode convertArrayToLinkedList(int[] listArray) {
        ListNode head = new ListNode();
        ListNode listPtr = head;
        for (int i = 0; i < listArray.length;  i++) {
            listPtr.val = listArray[i];
            // System.out.println("Adding value " + i);
            if (i < listArray.length - 1) {
                listPtr.next = new ListNode();
                listPtr = listPtr.next;
            }
        }
        listPtr = null;
        return head;
    }

    public static ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        ListNode head = result;
        int carry = 0;
        for (ListNode ptr1 = l1, ptr2 = l2; ptr1 != null || ptr2 != null;) {

            int val1 = 0, val2 = 0;
            if (ptr1 != null) {
                val1 = ptr1.val;
                ptr1 = ptr1.next;
            }
/*
            else {
                // Just assign the rest of the result to the non-empty list
                // result.val = ptr2.val + carry;  // Have to account for val > 10 here
                // result.next = ptr2.next;
                // break;
                ptr1 = new ListNode(0);
            }
*/
            if (ptr2 != null) {
                val2 = ptr2.val;
                ptr2 = ptr2.next;
            }
/*
            else {
                // Just assign the rest of the result to the non-empty list
                // result.val = ptr1.val + carry;  // Have to account for val > 10 here
                // result.next = ptr1.next;
                // break;
                ptr2 = new ListNode(0);
            }
*/
            result.val = (val1 + val2 + carry) % 10;
            if (ptr1 != null && ptr2 != null) {
                result.next = new ListNode();
                result = result.next;
            }
            carry = (val1 + val2 + carry) / 10;
        }
        // System.out.println("Result (before update): " + head.toNumber());
        result.next = (carry == 1 ? new ListNode(1, null) : null);
        // System.out.println("Result (after update): " + head.toNumber());
        // Set l1 = l2 = null to free memory?
        return head;
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        ListNode head = result;
        int carry = 0;
        // for (ListNode ptr1 = l1, ptr2 = l2; ptr1 != null && ptr2 != null; ptr1 = ptr1.next, ptr2 = ptr2.next) {
        for (ListNode ptr1 = l1, ptr2 = l2; ptr1 != null || ptr2 != null; ptr1 = ptr1.next, ptr2 = ptr2.next) {

            if (ptr2 == null) {
                // System.out.println("Adding 0 node to ptr2");
                ptr2 = new ListNode(0);
            }
            if (ptr1 == null) {
                // System.out.println("Adding 0 node to ptr1");
                ptr1 = new ListNode(0);
            }

            result.val = (ptr1.val + ptr2.val + carry) % 10;
            carry = (ptr1.val + ptr2.val + carry) / 10;
            if (ptr1.next != null || ptr2.next != null) {
                result.next = new ListNode();
                result = result.next;
            }
        }
        result.next = (carry == 1 ? new ListNode(1, null) : null);
        // Set l1 = l2 = null to free memory?
        return head;
    }

    public static void runTest(int[] l1, int[] l2, String expectedValue) {
        ListNode list1 = convertArrayToLinkedList(l1);
        ListNode list2 = convertArrayToLinkedList(l2);
        // ToDo: Test again with addTwoNumbers1()
        String answer = addTwoNumbers(list1, list2).toNumber();
        System.out.println(list1.toNumber() + " + " + list2.toNumber() + " = " + answer);
        if (!answer.equals(expectedValue)) {
            // ToDo: Convert to an assertion
            System.out.println("TEST FAILED: Expected " + expectedValue + " but got " + answer);
        }
    }

    // ToDo: Check in updates to GitHub
    public static void main(String[] argv) {
        runTest(new int[] { 2, 4, 3 }, new int[] { 5, 6, 4 },"807");
        runTest(new int[] { 5, 6, 7 }, new int[] { 6, 4, 5 },"1311");
        runTest(new int[] { 2, 3 }, new int[] { 2, 3, 4 },"464");
        runTest(new int[] { 2, 6 }, new int[] { 3, 5, 9 },"1015");
    }
}
