package ca.jrvs.practice.codingChallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * URL: https://www.notion.so/Fibonacci-Number-Climbing-Stairs-4b12d628fe634ec79c521f6270286dc8
 */
public class FibonacciNumberApp2 {

  private final Logger logger = LoggerFactory.getLogger(FibonacciNumberApp2.class);

  /**
   * Analysis: time complexity is O(n) since the array is traversed only once to store the data.
   * Retrieving the final result takes O(1) time since the result is already stored in the array.
   *
   * @param n
   * @return
   */
  public int fibNum(int n) {

    if ((n == 1) || (n == 0)) {
      return n;
    } else if (n > 1) {
      int[] storage = new int[n + 1];
      for (int i = 0; i < storage.length; i++) {
        if (i == 0) {
          storage[i] = 0;
        } else if (i == 1) {
          storage[i] = 1;
        } else {
          storage[i] = storage[i - 1] + storage[i - 2];
        }
      }
      logger.info("Fibonacci number successfully computed");
      return storage[n];
    } else {
      throw new IllegalArgumentException("Invalid argument, input is less than 0");
    }
  }

}
