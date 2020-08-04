package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.example.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  //create logger
  private final Logger logger = LoggerFactory.getLogger(TwitterDaoUnitTest.class);

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao dao;

  @Test
  public void showTweet() throws Exception {
    //test failed request
    String hashTag = "#abc";
    String text = "@someone sometext" + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    //exception is expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.create(TweetUtil.buildTweet(text, lon, lat));
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    //Test happy path
    //however, we don't want to call parseResponseBody.
    //we will make a spyDao which can fake parseResponseBody return value
    String tweetJsonStr = "{\n"
        + "  \"created_at\" : \"Wed Jul 15 14:38:43 +0000 2020\",\n"
        + "  \"id\" : 1283410672722817024,\n"
        + "  \"id_str\" : \"1283410672722817024\",\n"
        + "  \"text\" : \"testing\",\n"
        + "  \"entities\" : {\n"
        + "    \"hashtags\" : [ ],\n"
        + "    \"user_mentions\" : [ ]\n"
        + "  },\n"
        + "  \"coordinates\" : {\n"
        + "    \"coordinates\" : [ 1.0, 1.0 ],\n"
        + "    \"type\" : \"Point\"\n"
        + "  },\n"
        + "  \"retweet_count\" : 0,\n"
        + "  \"favorite_count\" : 0,\n"
        + "  \"favorited\" : false,\n"
        + "  \"retweeted\" : false\n"
        + "}";

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonParser.toObjectFromJson(tweetJsonStr, Tweet.class);
    //mock parseResponseBody
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet tweet = spyDao.create(TweetUtil.buildTweet(text, lon, lat));
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
    logger.info("Test completed");
  }
}