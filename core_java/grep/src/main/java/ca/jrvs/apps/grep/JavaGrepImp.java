package ca.jrvs.apps.grep;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep{

  private final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args){
    if(args.length != 3){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try{
      javaGrepImp.process();
    }
    catch (Exception ex){
      javaGrepImp.logger.error(ex.getMessage(), ex);
    }

    /////////////////////////////////////////////////////////

    /*JavaGrepImp test = new JavaGrepImp();

    /*Scanner input = new Scanner(System.in);
    System.out.println("What is the regex?");
    String reg = input.next();
    //System.out.println(reg);
    test.regex = reg;
    //System.out.println(test.regex);

    //System.out.println("What is file directory?");
    String path = "/home/centos/Practice/grep_test";

    List<File> list = test.listFiles(path);
    //System.out.println("The name of the file is: " + list.get(0).toString());
    //System.out.println("The name of the file is: " + list.get(1).toString());

    List<String> outLines = test.readLines(list.get(0));
    System.out.println("The file contains:");
    System.out.println(outLines.get(0));

    String line = outLines.get(0);

    boolean result = test.containsPattern(line);
    System.out.println(result);*/

    /*List<String> lines = new ArrayList<String>();
    lines.add("Hello");
    lines.add("World!");

    try {
      test.writeToFile(lines);
    }
    catch(IOException e){
      System.out.println(e.getMessage());
    }*/

    /*JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(".*");
    javaGrepImp.setRootPath("/home/centos/Practice/grep_test/");
    javaGrepImp.setOutFile("output.txt");

    try{
      javaGrepImp.process();
    }
    catch (Exception ex){
      System.out.println(ex.getMessage());
    }*/

  }

  @Override
  public void process() throws IOException {

    List<String> matchedLines = new ArrayList<String>();
    List<File> files;

    //JavaGrepImp run = new JavaGrepImp();

    files = this.listFiles(this.rootPath);

    for(int i = 0; i < files.size(); i++){

      List<String> lines;

      lines = this.readLines(files.get(i));

      for(int j = 0; j < lines.size(); j++){

        String line = lines.get(j);

        if(this.containsPattern(line)){
          matchedLines.add(line);
        }

      }

    }

    this.writeToFile(matchedLines);

  }

  @Override
  public List<File> listFiles(String rootDir) {

    List<File> fileList = new ArrayList<File>();

    File[] files = new File(rootDir).listFiles();

    for(File file : files){
      if(file.isFile()){
        fileList.add(file);
      }
    }

    return fileList;
  }

  @Override
  public List<String> readLines(File inputFile) {

    List<String> lines = new ArrayList<String>();

    if(inputFile.exists()) {

      try {

        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        String line;

        while ((line = in.readLine()) != null) {
          lines.add(line);
        }

      }
      catch (IOException e){
        logger.error(e.getMessage());
      }

      return lines;

    }
    else{
      throw new IllegalArgumentException("The inputFile is not a file.");
    }
  }

  @Override
  public boolean containsPattern(String line) {

    //System.out.println(this.regex);
    //System.out.println(line);

    String myRegex = this.regex;
    //System.out.println(myRegex);
    String regex = ".*\\.jpg";
    //System.out.println(regex);

    boolean  val = myRegex.equals(regex);
    //System.out.println(val);

    Pattern p = Pattern.compile(myRegex);
    Matcher m = p.matcher(line);
    boolean b = m.matches();
    return b;

  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {

    File outputFile = new File(this.rootPath + this.outFile);
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

    for(int i = 0; i < lines.size(); i++){

      String line = lines.get(i);
      out.print(line);
      out.print("\n");

    }

    System.out.println("File write successful!");

    out.flush();
    out.close();

  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return this.regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return this.outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
