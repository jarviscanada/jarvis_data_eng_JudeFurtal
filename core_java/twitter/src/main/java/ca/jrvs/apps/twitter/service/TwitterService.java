package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.example.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class TwitterService implements Service{

  private CrdDao dao;

  //@Autowired
  public TwitterService(CrdDao dao){
    this.dao = dao;
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

    //Tweet tweetPost = TweetUtil.buildTweet("Testing", 1,1);
    //Tweet tweetResult;
    Tweet tweetResult2;
    String[] fields = {"id","text","retweet_count"};
    //List<Tweet> tweetResult3;
    //String[] ids = {"1282016269152202756"};

    //tweetResult =twitterService.postTweet(tweetPost);
    //System.out.println(JsonParser.toJson(tweetResult,true,false));
    tweetResult2 = twitterService.showTweet("1282062894209826824", fields);
    System.out.println(tweetResult2.getText());
    System.out.println(JsonParser.toJson(tweetResult2, true, false));
    //tweetResult3 = twitterService.deleteTweets(ids);
    //System.out.println(tweetResult3.get(0).getText());

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

    if(StringUtils.isEmpty(fields)){
      return (Tweet) dao.findById(id);
    }
    else{
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

      for(int i = 0; i < fields.length; i++){
        if(fields[i].equals("created_at")){
          result.setCreated_at(tweet.getCreated_at());
        }
        else if(fields[i].equals("id")){
          result.setId(tweet.getId());
        }
        else if(fields[i].equals("id_str")){
          result.setId_str(tweet.getId_str());
        }
        else if(fields[i].equals("text")){
          result.setText(tweet.getText());
        }
        else if(fields[i].equals("entities")){
          result.setEntities(tweet.getEntities());
        }
        else if(fields[i].equals("coordinates")){
          result.setCoordinates(tweet.getCoordinates());
        }
        else if(fields[i].equals("retweet_count")){
          result.setRetweet_count(tweet.getRetweet_count());
        }
        else if(fields[i].equals("favorite_count")){
          result.setFavorite_count(tweet.getFavorite_count());
        }
        else if(fields[i].equals("favorited")){
          result.setFavorited(tweet.isFavorited());
        }
        else if(fields[i].equals("retweeted")){
          result.setRetweeted(tweet.isRetweeted());
        }
      }
      return result;
    }

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
