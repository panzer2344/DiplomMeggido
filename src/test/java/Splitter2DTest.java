import model.Inequality;
import org.junit.Assert;
import org.junit.Test;
import solver.splitter.Splitter2D;

import static model.Inequality.Sign.*;
import static model.Inequality.ZERO_CONSTRAINT;

public class Splitter2DTest {

  @Test
  public void test() {

    Inequality[] inequalities = {
        new Inequality(new double[]{1.0}, GREAT, !ZERO_CONSTRAINT),
        new Inequality(new double[]{-2.0}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
        new Inequality(new double[]{3.0}, LESS, !ZERO_CONSTRAINT),
        new Inequality(new double[]{-4.0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
        new Inequality(new double[]{5.0}, GREAT_OR_EQUAL, ZERO_CONSTRAINT),
        new Inequality(new double[]{-5.0}, LESS_OR_EQUAL, ZERO_CONSTRAINT)};

    Splitter2D.Splitted splitted = new Splitter2D().split(inequalities);

    Assert.assertNotNull(splitted);
    Assert.assertNotNull(splitted.getBottom());
    Assert.assertNotNull(splitted.getTop());
    Assert.assertNotNull(splitted.getZero());

    Assert.assertEquals(2, splitted.getBottom().length);
    Assert.assertEquals(2, splitted.getZero().length);
    Assert.assertEquals(2, splitted.getTop().length);

    Assert.assertArrayEquals(new Inequality[]{inequalities[0], inequalities[1]}, splitted.getBottom());
    Assert.assertArrayEquals(new Inequality[]{inequalities[2], inequalities[3]}, splitted.getTop());
    Assert.assertArrayEquals(new Inequality[]{inequalities[4], inequalities[5]}, splitted.getZero());
  }

}
