package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  //create logger
  private final Logger logger = LoggerFactory.getLogger(TwitterControllerUnitTest.class);

  @Mock
  Service service;

  @InjectMocks
  TwitterController controller;

  @Test
  public void postTweet() {

    String text = "testing";
    Double lat = 0.0;
    Double lon = 50.0;
    TwitterController spyController = Mockito.spy(controller);
    String[] input = {"post", text, lon.toString() + ":" + lat.toString()};

    when(service.postTweet(any())).thenReturn(new Tweet());
    Tweet tweet = spyController.postTweet(input);
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
    assertEquals("testing", tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    logger.info("Test completed");

  }
}