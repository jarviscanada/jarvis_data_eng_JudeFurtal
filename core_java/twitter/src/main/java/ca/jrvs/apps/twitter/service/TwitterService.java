package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterService implements Service{

  private CrdDao dao;

  //@Autowired
  public TwitterService(CrdDao dao){
    this.dao = dao;
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
    TwitterService twitterService = new TwitterService(DAO);

    //Tweet tweetPost = TweetUtil.buildTweet("Testing", 1,1);
    //Tweet tweetResult;
    //Tweet tweetResult2;
    //String[] strings = {"id"};
    List<Tweet> tweetResult3;
    String[] ids = {"1282016269152202756"};

    //tweetResult =twitterService.postTweet(tweetPost);
    //tweetResult2 = twitterService.showTweet("1282016269152202756", strings);
    //System.out.println(tweetResult2.getText());
    tweetResult3 = twitterService.deleteTweets(ids);
    System.out.println(tweetResult3.get(0).getText());

  }

  @Override
  public Tweet postTweet(Tweet tweet) {

    //Business logic:
    //e.g text length, lat/lon range, id format
    validatePostTweet(tweet);

    //create tweet via dao
    return (Tweet) dao.create(tweet);

  }

  private void validatePostTweet(Tweet tweet) {

    String text = tweet.getText();
    Double lon = tweet.getCoordinates().getCoordinates().get(0);
    Double lat = tweet.getCoordinates().getCoordinates().get(1);

    if(text.length() > 140){
      throw new IllegalArgumentException("Tweet exceeds 140 characters");
    }

    if((lon > 180.0) || (lon < -180.0)){
      throw new IllegalArgumentException("Tweet coordinates are out of range");
    }

  }

  @Override
  public Tweet showTweet(String id, String[] fields) {

    //Checks both range and non-digit characters. Says id MUST be a long to be valid.
    try{
      Long ID = Long.parseLong(id);
    }catch(NumberFormatException e){
      throw new IllegalArgumentException("Invalid ID");
    }

    for(String field : fields) {
      if(field == null) {
        throw new IllegalArgumentException("Invalid field");
      }
    }

    return (Tweet) dao.findById(id);

  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {

    List<Tweet> tweetList = new ArrayList<Tweet>();

    for(String id : ids){

      try{
        Long ID = Long.parseLong(id);
      }catch(NumberFormatException e){
        throw new IllegalArgumentException("Invalid ID");
      }

      tweetList.add((Tweet) dao.deleteById(id));

    }

    return tweetList;

  }
}
