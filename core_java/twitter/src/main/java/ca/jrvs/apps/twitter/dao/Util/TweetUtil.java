package ca.jrvs.apps.twitter.dao.Util;

import ca.jrvs.apps.twitter.dao.model.Coordinates;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetUtil {

  //create logger
  private static final Logger logger = LoggerFactory.getLogger(TweetUtil.class);

  /**
   * This method is used to build a Tweet object given the text and the location (latitude and
   * longitude)
   *
   * @param text
   * @param longitude
   * @param latitude
   * @return Tweet
   */
  public static Tweet buildTweet(String text, double longitude, double latitude) {

    Tweet tweet = new Tweet();
    tweet.setText(text);

    Double lon = new Double(longitude);
    Double lat = new Double(latitude);

    //List to store location information
    List<Double> point = new ArrayList<Double>();
    point.add(lon);
    point.add(lat);

    Coordinates coordinates = new Coordinates();
    coordinates.setCoordinates(point);

    tweet.setCoordinates(coordinates);

    logger.info("Tweet successfully built");
    return tweet;

  }

}