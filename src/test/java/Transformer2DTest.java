import data.reader.DataReader;
import model.Inequality;
import model.LPTask;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import solver.transformer.Transformer2D;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @Test
    public void test5() throws URISyntaxException, IOException {
        String lpTaskString = Files.lines(Paths.get(
                this.getClass()
                        .getClassLoader()
                        .getResource("lp_task_test_data.txt")
                        .toURI()))
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");
        LPTask lpTask = new DataReader().readLPTask(lpTaskString);

        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{-1.0, -1.0}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1.0, -1.0}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1.0, 1.0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1.0, 1.0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)
        };

        Inequality[] actual = new Transformer2D().transform(lpTask);

        checkIneqsOnEqual(expected, actual);
    }

    @Test
    public void test3() {
        LPTask lpTask = new LPTask(1, 1, new double[0], new double[0], new double[0]);
        Pair<Double, Double> toTransformBack = new Pair<Double, Double>(1.0, 2.0);

        Pair<Double, Double> expected = new Pair<Double, Double>(1.0, 1.0);
        Pair<Double, Double> actual = Transformer2D.transformResultBack(lpTask, toTransformBack);

        Assert.assertEquals(expected.getValue0(), actual.getValue0());
        Assert.assertEquals(expected.getValue1(), actual.getValue1());
    }

    @Test
    public void test4() {
        LPTask lpTask = new LPTask(3, -7, new double[0], new double[0], new double[0]);
        Pair<Double, Double> toTransformBack = new Pair<Double, Double>(-7.0, 28.0);

        Pair<Double, Double> expected = new Pair<Double, Double>(-7.0, -7.0);
        Pair<Double, Double> actual = Transformer2D.transformResultBack(lpTask, toTransformBack);

        Assert.assertEquals(expected.getValue0(), actual.getValue0());
        Assert.assertEquals(expected.getValue1(), actual.getValue1());
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
