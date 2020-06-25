package ca.jrvs.apps.practice;

import java.util.regex.*;
import java.util.Scanner;
import java.io.PrintStream;

public class RegexExcImp implements RegexExc{

  @Override
  public boolean matchJpeg(String filename) {

    Pattern p1 = Pattern.compile(".*\\.jpg");
    Pattern p2 = Pattern.compile(".*\\.jpeg");

    Matcher m1 = p1.matcher(filename);
    Matcher m2 = p2.matcher(filename);

    if(m1.matches() || m2.matches()) {
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public boolean matchIP(String ip) {

    Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    Matcher m = p.matcher(ip);
    boolean b = m.matches();
    return b;

  }

  @Override
  public boolean isEmptyLine(String line) {

    Pattern p = Pattern.compile("\\s*");
    Matcher m = p.matcher(line);
    boolean b = m.matches();
    return b;

  }

  public static void main(String[] args){

    /*Scanner input = new Scanner(System.in);
    PrintStream output = System.out;

    output.println("what is the line?");
    String line = input.next();*/

    boolean result;
    RegexExcImp regex = new RegexExcImp();

    result = regex.isEmptyLine("  ");
    System.out.println(result);

  }

}
