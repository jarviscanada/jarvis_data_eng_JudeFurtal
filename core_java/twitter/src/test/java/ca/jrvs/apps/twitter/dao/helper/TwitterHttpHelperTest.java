package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterHttpHelperTest {

  private final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperTest.class);

  @Test
  public void httpPost() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    //Create components
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    logger.info("HttpHelper initialized with secrets");
    HttpResponse response = httpHelper
        .httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=tweet_test"));
    logger.info("httpPost method is used");
    System.out.println(EntityUtils.toString(response.getEntity()));
  }
}