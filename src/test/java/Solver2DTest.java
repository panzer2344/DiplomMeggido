import model.Inequality;
import org.junit.Assert;
import org.junit.Test;
import solver.Solver2D;

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

  private class Solver2DForTest extends Solver2D {
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
  }

}
