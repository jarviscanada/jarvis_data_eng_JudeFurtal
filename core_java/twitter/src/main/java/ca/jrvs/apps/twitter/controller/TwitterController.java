package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class TwitterController implements Controller{

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private Service service;

  //@Autowired
  public TwitterController(Service service){
    this.service = service;
  }

  public static void main(String[] args) throws JsonProcessingException {

    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    //Create components
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    TwitterDao DAO = new TwitterDao(httpHelper);
    TwitterService twitterService = new TwitterService(DAO);
    TwitterController twitterController = new TwitterController(twitterService);

    //String[] input1 = {"post", "testing","1:1"};
    //Tweet result1 = twitterController.postTweet(input1);

    //String[] input2 = {"show", "1282080002360651776", "id,text,retweet_count"};
    //Tweet result2 = twitterController.showTweet(input2);
    //System.out.println(JsonParser.toJson(result2, true, false));

    String[] input3 = {"delete", "1282080002360651776"};
    List<Tweet> tweetList = twitterController.deleteTweet(input3);
    Tweet result3 = tweetList.get(0);
    System.out.println(JsonParser.toJson(result3, true, false));

  }

  @Override
  public Tweet postTweet(String[] args) {

    if(args.length != 3){
      throw new IllegalArgumentException("USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    String tweet_txt = args[1];
    String coord = args[2];
    String[] coordArray = coord.split(COORD_SEP);

    if(coordArray.length != 2 || StringUtils.isEmpty(tweet_txt)){
      throw new IllegalArgumentException("Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    Double lat = null;
    Double lon = null;

    try{
      lat = Double.parseDouble(coordArray[0]);
      lon = Double.parseDouble(coordArray[1]);
    }catch(Exception e){
      throw new IllegalArgumentException("Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"", e);
    }

    Tweet postTweet = TweetUtil.buildTweet(tweet_txt, lon, lat);
    return service.postTweet(postTweet);

  }

  @Override
  public Tweet showTweet(String[] args) {

    if(args.length != 3){
      throw new IllegalArgumentException("USAGE: TwitterCLIApp show tweet_id [field1, field2]");
    }

    String id = args[1];
    String fieldArgs = args[2];
    String[] fields = fieldArgs.split(COMMA);

    if(fields.length > 10){
      throw new IllegalArgumentException("Options size exceeded limit");
    }

    Tweet showTweet = service.showTweet(id, fields);
    return showTweet;

  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {

    if(args.length != 2){
      throw new IllegalArgumentException("USAGE: TwitterCLIApp delete [id1, id2,..]");
    }

    String idArgs = args[1];
    String[] ids = idArgs.split(COMMA);

    if(StringUtils.isEmpty(ids)){
      throw new IllegalArgumentException("ID(s) to delete are not provided");
    }

    List<Tweet> tweetList = service.deleteTweets(ids);
    return tweetList;

  }
}
