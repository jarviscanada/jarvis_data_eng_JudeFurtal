package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterControllerIntTest {

  //create logger
  private final Logger logger = LoggerFactory.getLogger(TwitterControllerIntTest.class);

  private TwitterController controller;

  @Before
  public void setUp() {

    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    //Set up dependency
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    TwitterDao dao = new TwitterDao(httpHelper);
    TwitterService service = new TwitterService(dao);

    //Pass dependency
    this.controller = new TwitterController(service);

  }

  @Test
  public void postTweet() throws Exception {

    String hashTag = "#abc";
    String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
    System.out.println(JsonParser.toJson(postTweet, true, false));

    String[] input = {"post", text, lon.toString() + ":" + lat.toString()};

    Tweet tweet = controller.postTweet(input);

    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().size());
    assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    assertTrue(hashTag.contains(tweet.getEntities().getHashtags().get(0).getText()));
    logger.info("Test completed");

  }
}