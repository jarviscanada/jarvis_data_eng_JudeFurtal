package ca.jrvs.practice.codingChallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * URL: https://www.notion.so/Fibonacci-Number-Climbing-Stairs-4b12d628fe634ec79c521f6270286dc8
 */
public class FibonacciNumberApp1 {

  private final Logger logger = LoggerFactory.getLogger(FibonacciNumberApp1.class);

  /**
   * Analysis: time complexity is O(2^n) due to the individual method calls for each level of
   * recursion. Too many recursive calls can lead to a StackOverFlow exception if there is not
   * enough memory.
   *
   * @param n
   * @return
   */
  public int fibNum(int n) {

    //base case
    if ((n == 0) || (n == 1)) {
      logger.info("Fibonacci number successfully computed");
      return n;
    } else if (n > 1) { //recursive case
      logger.info("Fibonacci number successfully computed");
      return fibNum(n - 1) + fibNum(n - 2);
    } else {
      throw new IllegalArgumentException("Invalid argument, input is less than 0");
    }
  }

}
