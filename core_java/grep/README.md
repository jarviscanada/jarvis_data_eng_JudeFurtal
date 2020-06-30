#Introduction

The grep app is used to recursively search file(s) in a given directory for specific text and then the found text is redirected into an output file. The text is found through pattern matching using Regular Expressions (regex). By developing this app I learned about regex in Java, reading/writing to files, the lambda/stream API and how to use the IntelliJ IDE to create, test, compile and run java code. 

#Usage

This sections shows the usage of the grep app. The app takes three arguments and is used as follows,

App USAGE: `regex rootPath outFile`

The descriptions of the arguments are given below.

- `regex`: a special text string that describes a search pattern
- `rootPath`: the root directory path that needs to be searched
- `outFile`: the name of the output file

The application will search all the files in the `rootPath` recursively for any file that matches the search pattern described by the `regex`. Any lines in the files that match the regex are stored in the `outFile`. 
Below is an simple example of the app usage,

`.*IllegalArgumentException.* ./grep/src /tmp/grep.out`

#Pseudocode

The `process` method is used for high-level workflow purposes. Furthermore, it automates the regex matching process by using all the other methods in the class. The pseudocode for the `process` method is given below.

```java
matchedLines = [] List
for file in listFiles(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

#Performance Issue

It can be seen from the pseudocode that the `process` method is executed using nested for loops, this leads to memory and running time issues. If the input size to the method is small (ex. few files in the directory) then the performance is acceptable. However, if the input size is large then the method will lead to poor performance.

#Improvement

1. Improve performance by storing data in a data structure such as a balanced tree instead of a list.
2. Add an option in the application to redirect the output to the terminal screen. 
3. Add an option in the application to ignore certain files in the directory. This avoids searching files that do not need to be searched.

