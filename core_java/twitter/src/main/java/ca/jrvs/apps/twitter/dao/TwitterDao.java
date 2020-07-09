package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.model.Coordinates;
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

public class TwitterDao implements CrdDao<Tweet, String>{

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

  public TwitterDao(HttpHelper httpHelper){

    this.httpHelper = httpHelper;

  }

  public static void main(String[] args) {

    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    //Create components
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);

    TwitterDao DAO = new TwitterDao(httpHelper);

    //Tweet entity = new Tweet();
    //entity.setText("testing1");

    Tweet entity = TweetUtil.buildTweet("TESTING TESTING", -79.3832, 43.6532);
    System.out.println(entity.getCoordinates().getCoordinates().get(0));

    Tweet result = DAO.create(entity);

    System.out.println(result.getText());
    System.out.println(result.getId_str());
    System.out.println(result.getCoordinates().getCoordinates().get(0));

    //Tweet result2 = DAO.findById("1281260687650586624");
    //System.out.println(result2.getText());

    //Tweet result3 = DAO.deleteById("1281260687650586624");
    //System.out.println(result3.getText());


  }

  @Override
  public Tweet create(Tweet entity) {

    //Construct URI
    URI uri;

    try{
      uri = getPostUri(entity);
    }catch(URISyntaxException | UnsupportedEncodingException e){
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    //Execute HTTP Request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and deser response to Tweet object
    return parseResponseBody(response, HTTP_OK);

  }

  /**
   * Check response status code Convert Response Entity to Tweet
   */
  private Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode){
    Tweet tweet = null;

    //Check response status
    int status = response.getStatusLine().getStatusCode();
    if(status != expectedStatusCode){
      try{
        System.out.println(EntityUtils.toString(response.getEntity()));
      }catch(IOException e){
        System.out.println("Response has no entity");
      }
      throw new RuntimeException("Unexpected HTTP status:" + status);
    }

    if(response.getEntity() == null){
      throw new RuntimeException("Empty response body");
    }

    //Convert Response Entity to str
    String jsonStr;
    try{
      jsonStr = EntityUtils.toString(response.getEntity());

      System.out.println(jsonStr);

    }catch(IOException e){
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    //Deser JSON string to Tweet object
    try{
      tweet = JsonParser.toObjectFromJson(jsonStr, Tweet.class);
    }catch(IOException e){
      throw new RuntimeException("Unable to convert JSON str to Object", e);
    }

    return tweet;

  }

  @Override
  public Tweet findById(String s) {

    //Construct URI
    URI uri;

    try{
      uri = getShowUri(s);
    }catch(URISyntaxException | UnsupportedEncodingException e){
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    //Execute HTTP Request
    HttpResponse response = httpHelper.httpGet(uri);

    //Validate response and deser response to Tweet object
    return parseResponseBody(response, HTTP_OK);

  }

  @Override
  public Tweet deleteById(String s) {

    //Construct URI
    URI uri;

    try{
      uri = getDeleteUri(s);
    }catch(URISyntaxException | UnsupportedEncodingException e){
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    //Execute HTTP Request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and deser response to Tweet object
    return parseResponseBody(response, HTTP_OK);

  }

  private URI getPostUri(Tweet tweet) throws URISyntaxException, UnsupportedEncodingException{

    Double lon = tweet.getCoordinates().getCoordinates().get(0);
    Double lat = tweet.getCoordinates().getCoordinates().get(1);

    String text = tweet.getText().replaceAll("\\s", "%20");


    String uriString = API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + text + AMPERSAND + "long" + EQUAL + lon + AMPERSAND + "lat" + EQUAL + lat;
    URI uri = new URI(uriString);


    System.out.println(uriString);

    return uri;

  }

  private URI getShowUri(String s) throws URISyntaxException, UnsupportedEncodingException{

    String uriString = API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s;
    URI uri = new URI(uriString);

    System.out.println(uriString);

    return uri;

  }

  private URI getDeleteUri(String s) throws URISyntaxException, UnsupportedEncodingException {

    String uriString = API_BASE_URI + DELETE_PATH + "/" + s + ".json";
    URI uri = new URI(uriString);

    System.out.println(uriString);

    return uri;

  }

}
