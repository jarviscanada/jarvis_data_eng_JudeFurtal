package ca.jrvs.practice.codingChallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * URL: https://www.notion.so/Two-Sum-c1f3d59975394a839db09977706ea539
 */
public class TwoSumApp1 {

  private final Logger logger = LoggerFactory.getLogger(TwoSumApp2.class);

  /**
   * Analysis: time complexity is O(n^2) because of the nested for loops. For each element in the
   * array a separate loop is needed to find the result that adds up to the target
   *
   * @param nums
   * @param target
   * @return
   */
  public int[] twoSum(int[] nums, int target) {

    boolean found = false;
    int[] result = new int[2];
    for (int i = 0; i < nums.length; i++) {
      for (int j = i + 1; j < nums.length; j++) {
        int match = target - nums[i];
        int current = nums[j];
        if (current == match) {
          result[0] = i;
          result[1] = j;
          found = true;
        }
      }
    }
    if(found){
      logger.info("Two sum solution found");
      return result;
    }
    else{
      throw new IllegalArgumentException("Two sum solution not found for inputted arguments");
    }
  }

}
