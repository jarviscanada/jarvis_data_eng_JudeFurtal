package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {

  private final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error(ex.getMessage(), ex);
    }

  }

  /**
   * Top level search workflow used to automate the regex matching process
   *
   * @throws IOException
   */
  @Override
  public void process() throws IOException {

    List<String> matchedLines = new ArrayList<String>();
    List<File> files;

    files = this.listFiles(this.rootPath);

    for (int i = 0; i < files.size(); i++) {
      List<String> lines;
      lines = this.readLines(files.get(i));

      for (int j = 0; j < lines.size(); j++) {
        String line = lines.get(j);
        if (this.containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    logger.info("Regex matching process successfully completed.");
    this.writeToFile(matchedLines);
  }

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) {

    List<File> fileList = new ArrayList<File>();

    File[] files = new File(rootDir).listFiles();

    for (File file : files) {
      if (file.isFile()) {
        fileList.add(file);
      }
    }
    logger.info("All files returned for given directory.");
    return fileList;
  }

  /**
   * Read a file and return all the lines
   *
   * @param inputFile file to be read
   * @return Lines
   * @throws IllegalArgumentException if a inputFile is not a file
   */
  @Override
  public List<String> readLines(File inputFile) {

    List<String> lines = new ArrayList<String>();

    if (inputFile.exists()) {
      try {
        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        String line;

        while ((line = in.readLine()) != null) {
          lines.add(line);
        }
      } catch (IOException e) {
        logger.error(e.getMessage());
      }
      logger.info("All lines returned for the file.");
      return lines;

    } else {
      throw new IllegalArgumentException("The inputFile is not a file.");
    }
  }

  /**
   * check if a line contains the regex pattern (passed by user)
   *
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {

    String myRegex = this.regex;
    Pattern p = Pattern.compile(myRegex);
    Matcher m = p.matcher(line);
    boolean b = m.matches();
    logger.info("Pattern checking is complete.");
    return b;
  }

  /**
   * Write lines to a file
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {

    File outputFile = new File(this.rootPath + this.outFile);
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      out.print(line);
      out.print("\n");
    }

    logger.info("Matched lines are printed to the file");
    //flush and close the stream
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
