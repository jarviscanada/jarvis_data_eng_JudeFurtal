package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.example.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

  private final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  //Class constructor
  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  /**
   * This method is used to create and return a Tweet object given a DTO/model.
   *
   * @param entity entity that to be created
   * @return Tweet
   */
  @Override
  public Tweet create(Tweet entity) {

    //Construct URI
    URI uri;

    try {
      uri = getPostUri(entity);
      logger.info("Successfully obtained post URI");
    } catch (URISyntaxException | UnsupportedEncodingException e) {
      logger.error("Invalid tweet input");
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    //Execute HTTP Request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and deser response to Tweet object
    logger.info("parseResponseBody() method invoked");
    return parseResponseBody(response, HTTP_OK);

  }

  /**
   * Check response status code Convert Response Entity to Tweet
   */
  Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
    Tweet tweet = null;

    //Check response status
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        logger.info("Response has an entity: " + EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        logger.error("Response has no entity");
      }
      logger.error("Unexpected HTTP status");
      throw new RuntimeException("Unexpected HTTP status:" + status);
    }

    if (response.getEntity() == null) {
      logger.error("Response body is empty");
      throw new RuntimeException("Empty response body");
    }

    //Convert Response Entity to str
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
      logger.info("Successfully converted entity to String");
    } catch (IOException e) {
      logger.error("Failed to convert entity to String");
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    //Deser JSON string to Tweet object
    try {
      tweet = JsonParser.toObjectFromJson(jsonStr, Tweet.class);
      logger.info("Successfully converted JSON str to Object");
    } catch (IOException e) {
      logger.error("Unable to convert JSON str to Object");
      throw new RuntimeException("Unable to convert JSON str to Object", e);
    }

    logger.info("Tweet object successfully created");
    return tweet;

  }

  /**
   * This method finds and returns a Tweet object given its ID.
   *
   * @param s
   * @return Tweet
   */
  @Override
  public Tweet findById(String s) {

    //Construct URI
    URI uri;

    try {
      uri = getShowUri(s);
      logger.info("Successfully obtained show URI");
    } catch (URISyntaxException | UnsupportedEncodingException e) {
      logger.info("Invalid tweet input");
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    //Execute HTTP Request
    HttpResponse response = httpHelper.httpGet(uri);

    //Validate response and deser response to Tweet object
    logger.info("parseResponseBody() method invoked");
    return parseResponseBody(response, HTTP_OK);

  }

  /**
   * This method deletes a Tweet object given its ID and then returns the deleted Tweet.
   *
   * @param s
   * @return Tweet
   */
  @Override
  public Tweet deleteById(String s) {

    //Construct URI
    URI uri;

    try {
      uri = getDeleteUri(s);
      logger.info("Successfully obtained delete URI");
    } catch (URISyntaxException | UnsupportedEncodingException e) {
      logger.error("Invalid tweet input");
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    //Execute HTTP Request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and deser response to Tweet object
    logger.info("parseResponseBody() method invoked");
    return parseResponseBody(response, HTTP_OK);

  }

  //Private method used to obtain post URI
  private URI getPostUri(Tweet tweet) throws URISyntaxException, UnsupportedEncodingException {

    Double lon = tweet.getCoordinates().getCoordinates().get(0);
    Double lat = tweet.getCoordinates().getCoordinates().get(1);

    //Accounts for texts that have spaces
    String text = tweet.getText().replaceAll("\\s", "%20");

    String uriString =
        API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + text + AMPERSAND + "long" + EQUAL
            + lon + AMPERSAND + "lat" + EQUAL + lat;
    URI uri = new URI(uriString);

    logger.info("Successfully obtained post URI");
    return uri;

  }

  //Private method used to obtain show URI
  private URI getShowUri(String s) throws URISyntaxException, UnsupportedEncodingException {

    String uriString = API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s;
    URI uri = new URI(uriString);

    logger.info("Successfully obtained show URI");
    return uri;

  }

  //Private method used to obtain delete URI
  private URI getDeleteUri(String s) throws URISyntaxException, UnsupportedEncodingException {

    String uriString = API_BASE_URI + DELETE_PATH + "/" + s + ".json";
    URI uri = new URI(uriString);

    logger.info("Successfully obtained delete URI");
    return uri;

  }

}