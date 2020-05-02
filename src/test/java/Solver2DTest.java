import model.Inequality;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import solver.Solver2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static model.Inequality.Sign.*;
import static model.Inequality.ZERO_CONSTRAINT;

public class Solver2DTest {

  private final Solver2DForTest solver = new Solver2DForTest();

  /**
   * isLeftConstraint for 0 <= x + 1.0 => true
   * */
  @Test
  public void test() {
    Assert.assertTrue(solver.isLeftConstraint(new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isLeftConstraint for 0 <= -x + 1.0 => false
   * */
  @Test
  public void test1() {
    Assert.assertFalse(solver.isLeftConstraint(new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isLeftConstraint for 0 >= x + 1.0 => false
   * */
  @Test
  public void test2() {
    Assert.assertFalse(solver.isLeftConstraint(new Inequality(new double[]{1, 1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isLeftConstraint for 0 >= -x + 1.0 => true
   * */
  @Test
  public void test3() {
    Assert.assertTrue(solver.isLeftConstraint(new Inequality(new double[]{-1, 1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isLeftConstraint for 0 < x + 1.0 => true
   * */
  @Test
  public void test4() {
    Assert.assertTrue(solver.isLeftConstraint(new Inequality(new double[]{1, 1}, LESS, ZERO_CONSTRAINT)));
  }

  /**
   * isLeftConstraint for 0 < -x + 1.0 => false
   * */
  @Test
  public void test5() {
    Assert.assertFalse(solver.isLeftConstraint(new Inequality(new double[]{-1, 1}, LESS, ZERO_CONSTRAINT)));
  }

  /**
   * isLeftConstraint for 0 > x + 1.0 => false
   * */
  @Test
  public void test6() {
    Assert.assertFalse(solver.isLeftConstraint(new Inequality(new double[]{1, 1}, GREAT, ZERO_CONSTRAINT)));
  }

  /**
   * isLeftConstraint for 0 > -x + 1.0 => true
   * */
  @Test
  public void test7() {
    Assert.assertTrue(solver.isLeftConstraint(new Inequality(new double[]{-1, 1}, GREAT, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 <= x + 1.0 => false
   * */
  @Test
  public void test8() {
    Assert.assertFalse(solver.isRightConstraint(new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 <= -x + 1.0 => true
   * */
  @Test
  public void test9() {
    Assert.assertTrue(solver.isRightConstraint(new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 >= x + 1.0 => true
   * */
  @Test
  public void test10() {
    Assert.assertTrue(solver.isRightConstraint(new Inequality(new double[]{1, 1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 >= -x + 1.0 => false
   * */
  @Test
  public void test11() {
    Assert.assertFalse(solver.isRightConstraint(new Inequality(new double[]{-1, 1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 < x + 1.0 => false
   * */
  @Test
  public void test12() {
    Assert.assertFalse(solver.isRightConstraint(new Inequality(new double[]{1, 1}, LESS, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 < -x + 1.0 => true
   * */
  @Test
  public void test13() {
    Assert.assertTrue(solver.isRightConstraint(new Inequality(new double[]{-1, 1}, LESS, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 > x + 1.0 => true
   * */
  @Test
  public void test14() {
    Assert.assertTrue(solver.isRightConstraint(new Inequality(new double[]{1, 1}, GREAT, ZERO_CONSTRAINT)));
  }

  /**
   * isRightConstraint for 0 > -x + 1.0 => false
   * */
  @Test
  public void test15() {
    Assert.assertFalse(solver.isRightConstraint(new Inequality(new double[]{-1, 1}, GREAT, ZERO_CONSTRAINT)));
  }

  /**
   * xFromZero: 0 <= 2.0 * x + 4.0  ==> x=-2.0
   * */
  @Test
  public void test16() {
    Assert.assertEquals(-2.0, solver.xFromZero(new Inequality(new double[]{2.0, 4.0}, LESS_OR_EQUAL, ZERO_CONSTRAINT)), 0.0001);
  }

  /**
   * xFromZero: 0 >= -4.0 * x + 2.0  ==> x=0.5
   * */
  @Test
  public void test17() {
    Assert.assertEquals(0.5, solver.xFromZero(new Inequality(new double[]{-4.0, 2.0}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)), 0.0001);
  }

  /**
   * getLeftBorder: ex. getLeftBorder({ 0 <= 1.0 * x + 2.0 , 0 >= -2.0 * x + 2.0 , 0 >= 1.0 * x - 5.0})
   * its equal to getLeftBorder({ x >= -2.0 , x >= 1.0 , x <= 5.0 }) = 1.0
   * */
  @Test
  public void test18() {
    Inequality[] zeros = new Inequality[]{
        new Inequality(new double[]{1.0, 2.0}, LESS_OR_EQUAL, ZERO_CONSTRAINT),
        new Inequality(new double[]{-2.0, 2.0}, GREAT_OR_EQUAL, ZERO_CONSTRAINT),
        new Inequality(new double[]{1.0, -5.0}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)
    };

    Assert.assertEquals(1.0, solver.getLeftBorder(zeros), 0.0001);
  }

  /**
   * getRightBorder: ex. getRightBorder({ 0 <= 1.0 * x + 2.0 , 0 <= -2.0 * x + 6.0 , 0 >= 1.0 * x - 5.0})
   * its equal to getRightBorder({ x >= -2.0 , x <= 3.0 , x <= 5.0 }) = 3.0
   * */
  @Test
  public void test19() {
    Inequality[] zeros = new Inequality[]{
        new Inequality(new double[]{1.0, 2.0}, LESS_OR_EQUAL, ZERO_CONSTRAINT),
        new Inequality(new double[]{-2.0, 6.0}, LESS_OR_EQUAL, ZERO_CONSTRAINT),
        new Inequality(new double[]{1.0, -5.0}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)
    };

    Assert.assertEquals(3.0, solver.getRightBorder(zeros), 0.0001);
  }

  /**
   * getWithSmallerIncline: ex. getWithSmallerIncline({ y >= x + 3 , y >= -x - 1 }) = { y >= -x - 1 }
   * */
  @Test
  public void test20() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertEquals(second, solver.getWithSmallerIncline(first, second));
  }

  /**
  * getWithGreaterIncline: ex. getWithGreaterIncline({ y >= x + 3 , y >= -x - 1 }) = { y >= x + 3 }
  * */
  @Test
  public void test21() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertEquals(first, solver.getWithGreaterIncline(first, second));
  }

  /**
   * getIntersection: ex. getIntersection({ y >= x + 3 , y >= -x -1 }) = { x = -2 }
   * */
  @Test
  public void test22() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertEquals(-2, solver.getIntersection(first, second), 0.0001);
  }

  /**
   * isParallel: ex. isParallel({ y >= x + 3, y >= -x -1 }) = false
   * */
  @Test
  public void test23() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertFalse(solver.isParallel(first, second));
  }

  /**
   * isParallel: ex. isParallel({ y >= x + 3, y >= x - 1 }) = true
   * */
  @Test
  public void test24() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertTrue(solver.isParallel(first, second));
  }

  /**
   * getMostLower: ex. getMostLower({ y >= x + 3, y >= x - 1 }) = { y >= x - 1 }
   * */
  @Test
  public void test25() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertEquals(second, solver.getMostLower(first, second));
  }

  /**
   * getMostUpper: ex. getMostUpper({ y <= x + 3, y <= x - 1 }) = { y <= x + 3 }
   * */
  @Test
  public void test26() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertEquals(first, solver.getMostUpper(first, second));
  }

  /**
   * isTopConstraint: ex. isTopConstraint({ y <= x + 1 }) = true
   * */
  @Test
  public void test27() {
    Assert.assertTrue(solver.isTopConstraint(new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * isTopConstraint: ex. isTopConstraint({ y >= x + 1 }) = false
   * */
  @Test
  public void test28() {
    Assert.assertFalse(solver.isTopConstraint(new Inequality(new double[]{1, 1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * isTopConstraint: ex. isTopConstraint({ y >= -x + 1 }) = false
   * */
  @Test
  public void test29() {
    Assert.assertFalse(solver.isTopConstraint(new Inequality(new double[]{-1, 1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * isTopConstraint: ex. isTopConstraint({ y <= -x + 1 }) = true
   * */
  @Test
  public void test30() {
    Assert.assertTrue(solver.isTopConstraint(new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * isBottomConstraint: ex. isBottomConstraint({ y <= x + 1 }) = false
   * */
  @Test
  public void test31() {
    Assert.assertFalse(solver.isBottomConstraint(new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * isBottomConstraint: ex. isBottomConstraint({ y >= x + 1 }) = true
   * */
  @Test
  public void test32() {
    Assert.assertTrue(solver.isBottomConstraint(new Inequality(new double[]{1, 1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * isBottomConstraint: ex. isBottomConstraint({ y >= -x + 1 }) = true
   * */
  @Test
  public void test33() {
    Assert.assertTrue(solver.isBottomConstraint(new Inequality(new double[]{-1, 1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * isBottomConstraint: ex. isBottomConstraint({ y <= -x + 1 }) = false
   * */
  @Test
  public void test34() {
    Assert.assertFalse(solver.isBottomConstraint(new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)));
  }

  /**
   * getNonSuitableOnFeasibility: ex. getNonSuitableOnFeasibility({ y <= x + 3, y <= -x - 1 }, left = -1, right = +inf)
   *  = { y >= x + 3 }
   * */
  @Test
  public void test35() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    double left = -1;
    double right = Double.POSITIVE_INFINITY;

    Assert.assertEquals(first, solver.getNonSuitableOnFeasibility(first, second, left, right));
  }

  /**
   * getNonSuitableOnFeasibility: ex. getNonSuitableOnFeasibility({ y >= -x + 3, y >= x - 1 }, left = -inf, right = 1)
   *  = { y >= x + 3 }
   * */
  @Test
  public void test36() {
    Inequality first = new Inequality(new double[]{-1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    double left = Double.NEGATIVE_INFINITY;
    double right = 1;

    Assert.assertEquals(first, solver.getNonSuitableOnFeasibility(first, second, left, right));
  }

  /**
   * getNonSuitableOnParallel: ex. getNonSuitableOnParallel({ y >= x + 3, y >= x -1 }) = { y >= x - 1 }
   * */
  @Test
  public void test37() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertEquals(second, solver.getNonSuitableOnParallel(first, second));
  }

  /**
   * getNonSuitableOnParallel: ex. getNonSuitableOnParallel({ y <= x + 3, y <= x -1 }) = { y >= x + 3 }
   * */
  @Test
  public void test38() {
    Inequality first = new Inequality(new double[]{1, 3}, LESS_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{1, -1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertEquals(first, solver.getNonSuitableOnParallel(first, second));
  }

  /**
   * getNonSuitableOnParallel on nonParallel: ex. getNonSuitableOnParallel({ y >= x + 3, y >= -x -1 }) = null
   * */
  @Test
  public void test39() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Assert.assertNull(solver.getNonSuitableOnParallel(first, second));
  }

  /**
   * testPair: ex. testPair({ y >= x + 3, y >= -x - 1 }, left = -inf, right = +inf) = -2, stack=empty
   * */
  @Test
  public void test40() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Stack<Inequality> inequalityStack = new Stack<>();
    inequalityStack.push(first);
    inequalityStack.push(second);

    List<Inequality> nonSuitable = new LinkedList<>();

    double left = Double.NEGATIVE_INFINITY;
    double right = Double.POSITIVE_INFINITY;

    Double actualIntersection = solver.testPair(inequalityStack, nonSuitable, left, right);

    Assert.assertEquals(-2, actualIntersection, 0.0001);
    Assert.assertTrue(inequalityStack.empty());
    Assert.assertTrue(nonSuitable.isEmpty());
  }

  /**
   * testPair: ex. testPair({ y >= x + 3, y >= -x - 1 }, left = -1, right = +inf) = null, stack={ y >= -x -1 }
   * */
  @Test
  public void test41() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Stack<Inequality> inequalityStack = new Stack<>();
    inequalityStack.push(first);
    inequalityStack.push(second);

    List<Inequality> nonSuitable = new LinkedList<>();

    double left = -1;
    double right = Double.POSITIVE_INFINITY;

    Double actualIntersection = solver.testPair(inequalityStack, nonSuitable, left, right);

    Stack<Inequality> expectedStack = new Stack<>();
    expectedStack.push(second);

    List<Inequality> nonSuitableExpected = new LinkedList<>();
    nonSuitableExpected.add(first);

    Assert.assertNull(actualIntersection);
    Assert.assertFalse(inequalityStack.empty());
    Assert.assertEquals(expectedStack, inequalityStack);
    Assert.assertEquals(nonSuitableExpected, nonSuitable);
  }

  /**
   * testPair: ex. testPair({ y >= x + 3, y >= x - 1 }, left = -inf, right = +inf) = null, stack={ y >= x + 3 }
   * */
  @Test
  public void test42() {
    Inequality first = new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
    Inequality second = new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);

    Stack<Inequality> inequalityStack = new Stack<>();
    inequalityStack.push(first);
    inequalityStack.push(second);

    List<Inequality> nonSuitable = new LinkedList<>();

    double left = Double.NEGATIVE_INFINITY;
    double right = Double.POSITIVE_INFINITY;

    Double actualIntersection = solver.testPair(inequalityStack, nonSuitable, left, right);

    Stack<Inequality> expectedStack = new Stack<>();
    expectedStack.push(first);

    List<Inequality> nonSuitableExpected = new LinkedList<>();
    nonSuitableExpected.add(second);

    Assert.assertNull(actualIntersection);
    Assert.assertFalse(inequalityStack.empty());
    Assert.assertEquals(expectedStack, inequalityStack);
    Assert.assertEquals(nonSuitableExpected, nonSuitable);
  }

  /**
   * getIntersections: ex. getIntersections({ y >= x - 3, y >= -x - 1, y >= x - 1, y >= x + 3 }, left = -1.5, right = +inf)
   *  = { 1 }, nonSuitable = { y >= x + 3, y >= x - 1 }
   * */
  @Test
  public void test43() {
    Inequality[] inequalities = new Inequality[] {
            new Inequality(new double[]{1, -3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{1, 3}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};

    double leftBorder = -1.5;
    double rightBorder = Double.POSITIVE_INFINITY;

    List<Inequality> nonSuitable = new LinkedList<>();

    double[] actualIntersections = solver.getIntersections(inequalities, nonSuitable, leftBorder, rightBorder);
    double[] expectedIntersections = new double[]{ 1 };

    List<Inequality> nonSuitableExpected = new LinkedList<>();
    nonSuitableExpected.add(inequalities[2]);
    nonSuitableExpected.add(inequalities[3]);

    Assert.assertNotNull(actualIntersections);
    Assert.assertFalse(nonSuitable.isEmpty());
    Assert.assertArrayEquals(expectedIntersections, actualIntersections, 0.00001);
    Assert.assertEquals(nonSuitableExpected, nonSuitable);
  }

  /**
   * getMaxFunctionFeasibleValue: ex. getMaxFunctionFeasibleValue(  1, { y <= x + 1, y <= x + 2, y <= 2x + 10 } ) = 2
   * should return max func value in feasible Y set. There feasible set is only under y <= x + 1
   * */
  @Test
  public void test44() {
    Inequality[] topIneqs = new Inequality[]{
            new Inequality(new double[]{ 1, 1 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 2 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 2, 10 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};
    double x = 1;
    Assert.assertEquals(2, solver.getMaxFunctionFeasibleValue(x, topIneqs), 0.00001);
  }

  /**
   * getMaxFunctionFeasibleValue: ex. getMaxFunctionFeasibleValue(  1, { y >= x + 1, y >= x + 2, y >= 2x + 10 } ) = 12
   * should return max func value in feasible Y set. There feasible set is only above y <= 2x + 10
   * */
  @Test
  public void test45() {
    Inequality[] bottomIneqs = new Inequality[]{
            new Inequality(new double[]{ 1, 1 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 2 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 2, 10 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
    double x = 1;
    Assert.assertEquals(12, solver.getMinFunctionFeasibleValue(x, bottomIneqs), 0.00001);
  }

  /**
   * getFeasibleTopIneqsAtX: ex. getFeasibleTopIneqsAtX(0, { y <= x + 3, y <= -x + 3, y <= x + 4, y <= -x + 4 })
   * = { y <= x + 3, y <= -x + 3 }
   * */
  @Test
  public void test46() {
    Inequality[] topIneqs = new Inequality[]{
            new Inequality(new double[]{ 1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};
    Pair<Inequality, Inequality> expected = new Pair<>(topIneqs[0], topIneqs[1]);
    Assert.assertEquals(expected, solver.getFeasibleTopIneqsAtX(0, topIneqs));
  }

  /**
   * getFeasibleTopIneqsAtX: ex. getFeasibleTopIneqsAtX(0, { y <= x + 3, y <= x + 4, y <= -x + 4 })
   * = { y <= x + 3, y <= x + 3 }
   * */
  @Test
  public void test47() {
    Inequality[] topIneqs = new Inequality[]{
            new Inequality(new double[]{ 1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};
    Pair<Inequality, Inequality> expected = new Pair<>(topIneqs[0], topIneqs[0]);
    Assert.assertEquals(expected, solver.getFeasibleTopIneqsAtX(0, topIneqs));
  }

  /**
   * getFeasibleTopIneqsAtX: ex. getFeasibleTopIneqsAtX(0, { y <= -x + 3, y <= x + 4, y <= -x + 4 })
   * = { y <= -x + 3, y <= -x + 3 }
   * */
  @Test
  public void test48() {
    Inequality[] topIneqs = new Inequality[]{
            new Inequality(new double[]{ -1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};
    Pair<Inequality, Inequality> expected = new Pair<>(topIneqs[0], topIneqs[0]);
    Assert.assertEquals(expected, solver.getFeasibleTopIneqsAtX(0, topIneqs));
  }

  /**
   * getFeasibleBottomIneqsAtX: ex. getFeasibleBottomIneqsAtX(0, { y >= x + 3, y >= -x + 3, y >= x + 4, y >= -x + 4 })
   * = { y >= x + 4, y >= -x + 4 }
   * */
  @Test
  public void test49() {
    Inequality[] bottomIneqs = new Inequality[]{
            new Inequality(new double[]{ 1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
    Pair<Inequality, Inequality> expected = new Pair<>(bottomIneqs[2], bottomIneqs[3]);
    Assert.assertEquals(expected, solver.getFeasibleBottomIneqsAtX(0, bottomIneqs));
  }

  /**
   * getFeasibleBottomIneqsAtX: ex. getFeasibleBottomIneqsAtX(0, { y >= x + 3, y >= -x + 3, y >= x + 4 })
   * = { y >= x + 4, y >= x + 4 }
   * */
  @Test
  public void test50() {
    Inequality[] bottomIneqs = new Inequality[]{
            new Inequality(new double[]{ 1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
    Pair<Inequality, Inequality> expected = new Pair<>(bottomIneqs[2], bottomIneqs[2]);
    Assert.assertEquals(expected, solver.getFeasibleBottomIneqsAtX(0, bottomIneqs));
  }

  /**
   * getFeasibleBottomIneqsAtX: ex. getFeasibleBottomIneqsAtX(0, { y >= x + 3, y >= -x + 3, y >= -x + 4 })
   * = { y >= -x + 4, y >= -x + 4 }
   * */
  @Test
  public void test51() {
    Inequality[] bottomIneqs = new Inequality[]{
            new Inequality(new double[]{ 1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
    Pair<Inequality, Inequality> expected = new Pair<>(bottomIneqs[2], bottomIneqs[2]);
    Assert.assertEquals(expected, solver.getFeasibleBottomIneqsAtX(0, bottomIneqs));
  }

  /**
   * getMaxFunctionFeasibleValue: ex. getMaxFunctionFeasibleValue(0, { y <= x + 4, y <= -x + 4}) = 4
   * */
  @Test
  public void test52() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(4, solver.getMaxFunctionFeasibleValue(0, inequalityPair), 0.00001);
  }

  /**
   * getMaxFunctionFeasibleValue: ex. getMaxFunctionFeasibleValue(0, { y <= x + 4, y <= x + 4 }) = 4
   * */
  @Test
  public void test53() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 4 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(4, solver.getMaxFunctionFeasibleValue(0, inequalityPair), 0.00001);
  }

  /**
   * getMinFunctionFeasibleValue: ex. getMinFunctionFeasibleValue(0, { y >= x + 4, y >= -x + 4}) = 4
   * */
  @Test
  public void test54() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(4, solver.getMaxFunctionFeasibleValue(0, inequalityPair), 0.00001);
  }

  /**
   * getMinFunctionFeasibleValue: ex. getMinFunctionFeasibleValue(0, { y >= x + 4, y >= x + 4}) = 4
   * */
  @Test
  public void test55() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 4 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(4, solver.getMinFunctionFeasibleValue(0, inequalityPair), 0.00001);
  }

  /**
   * getLeftBottomIncline( 2 bottom ineq ):
   * ex. getLeftBottomIncline({ y >= 2x - 5, y >= -2x - 5 }) = -2
   * */
  @Test
  public void test56() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(-2, solver.getLeftBottomIncline(inequalityPair), 0.00001);
  }

  /**
   * getLeftBottomIncline( 1 bottom ineq ):
   * ex. getLeftBottomIncline({ y >= 2x - 5, y >= 2x - 5 }) = 2
   * */
  @Test
  public void test57() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(2, solver.getLeftBottomIncline(inequalityPair), 0.00001);
  }

  /**
   * getRightBottomIncline( 2 bottom ineq ):
   * ex. getRightBottomIncline({ y >= 2x - 5, y >= -2x - 5 }) = 2
   * */
  @Test
  public void test58() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(2, solver.getRightBottomIncline(inequalityPair), 0.00001);
  }

  /**
   * getRightBottomIncline( 1 bottom ineq ):
   * ex. getRightBottomIncline({ y >= -2x - 5, y >= -2x - 5 }) = -2
   * */
  @Test
  public void test59() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ -2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(-2, solver.getRightBottomIncline(inequalityPair), 0.00001);
  }

  /**
   * getLeftTopIncline( 2 bottom ineq ):
   * ex. getLeftTopIncline({ y <= 2x - 5, y <= -2x - 5 }) = 2
   * */
  @Test
  public void test60() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(2, solver.getLeftTopIncline(inequalityPair), 0.00001);
  }

  /**
   * getLeftTopIncline( 1 bottom ineq ):
   * ex. getLeftTopIncline({ y <= -2x - 5, y <= -2x - 5 }) = -2
   * */
  @Test
  public void test61() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ -2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ -2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(-2, solver.getLeftTopIncline(inequalityPair), 0.00001);
  }

  /**
   * getRightTopIncline( 2 bottom ineq ):
   * ex. getRightTopIncline({ y <= -2x - 5, y <= 2x - 5 }) = -2
   * */
  @Test
  public void test62() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ -2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(-2, solver.getRightTopIncline(inequalityPair), 0.00001);
  }

  /**
   * getRightTopIncline( 1 bottom ineq ):
   * ex. getRightTopIncline({ y <= 2x - 5, y <= 2x - 5 }) = 2
   * */
  @Test
  public void test63() {
    Pair<Inequality, Inequality> inequalityPair = new Pair<>(
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 2, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertEquals(2, solver.getRightTopIncline(inequalityPair), 0.00001);
  }

  /**
   * isFeasible( input: min and max Y feasible values)
   * ex. isFeasible(-1, 2) = true
   * */
  @Test
  public void test64() {
    Assert.assertTrue(solver.isFeasible(-1, 2));
  }

  /**
   * isFeasible( input: min and max Y feasible values)
   * ex. isFeasible(2, -1) = false
   * */
  @Test
  public void test65() {
    Assert.assertFalse(solver.isFeasible(2, -1));
  }

  /**
   * isFeasible( input: x, and two pairs of ineqs - one at bottom border intersect at X, second - from top)
   * ex. isFeasible(0, { y >= -x - 3, y >= x - 3 }, { y <= -x + 3, y <= x + 3}) = true
   * */
  @Test
  public void test66() {
    Pair<Inequality, Inequality> bottomBorderPair = new Pair<>(
            new Inequality(new double[]{ -1, -3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, -3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Pair<Inequality, Inequality> topBorderPair = new Pair<>(
            new Inequality(new double[]{ -1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertTrue(solver.isFeasible(0, bottomBorderPair, topBorderPair));
  }

  /**
   * isFeasible( input: x, and two pairs of ineqs - one at bottom border intersect at X, second - from top)
   * ex. isFeasible(0, { y >= -x + 3, y >= x + 3 }, { y <= -x - 3, y <= x - 3}) = false
   * */
  @Test
  public void test67() {
    Pair<Inequality, Inequality> bottomBorderPair = new Pair<>(
            new Inequality(new double[]{ -1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT));
    Pair<Inequality, Inequality> topBorderPair = new Pair<>(
            new Inequality(new double[]{ -1, -3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, -3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT));
    Assert.assertFalse(solver.isFeasible(0, bottomBorderPair, topBorderPair));
  }

  /**
   * isFeasible( input x, and two sets of ineqs - top and border)
   * ex. isFeasible(0, { y >= -x - 3, y >= x - 3, y >= x - 5 }, { y <= -x + 3, y <= x + 3, y <= x + 5 }) = true
   * */
  @Test
  public void test68() {
    Inequality[] bottomInequalities = new Inequality[]{
            new Inequality(new double[]{ -1, -3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, -3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, -5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
    Inequality[] topInequalities = new Inequality[]{
            new Inequality(new double[]{ -1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 5 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

    Assert.assertTrue(solver.isFeasible(0, bottomInequalities, topInequalities));
  }

  /**
   * isFeasible( input x, and two sets of ineqs - top and border)
   * ex. isFeasible(0, { y >= -x + 3, y >= x + 3, y >= x + 5 }, { y <= -x - 3, y <= x - 3, y <= x - 5 }) = false
   * */
  @Test
  public void test69() {
    Inequality[] bottomInequalities = new Inequality[]{
            new Inequality(new double[]{ -1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 3 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, 5 }, GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
    Inequality[] topInequalities = new Inequality[]{
            new Inequality(new double[]{ -1, -3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, -3 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
            new Inequality(new double[]{ 1, -5 }, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

    Assert.assertFalse(solver.isFeasible(0, bottomInequalities, topInequalities));
  }

  private static class Solver2DForTest extends Solver2D {
    @Override
    protected double getMaxFunctionFeasibleValue(double x, Inequality[] topIneqs) {
      return super.getMaxFunctionFeasibleValue(x, topIneqs);
    }

    @Override
    protected double getMinFunctionFeasibleValue(double x, Inequality[] bottomIneqs) {
      return super.getMinFunctionFeasibleValue(x, bottomIneqs);
    }

    @Override
    public double getLeftBorder(Inequality[] zeros) {
      return super.getLeftBorder(zeros);
    }

    @Override
    public double getRightBorder(Inequality[] zeros) {
      return super.getRightBorder(zeros);
    }

    @Override
    public double xFromZero(Inequality zero) {
      return super.xFromZero(zero);
    }

    @Override
    public boolean isLeftConstraint(Inequality zero) {
      return super.isLeftConstraint(zero);
    }

    @Override
    public boolean isRightConstraint(Inequality zero) {
      return super.isRightConstraint(zero);
    }

    @Override
    public double[] getIntersections(Inequality[] inequalities, List<Inequality> nonSuitable, double leftBorder, double rightBorder) {
      return super.getIntersections(inequalities, nonSuitable, leftBorder, rightBorder);
    }

    @Override
    protected Double testPair(Stack<Inequality> inequalityStack, List<Inequality> nonSuitableIneqs, double leftBorder, double rightBorder) {
      return super.testPair(inequalityStack, nonSuitableIneqs, leftBorder, rightBorder);
    }

    @Override
    protected Inequality getNonSuitableOnParallel(Inequality first, Inequality second) {
      return super.getNonSuitableOnParallel(first, second);
    }

    @Override
    protected Inequality getNonSuitableOnFeasibility(Inequality first, Inequality second, double leftBorder, double rightBorder) {
      return super.getNonSuitableOnFeasibility(first, second, leftBorder, rightBorder);
    }

    @Override
    protected boolean isBottomConstraint(Inequality constraint) {
      return super.isBottomConstraint(constraint);
    }

    @Override
    protected boolean isTopConstraint(Inequality constraint) {
      return super.isTopConstraint(constraint);
    }

    @Override
    protected Inequality getMostUpper(Inequality first, Inequality second) {
      return super.getMostUpper(first, second);
    }

    @Override
    protected Inequality getMostLower(Inequality first, Inequality second) {
      return super.getMostLower(first, second);
    }

    @Override
    protected boolean isParallel(Inequality first, Inequality second) {
      return super.isParallel(first, second);
    }

    @Override
    protected double getIntersection(Inequality first, Inequality second) {
      return super.getIntersection(first, second);
    }

    @Override
    protected Inequality getWithGreaterIncline(Inequality first, Inequality second) {
      return super.getWithGreaterIncline(first, second);
    }

    @Override
    protected Inequality getWithSmallerIncline(Inequality first, Inequality second) {
      return super.getWithSmallerIncline(first, second);
    }

    @Override
    protected double getLeftBottomIncline(Pair<Inequality, Inequality> bottomIneqsAtX) {
      return super.getLeftBottomIncline(bottomIneqsAtX);
    }

    @Override
    protected double getRightBottomIncline(Pair<Inequality, Inequality> bottomIneqsAtX) {
      return super.getRightBottomIncline(bottomIneqsAtX);
    }

    @Override
    protected double getLeftTopIncline(Pair<Inequality, Inequality> topIneqsAtX) {
      return super.getLeftTopIncline(topIneqsAtX);
    }

    @Override
    protected double getRightTopIncline(Pair<Inequality, Inequality> topIneqsAtX) {
      return super.getRightTopIncline(topIneqsAtX);
    }

    @Override
    protected double getMinFunctionFeasibleValue(double x, Pair<Inequality, Inequality> bottomIneqsAtX) {
      return super.getMinFunctionFeasibleValue(x, bottomIneqsAtX);
    }

    @Override
    protected double getMaxFunctionFeasibleValue(double x, Pair<Inequality, Inequality> topIneqsAtX) {
      return super.getMaxFunctionFeasibleValue(x, topIneqsAtX);
    }

    @Override
    protected Pair<Inequality, Inequality> getFeasibleTopIneqsAtX(double x, Inequality[] topIneqs) {
      return super.getFeasibleTopIneqsAtX(x, topIneqs);
    }

    @Override
    protected Pair<Inequality, Inequality> getFeasibleBottomIneqsAtX(double x, Inequality[] bottomIneqs) {
      return super.getFeasibleBottomIneqsAtX(x, bottomIneqs);
    }

    @Override
    protected boolean isFeasible(double x, Inequality[] bottomIneqs, Inequality[] topIneqs) {
      return super.isFeasible(x, bottomIneqs, topIneqs);
    }

    @Override
    protected boolean isFeasible(double x, Pair<Inequality, Inequality> bottomIneqsAtX, Pair<Inequality, Inequality> topIneqsAtX) {
      return super.isFeasible(x, bottomIneqsAtX, topIneqsAtX);
    }

    @Override
    protected boolean isFeasible(double minFuncFeasibleValue, double maxFuncFeasibleValue) {
      return super.isFeasible(minFuncFeasibleValue, maxFuncFeasibleValue);
    }
  }

}
