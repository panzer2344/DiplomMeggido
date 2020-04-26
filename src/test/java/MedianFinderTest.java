import median.MedianFinder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MedianFinderTest {


  @Test
  public void test() {
    test(new double[] {7, 6, 9, 3, 2, 8, 1, 5, 4}, 5);
  }


  @Test
  public void test1() {
    test(new double[] {6, 3, 2, 1, 5, 4}, 4);
  }


  @Test
  public void test2() {
    test(new double[] {3, 2, 1, 4}, 3);
  }


  @Test
  public void test3() {
    test(new double[] {3, 2, 1}, 2);
  }


  @Test
  public void test4() {
    test(new double[] {6, 3, 2, 1, 5, 4, 8, 9, 10, 7}, 6);
  }

  @Test
  public void test5() {
    test(new double[] {15, 13, 12, 11, 14, 6, 3, 2, 1, 5, 4, 8, 9, 10, 7}, 8);
  }

  @Test
  public void test6() {
    test(new double[]{19, 20, 17, 18, 16, 15, 13, 12, 11, 14, 6, 3, 2, 1, 5, 4, 8, 9, 10, 7}, 11);
  }

  @Test
  public void test7() {
    double[] randomArray = getRandomArray(1000, -100, 100);

    double[] copy = Arrays.copyOf(randomArray, randomArray.length);
    Arrays.sort(copy);

    double expectedMedian = copy[copy.length / 2];

    test(randomArray, expectedMedian);
  }

  @Test
  public void test8() {
    double[] randomArray = getRandomArray(1001, -100, 100);

    double[] copy = Arrays.copyOf(randomArray, randomArray.length);
    Arrays.sort(copy);

    double expectedMedian = copy[copy.length / 2];

    test(randomArray, expectedMedian);
  }

  private static MedianFinder finder = new MedianFinder();

  public void test(double[] testArray, double expectedMedian) {
    double founded = finder.find(testArray);
    Assert.assertEquals(expectedMedian, founded, 0.00001);
  }

  public double[] getRandomArray(int size, double from, double to) {
    double[] result = new double[size];
    for(int i = 0; i < size; i++){
      result[i] = from + Math.random() * (to - from);
    }
    return result;
  }

}
