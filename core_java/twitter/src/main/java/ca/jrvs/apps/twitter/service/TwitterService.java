package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class TwitterService implements Service {

  //create logger
  private final Logger logger = LoggerFactory.getLogger(TwitterService.class);

  //class private field
  private CrdDao dao;

  //@Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  /**
   * This method is used to create and post a tweet.
   *
   * @param tweet tweet to be created
   * @return Tweet
   */
  @Override
  public Tweet postTweet(Tweet tweet) {

    //Business logic:
    //e.g text length, lat/lon range, id format
    validatePostTweet(tweet);
    logger.info("Tweet has been validated");

    //create tweet via dao
    logger.info("Tweet object created and returned");
    return (Tweet) dao.create(tweet);

  }

  //private method used to validate a tweet before posting
  private void validatePostTweet(Tweet tweet) {

    String text = tweet.getText();
    Double lon = tweet.getCoordinates().getCoordinates().get(0);
    Double lat = tweet.getCoordinates().getCoordinates().get(1);

    if (text.length() > 140) {
      logger.error("Tweet exceeds 140 characters");
      throw new IllegalArgumentException("Tweet exceeds 140 characters");
    }

    if ((lon > 180.0) || (lon < -180.0)) {
      logger.error("Tweet coordinates are out of range");
      throw new IllegalArgumentException("Tweet coordinates are out of range");
    }

    if ((lat > 90.0) || (lat < -90.0)) {
      logger.error("Tweet coordinates are out of range");
      throw new IllegalArgumentException("Tweet coordinates are out of range");
    }

  }

  /**
   * This method is used to show and return a tweet given its ID and optional fields.
   *
   * @param id     tweet id
   * @param fields set fields not in the list to null
   * @return Tweet
   */
  @Override
  public Tweet showTweet(String id, String[] fields) {

    //Checks both range and non-digit characters. Says id MUST be a long to be valid.
    try {
      Long ID = Long.parseLong(id);
      logger.info("Valid ID");
    } catch (NumberFormatException e) {
      logger.info("Invalid ID");
      throw new IllegalArgumentException("Invalid ID");
    }

    for (String field : fields) {
      if (field == null) {
        logger.info("Invalid field");
        throw new IllegalArgumentException("Invalid field");
      }
    }

    if (StringUtils.isEmpty(fields)) {
      logger.info("Full Tweet object is returned since no options were selected");
      return (Tweet) dao.findById(id);
    } else {
      //Create tweet object with only selected fields
      Tweet tweet = (Tweet) dao.findById(id);
      Tweet result = new Tweet();
      result.setCreated_at(null);
      result.setId(null);
      result.setId_str(null);
      result.setText(null);
      result.setEntities(null);
      result.setCoordinates(null);
      result.setRetweet_count(null);
      result.setFavorite_count(null);
      result.setFavorited(null);
      result.setRetweeted(null);

      for (int i = 0; i < fields.length; i++) {
        if (fields[i].equals("created_at")) {
          result.setCreated_at(tweet.getCreated_at());
        } else if (fields[i].equals("id")) {
          result.setId(tweet.getId());
        } else if (fields[i].equals("id_str")) {
          result.setId_str(tweet.getId_str());
        } else if (fields[i].equals("text")) {
          result.setText(tweet.getText());
        } else if (fields[i].equals("entities")) {
          result.setEntities(tweet.getEntities());
        } else if (fields[i].equals("coordinates")) {
          result.setCoordinates(tweet.getCoordinates());
        } else if (fields[i].equals("retweet_count")) {
          result.setRetweet_count(tweet.getRetweet_count());
        } else if (fields[i].equals("favorite_count")) {
          result.setFavorite_count(tweet.getFavorite_count());
        } else if (fields[i].equals("favorited")) {
          result.setFavorited(tweet.isFavorited());
        } else if (fields[i].equals("retweeted")) {
          result.setRetweeted(tweet.isRetweeted());
        }
      }
      logger.info("Tweet object returned with selected options");
      return result;
    }

  }

  /**
   * This method is used to delete tweet objects by ID(s). Deleted objects are returned in a List.
   *
   * @param ids tweet IDs which will be deleted
   * @return List<Tweet>
   */
  @Override
  public List<Tweet> deleteTweets(String[] ids) {

    List<Tweet> tweetList = new ArrayList<Tweet>();

    for (String id : ids) {
      try {
        Long ID = Long.parseLong(id);
      } catch (NumberFormatException e) {
        logger.error("Invalid ID");
        throw new IllegalArgumentException("Invalid ID");
      }
      tweetList.add((Tweet) dao.deleteById(id));
    }
    logger.info("Tweets successfully deleted");
    return tweetList;

  }
}