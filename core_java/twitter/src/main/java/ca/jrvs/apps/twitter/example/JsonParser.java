package ca.jrvs.apps.twitter.example;

import ca.jrvs.apps.twitter.dao.model.Tweet;
import ca.jrvs.apps.twitter.example.dto.Company;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class JsonParser {

  /**
   * Convert a java object to JSON string
   * @param object
   * @param prettyJson
   * @param includeNullValues
   * @return
   * @throws JsonProcessingException
   */

  public static String toJson(Object object, boolean prettyJson, boolean includeNullValues) throws JsonProcessingException {

    ObjectMapper m = new ObjectMapper();

    if(!includeNullValues){
      m.setSerializationInclusion(Include.NON_NULL);
    }
    if(prettyJson){
        m.enable(SerializationFeature.INDENT_OUTPUT);
    }
    return m.writeValueAsString(object);

  }

  /**
   * Parse JSON string to a object
   * @param json
   * @param clazz
   * @param <T>
   * @return
   * @throws IOException
   */

  public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {

    ObjectMapper m = new ObjectMapper();
    m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return (T) m.readValue(json, clazz);
  }

  public static void main(String[] args) throws IOException {

    /*Company company = toObjectFromJson(companyStr, Company.class);
    System.out.println(toJson(company, true, false));*/

    Tweet tweet = toObjectFromJson(tweetStr, Tweet.class);
    System.out.println(toJson(tweet, true, false));

  }

  public static final String companyStr = "{\n"
      + "   \"symbol\":\"AAPL\",\n"
      + "   \"companyName\":\"Apple Inc.\",\n"
      + "   \"exchange\":\"Nasdaq Global Select\",\n"
      + "   \"description\":\"Apple Inc is designs, manufactures and markets mobile communication and media devices and personal computers, and sells a variety of related software, services, accessories, networking solutions and third-party digital content and applications.\",\n"
      + "   \"CEO\":\"Timothy D. Cook\",\n"
      + "   \"sector\":\"Technology\",\n"
      + "   \"financials\":[\n"
      + "      {\n"
      + "         \"reportDate\":\"2018-12-31\",\n"
      + "         \"grossProfit\":32031000000,\n"
      + "         \"costOfRevenue\":52279000000,\n"
      + "         \"operatingRevenue\":84310000000,\n"
      + "         \"totalRevenue\":84310000000,\n"
      + "         \"operatingIncome\":23346000000,\n"
      + "         \"netIncome\":19965000000\n"
      + "      },\n"
      + "      {\n"
      + "         \"reportDate\":\"2018-09-30\",\n"
      + "         \"grossProfit\":24084000000,\n"
      + "         \"costOfRevenue\":38816000000,\n"
      + "         \"operatingRevenue\":62900000000,\n"
      + "         \"totalRevenue\":62900000000,\n"
      + "         \"operatingIncome\":16118000000,\n"
      + "         \"netIncome\":14125000000\n"
      + "      }\n"
      + "   ],\n"
      + "   \"dividends\":[\n"
      + "      {\n"
      + "         \"exDate\":\"2018-02-09\",\n"
      + "         \"paymentDate\":\"2018-02-15\",\n"
      + "         \"recordDate\":\"2018-02-12\",\n"
      + "         \"declaredDate\":\"2018-02-01\",\n"
      + "         \"amount\":0.63\n"
      + "      },\n"
      + "      {\n"
      + "         \"exDate\":\"2017-11-10\",\n"
      + "         \"paymentDate\":\"2017-11-16\",\n"
      + "         \"recordDate\":\"2017-11-13\",\n"
      + "         \"declaredDate\":\"2017-11-02\",\n"
      + "         \"amount\":0.63\n"
      + "      }\n"
      + "   ]\n"
      + "}";

  public static final String tweetStr = "{\n"
      + "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
      + "   \"id\":1097607853932564480,\n"
      + "   \"id_str\":\"1097607853932564480\",\n"
      + "   \"text\":\"test with loc223\",\n"
      + "   \"entities\":{\n"
      + "      \"hashtags\":[\n"
      + "         {\n"
      + "            \"text\":\"documentation\",\n"
      + "            \"indices\":[\n"
      + "               211,\n"
      + "               225\n"
      + "            ]\n"
      + "         },\n"
      + "         {\n"
      + "            \"text\":\"parsingJSON\",\n"
      + "            \"indices\":[\n"
      + "               226,\n"
      + "               238\n"
      + "            ]\n"
      + "         },\n"
      + "         {\n"
      + "            \"text\":\"GeoTagged\",\n"
      + "            \"indices\":[\n"
      + "               239,\n"
      + "               249\n"
      + "            ]\n"
      + "         }\n"
      + "      ],\n"
      + "      \"user_mentions\":[\n"
      + "         {\n"
      + "            \"name\":\"Twitter API\",\n"
      + "            \"indices\":[\n"
      + "               4,\n"
      + "               15\n"
      + "            ],\n"
      + "            \"screen_name\":\"twitterapi\",\n"
      + "            \"id\":6253282,\n"
      + "            \"id_str\":\"6253282\"\n"
      + "         }\n"
      + "      ]\n"
      + "   },\n"
      + "   \"coordinates\":{\n"
      + "      \"coordinates\":[\n"
      + "         -75.14310264,\n"
      + "         40.05701649\n"
      + "      ],\n"
      + "      \"type\":\"Point\"\n"
      + "   },\n"
      + "   \"retweet_count\":0,\n"
      + "   \"favorite_count\":0,\n"
      + "   \"favorited\":false,\n"
      + "   \"retweeted\":false\n"
      + "}";

}
