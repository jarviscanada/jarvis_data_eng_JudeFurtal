package ca.jrvs.apps.practice;

import java.util.regex.*;
import java.lang.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexExcImp implements RegexExc{

  private final Logger logger = LoggerFactory.getLogger(RegexExcImp.class);

  /**
   * return true if filename extension is jpg or jpeg (case insensitive)
   * @param filename
   * @return
   */
  @Override
  public boolean matchJpeg(String filename) {

    Pattern pattern1 = Pattern.compile(".*\\.jpg");
    Pattern pattern2 = Pattern.compile(".*\\.jpeg");

    Matcher matcher1 = pattern1.matcher(filename);
    Matcher matcher2 = pattern2.matcher(filename);

    if(matcher1.matches() || matcher2.matches()) {
      logger.info("Inputted string matches either .jpg or .jpeg");
      return true;
    }
    else {
      logger.info("Inputted string did not match regex");
      return false;
    }
  }

  /**
   * return true if ip is valid
   * to simplify the problem, IP address range is from 0.0.0.0 to 999.999.999.999
   * @param ip
   * @return
   */
  @Override
  public boolean matchIP(String ip) {

    Pattern patternIp = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    Matcher matcher = patternIp.matcher(ip);
    boolean result = matcher.matches();
    logger.info("Inputted String has been checked to see if it matches the regex.");
    return result;

  }

  /**
   * return true if line is empty (e.g empty, white space, tabs, etc..)
   * @param line
   * @return
   */
  @Override
  public boolean isEmptyLine(String line) {

    Pattern patternLine = Pattern.compile("\\s*");
    Matcher matcher = patternLine.matcher(line);
    boolean result = matcher.matches();
    logger.info("Inputted String has been checked to see if it contains white spaces.");
    return result;

  }

}
