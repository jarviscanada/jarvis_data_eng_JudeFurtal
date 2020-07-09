package ca.jrvs.apps.twitter.dao.Util;

import ca.jrvs.apps.twitter.dao.model.Coordinates;
import ca.jrvs.apps.twitter.dao.model.Tweet;
import java.util.ArrayList;
import java.util.List;

public class TweetUtil {

  public static Tweet buildTweet(String text, double longitude, double latitude ){

    Tweet tweet = new Tweet();

    tweet.setText(text);

    Double lon = new Double(longitude);
    Double lat = new Double(latitude);
    List<Double> point = new ArrayList<Double>();
    point.add(lon);
    point.add(lat);

    Coordinates coordinates = new Coordinates();
    coordinates.setCoordinates(point);

    tweet.setCoordinates(coordinates);

    return tweet;

  }

}
