package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller {

  //create logger
  private final Logger logger = LoggerFactory.getLogger(TwitterController.class);

  //class private fields
  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";
  private Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  /**
   * This method is used to post a tweet given the tweet text and geo coordinates.
   *
   * @param args
   * @return Tweet
   */
  @Override
  public Tweet postTweet(String[] args) {

    if (args.length != 3) {
      throw new IllegalArgumentException(
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    String tweet_txt = args[1];
    String coord = args[2];
    String[] coordArray = coord.split(COORD_SEP);

    if (coordArray.length != 2 || StringUtils.isEmpty(tweet_txt)) {
      throw new IllegalArgumentException(
          "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    Double lat = null;
    Double lon = null;

    try {
      lat = Double.parseDouble(coordArray[0]);
      lon = Double.parseDouble(coordArray[1]);
      logger.info("Location successfully set");
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"",
          e);
    }

    Tweet postTweet = TweetUtil.buildTweet(tweet_txt, lon, lat);
    logger.info("Tweet successfully built");
    return service.postTweet(postTweet);

  }

  /**
   * This method is used to show and print a tweet in JSON format.
   *
   * @param args
   * @return Tweet
   */
  @Override
  public Tweet showTweet(String[] args) {

    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: TwitterCLIApp show tweet_id [field1, field2]");
    }

    String id = args[1];
    String fieldArgs = args[2];
    String[] fields = fieldArgs.split(COMMA);

    if (fields.length > 10) {
      throw new IllegalArgumentException("Options size exceeded limit");
    }

    Tweet showTweet = service.showTweet(id, fields);
    logger.info("Tweet successfully printed");
    return showTweet;

  }

  /**
   * This method is used to delete tweets given their ID(s).
   *
   * @param args
   * @return List<Tweet>
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {

    if (args.length != 2) {
      throw new IllegalArgumentException("USAGE: TwitterCLIApp delete [id1, id2,..]");
    }

    String idArgs = args[1];
    String[] ids = idArgs.split(COMMA);

    if (StringUtils.isEmpty(ids)) {
      throw new IllegalArgumentException("ID(s) to delete are not provided");
    }

    List<Tweet> tweetList = service.deleteTweets(ids);
    logger.info("Tweet(s) were successfully deleted");
    return tweetList;

  }
}