package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * URL: https://www.notion.so/Two-Sum-c1f3d59975394a839db09977706ea539
 */
public class TwoSumApp3 {

  private final Logger logger = LoggerFactory.getLogger(TwoSumApp3.class);

  /**
   * Analysis: time complexity is 0(n) because the data is stored in a Map. This means that the
   * lookup time for the matching number is 0(1) and storing the data takes O(n) time. This time the
   * passed in array is traversed once.
   *
   * @param nums
   * @param target
   * @return
   */
  public int[] twoSum(int[] nums, int target) {

    boolean found = false;
    int[] result = new int[2];
    Map<Integer, Integer> table = new HashMap<Integer, Integer>();

    for (int i = 0; i < nums.length; i++) {
      Integer key = nums[i];
      Integer value = i;
      Integer match = target - nums[i];
      if (table.containsKey(match)) {
        int firstNum = table.get(match);
        int secondNum = i;
        result[0] = firstNum;
        result[1] = secondNum;
        found = true;
      }
      table.put(key, value);
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
