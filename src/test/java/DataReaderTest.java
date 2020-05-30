import data.reader.DataReader;
import model.Inequality;
import model.LPTask;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.Inequality.Sign.*;
import static model.Inequality.ZERO_CONSTRAINT;

public class DataReaderTest {

  String lineSeparator = System.getProperty("line.separator");

  @Test
  public void checkOneSimpleTestData_shouldEqToExpected() {
    String testData = "" +
        "1 1 1" + lineSeparator +
        "1 -1 2" + lineSeparator +
        "-1 1 3" + lineSeparator +
        "-1 -1 4";

    DataReader dataReader = new DataReader();
    double[][] data = dataReader.readFromString(testData);

    double[][] expected = new double[][]{
        {1, 1, 1},
        {1, -1, 2},
        {-1, 1, 3},
        {-1, -1, 4}
    };

    Assert.assertArrayEquals(expected, data);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkOnNotAllLinesCoefficientsCountEquals_shouldThrowIAE() {
    String testData = "" +
        "1 1 1" + lineSeparator +
        "1 -1 2 3" + lineSeparator +
        "-1 1 4 5 6" + lineSeparator +
        "-1 -1 7";

    DataReader dataReader = new DataReader();
    dataReader.readFromString(testData);
  }

  @Test
  public void checkReadFromFile_shouldEqToExpected() throws IOException {
    String testData = "" +
        "1 1 1" + lineSeparator +
        "1 -1 2" + lineSeparator +
        "-1 1 3" + lineSeparator +
        "-1 -1 4";

    Path filePath = Paths.get("test_data.test.txt");
    Files.delete(filePath);
    Files.write(filePath, testData.getBytes(), StandardOpenOption.CREATE);

    Files.lines(filePath).forEach(System.out::println);

    DataReader dataReader = new DataReader();
    double[][] data = dataReader.readFromFile(filePath.toString());

    double[][] expected = new double[][]{
        {1, 1, 1},
        {1, -1, 2},
        {-1, 1, 3},
        {-1, -1, 4}
    };

    Assert.assertArrayEquals(expected, data);
  }

  @Test
  public void checkReadIneqsFromString_shouldPass() throws Exception {
    String testData = "" +
        "y <= 1 * x_0 + 2 * x_1 + 3 * x_2 + 3" + lineSeparator +
        "y >= -1 * x_1 - 2 * x_3" + lineSeparator +
        "0 > -1 * x_0 + 1";

    Inequality[] expectedIneqs = new Inequality[3];
    expectedIneqs[0] = new Inequality(new double[]{1, 2, 3, 0, 3}, LESS_OR_EQUAL, !ZERO_CONSTRAINT);
    expectedIneqs[1] = new Inequality(new double[]{0, -1, 0, -2, 0}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    expectedIneqs[2] = new Inequality(new double[]{-1, 0, 0, 0, 1}, GREAT,ZERO_CONSTRAINT);

    DataReader dataReader = new DataReader();
    Inequality[] actualIneqs = dataReader.readIneqsFromString(testData, 4);

    Assert.assertArrayEquals(expectedIneqs, actualIneqs);
  }

  @Test
  public void justTestForJavaMatcher_shouldPass() {
    String testData = "" + lineSeparator +
        "y <= 1 * x_0 + 2 * x_1 + 3 * x_2 + 3" + lineSeparator +
        "y >= -1 * x_1 - 2 * x_3" + lineSeparator +
        "0 > -1 * x_0 + 1" + lineSeparator;

    List<String> matches = new LinkedList<>();

    Pattern ineqPattern = Pattern.compile("^\\s*([y0])\\s*(<=|>=|<|>)(.*)$",
        Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    Pattern coeffsPattern = Pattern.compile("(\\+?\\-?\\s*\\d+\\s*)(\\*\\s*x_(\\d*))?\\s*",
        Pattern.CASE_INSENSITIVE);
    if(testData.matches(ineqPattern.pattern())) throw new IllegalArgumentException();
    Matcher matcher = ineqPattern.matcher(testData);
    while(matcher.find()) {
      matches.add(matcher.group(1));
      matches.add(matcher.group(2));
      Matcher coeffsMatcher = coeffsPattern.matcher(matcher.group(3));
      while(coeffsMatcher.find()) {
        matches.add(coeffsMatcher.group(1));
        matches.add(coeffsMatcher.group(3));
      }

    }

    System.out.println(matches);

  }


  /**
   * from lp_task_test_data.txt
   *
   * min ( 1 * x + 3 * y )
   * -4 * x - 6 * y - 2 <= 0
   * - 6 * y - 2 <= 0
   * 6 * y + 2 <= 0
   * 4 * x + 6 * y + 2 <= 0
   *
   * that is equals to
   *
   * min Y
   * -2 * x - 2 * Y - 2 <= 0
   * 2 * x - 2 * Y - 2 <= 0
   * -2 * x + 2 * Y + 2 <= 0
   * 2 * x + 2 * Y + 2 <= 0
   *
   * that is equals to
   *
   * min Y
   * Y >= -1 * x - 1
   * Y >= 1 * x - 1
   * Y <= -1 * x + 1
   * Y <= 1 * x + 1
   * */
  @Test
  public void lpTask_test() throws URISyntaxException, IOException {
    String input = Files.lines(Paths.get(
            this.getClass()
                    .getClassLoader()
                    .getResource("lp_task_test_data.txt")
                    .toURI()))
            .reduce((s1, s2) -> s1 + "\n" + s2)
            .orElse("");

    LPTask lpTask = new DataReader().readLPTask(input);

    Assert.assertEquals(1, lpTask.a, 0.0001);
    Assert.assertEquals(3, lpTask.b, 0.0001);

    Assert.assertArrayEquals(new double[] {-4, 0, 0, 4}, lpTask.A, 0.0001);
    Assert.assertArrayEquals(new double[] {-6, -6, 6, 6}, lpTask.B, 0.0001);
    Assert.assertArrayEquals(new double[] {-2, -2, 2, 2}, lpTask.C, 0.0001);
  }

}
