package ca.jrvs.apps.twitter.dao.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hashtag {

  private List<Integer> indices = null;
  private String text;

  public List<Integer> getIndices(){
    return indices;
  }

  public void setIndices(List<Integer> indices){
    this.indices = indices;
  }

  public String getText(){
    return text;
  }

  public void setText(String text){
    this.text = text;
  }

}
