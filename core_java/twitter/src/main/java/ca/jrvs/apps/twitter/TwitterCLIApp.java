package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TwitterCLIApp {

  //create logger
  private final Logger logger = LoggerFactory.getLogger(TwitterCLIApp.class);

  //class instance variables
  public static final String USAGE = "USAGE: TwitterCLIApp post|show|delete [options]";
  private Controller controller;

  @Autowired
  public TwitterCLIApp(Controller controller) {
    this.controller = controller;
  }

  public static void main(String[] args) {
    //Get secrets from env vars
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    //Create components and chain dependencies
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    CrdDao dao = new TwitterDao(httpHelper);
    Service service = new TwitterService(dao);
    Controller controller = new TwitterController(service);
    TwitterCLIApp app = new TwitterCLIApp(controller);

    //Start app
    app.run(args);
  }

  /**
   * This method is used to either post, show or delete tweet objects.
   *
   * @param args
   */
  public void run(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException(USAGE);
    }
    switch (args[0].toLowerCase()) {
      case "post":
        printTweet(controller.postTweet(args));
        logger.info("Post action selected");
        break;
      case "show":
        printTweet(controller.showTweet(args));
        logger.info("Show action selected");
        break;
      case "delete":
        controller.deleteTweet(args).forEach(this::printTweet);
        logger.info("Delete action selected");
        break;
      default:
        throw new IllegalArgumentException(USAGE);
    }
  }

  //Given a Tweet object, this private method will print its information in JSON format.
  private void printTweet(Tweet tweet) {
    try {
      System.out.println(JsonParser.toJson(tweet, true, false));
      logger.info("Tweet object successfully printed in JSON format");
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Unable to convert tweet object to string", e);
    }
  }
}