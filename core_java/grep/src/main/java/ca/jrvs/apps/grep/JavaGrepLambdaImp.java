package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaImp extends JavaGrepImp {

  private final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  public static void main(String[] args) {

    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception e) {
      javaGrepLambdaImp.logger.error(e.getMessage());
    }

  }

  /**
   * Read a file and return all the lines This method uses the Lambda and Stream API
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
        Stream<String> stream = Files.lines(Paths.get(inputFile.getAbsolutePath()));
        stream.forEach(lines::add);
      } catch (IOException e) {
        logger.error(e.getMessage());
      }

      logger.info("File has been read and all lines have been returned.");
      return lines;

    } else {
      throw new IllegalArgumentException("The inputFile is not a file.");
    }
  }

  /**
   * Traverse a given directory and return all files This method uses the Lambda and Stream API
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) {

    List<File> fileList = new ArrayList<File>();

    File[] filesArray = new File(rootDir).listFiles();
    List<File> files = Arrays.asList(filesArray);

    files.stream().forEachOrdered(fileList::add);

    logger.info("Directory has been traversed and all files are returned.");
    return fileList;

  }

}
