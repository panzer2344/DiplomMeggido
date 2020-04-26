package data.reader;

import model.Inequality;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.Inequality.ZERO_CONSTRAINT;

public class DataReader {

  private static final String lineSeparator = System.getProperty("line.separator");
  private int lineSize = -1;

  /**
   *
   * one row should looks like 1 -1 3,
   * its equals to x - y + 3 <= 0
   *
   * */
  public double[][] readFromString(String testData) {
    double[][] resultData;

    String[] lines = testData.split(lineSeparator);
    resultData = new double[lines.length][];

    for (int i = 0; i < lines.length; i++) {
      String[] numbers = lines[i].split("\\s+");
      if(lineSize == -1) lineSize = numbers.length;
      if(numbers.length != lineSize) throw new IllegalArgumentException();
      resultData[i] = new double[lineSize];
      for (int j = 0; j < numbers.length; j++) {
        resultData[i][j] = Double.parseDouble(numbers[j]);
      }
    }

    return resultData;
  }

  /**
   *
   * one row should looks like y <=( >= / > / < ) a_1 * x_1 + a_2 * x_2 + ...
   * or 0 <= ( >= / > / < ) a_1 * x_1 + a_2 * x_2 + ...
   *
   * */
  public Inequality[] readIneqsFromString(String testData, int dimensions) {
    List<Inequality> inequalities = new LinkedList<>();

    /*
     * this pattern matches 0 <= ( >= / > / < ) a_0 * x_0 + ... + a_n
     * or 0 <= ( >= / > / < ) a_1 * x_1 + ... + a_n
     *
     * group 1 -> y or 0
     * group 2 -> sign
     * group 3 -> coeffs
     */
    Pattern ineqPattern = Pattern.compile("^\\s*([y0])\\s*(<=|>=|<|>)(.*)$",
        Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    /*
    * this pattern matches coeffs of right part of inequality
    * like a_0 * x_0 + .. + x_n
    *
    * group1 -> coeff (a_i)
    * group3 -> coeffIndex (i from x_i)
    * */
    Pattern coeffsPattern = Pattern.compile("(\\+?\\-?\\s*\\d+\\s*)(\\*\\s*x_(\\d*))?\\s*",
        Pattern.CASE_INSENSITIVE);
    if(testData.matches(ineqPattern.pattern())) throw new IllegalArgumentException();
    Matcher matcher = ineqPattern.matcher(testData);
    while(matcher.find()) {
      // check if at the left "y" or "0"
      boolean isZeroConstraint = matcher.group(1).matches("\\s*y\\s*") ? !ZERO_CONSTRAINT : ZERO_CONSTRAINT;
      // check sigh of inequality
      Inequality.Sign sign = Inequality.Sign.fromString(matcher.group(2));

      if(matcher.group(3).matches(coeffsPattern.pattern())) throw new IllegalArgumentException();
      Matcher coeffsMatcher = coeffsPattern.matcher(matcher.group(3));

      Map<Integer, Double> coeffsMap = new TreeMap<>();
      while(coeffsMatcher.find()) {
        Double coeff = Double.valueOf(coeffsMatcher.group(1).replace(" ", ""));
        int coeffIndex = coeffsMatcher.group(3) != null ? Integer.parseInt(coeffsMatcher.group(3)) : -1;
        coeffsMap.put(coeffIndex, coeff);
      }

      double[] coeffs = new double[dimensions + 1];
      coeffsMap.forEach((index, coeff) -> {
        if(index != -1) coeffs[index] = coeff;
        else coeffs[coeffs.length - 1] = coeff;
      });

      inequalities.add(new Inequality(coeffs, sign, isZeroConstraint));
    }

    return inequalities.toArray(new Inequality[0]);
  }

  public double[][] readFromFile(String fileName) {
    try {
      return readFromFile(fileName, this::readFromString);
    } catch (IOException e) {
      e.printStackTrace();
      return new double[0][0];
    }
  }

  public <T> T readFromFile(String fileName, Function<String, T> readFromStrFunction) throws IOException {
      String lines = Files.lines(Paths.get(fileName))
          .reduce((before, next) -> before + lineSeparator + next)
          .orElse("");
      return readFromStrFunction.apply(lines);
  }

}
