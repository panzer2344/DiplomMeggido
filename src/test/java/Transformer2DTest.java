import model.Inequality;
import model.LPTask;
import org.junit.Assert;
import org.junit.Test;
import solver.transformer.Transformer2D;

import java.util.Arrays;

import static model.Inequality.Sign.GREAT_OR_EQUAL;
import static model.Inequality.Sign.LESS_OR_EQUAL;
import static model.Inequality.ZERO_CONSTRAINT;

public class Transformer2DTest {

    /**
     * LPTask: min(x + y) { x + y + 1 <= 0 ; x + y - 1 <= 0 ; x - y + 1 <= 0 ; x - y - 1 <= 0 ; -x + y + 1 <= 0; x + 1 <= 0 }
     * */
    @Test
    public void test1() {
        LPTask lpTask = new LPTask(1, 1,
                new double[]{ 1, 1, 1, 1, -1, 1},
                new double[]{ 1, 1, -1, -1, 1, 0},
                new double[]{ 1, -1, 1, -1, 1, 1});

        Transformer2D transformer = new Transformer2D();
        Inequality[] actual = transformer.transform(lpTask);

        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{0, -1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{0, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{2, 1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{2, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{2, -1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT)};

        checkIneqsOnEqual(expected, actual);
    }

    @Test
    public void test2() {
        LPTask lpTask = new LPTask(1, 1, new double[]{ 1, -1 }, new double[]{ 0, 0 }, new double[]{ -1, -1 });

        Inequality[] expected = new Inequality[] {
                new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT) };

        Inequality[] actual = new Transformer2D().transform(lpTask);

        checkIneqsOnEqual(expected, actual);
    }

    public static void checkIneqsOnEqual(Inequality[] expected, Inequality[] actual) {
        Assert.assertEquals(expected.length, actual.length);

        for(int i = 0; i < expected.length; i++) {
            Assert.assertArrayEquals("At " + i , expected[i].getCoeffs(), actual[i].getCoeffs(), 0.000001);
            Assert.assertEquals("At " + i , expected[i].getA(), actual[i].getA(), 0.0000001);
            Assert.assertEquals("At " + i , expected[i].getSign(), actual[i].getSign());
        }
    }

}
