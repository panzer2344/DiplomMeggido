import data.breeder.DataBreeder;
import data.reader.DataReader;
import model.Inequality;
import model.LPTask;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import solver.Solver2D;
import solver.Solver2DWithBruteforce;
import solver.transformer.Transformer2D;
import util.LPTaskGenerator;

import java.io.IOException;
import java.net.URISyntaxException;

import static model.Inequality.Sign.GREAT_OR_EQUAL;
import static model.Inequality.Sign.LESS_OR_EQUAL;
import static model.Inequality.ZERO_CONSTRAINT;

public class IntegrationTest {

    /**
     *
     *  solve task from lp_task_test_data.txt
     *
     * */

    @Test
    public void test() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 1, false);
    }

    @Test
    public void test_with_breeder_x2() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 2, false);
    }

    @Test
    public void test_with_breeder_x5() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 5, false);
    }

    @Test
    public void test_with_breeder_x6() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 6, false);
    }

    @Test
    public void test_with_breeder_x100() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 100, true);
    }

    @Test
    public void test_with_breeder_x1000() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 1000, true);
    }

    @Test
    public void test_with_breeder_x100000() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 100000, true);
    }

    /**
     * 1 000 000 is perhaps max size (in practice) for run this alg in test suites.
     * 10 000 000 . when run this size, java throw OutOfMemoryError: GC overhead, JavaHeapMemoryError: memory oversized etc.
     * */
    @Test
    public void test_with_breeder_x1000000() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0 / 3), 1000000, true);
    }

    @Test
    public void test1() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 1, false);
    }

    @Test
    public void test1_with_breeder_x2() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 2, false);
    }

    @Test
    public void test1_with_breeder_x5() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 5, false);
    }

    @Test
    public void test1_with_breeder_x6() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 6, false);
    }

    @Test
    public void test1_with_breeder_x100() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 100, true);
    }

    @Test
    public void test1_with_breeder_x1000() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 1000, true);
    }

    @Test
    public void test1_with_breeder_x100000() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 100000, true);
    }

    @Test
    public void test1_with_breeder_x1000000() throws IOException, URISyntaxException {
        test("lp_task_test_data_1.txt", new Pair<>(0.0, -1.0), 1000000, true);
    }

    /**
     *
     * min Y
     * {
     *    Y >= -10 * x - 5
     *    Y >= -5 * x - 4
     *    Y >= 10 * x - 5
     *    Y >= 5 * x - 4
     *    Y <= -10 * x + 5
     *    Y <= -5 * x + 4
     *    Y <= 10 * x + 5
     *    Y <= 5 * x + 4
     * }
     *
     * */
    @Test
    public void test2() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{-10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 20, 0.5);
        Pair<Double, Double> expected = new Pair<>(0.0, -4.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test2_x9() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{-10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 9, 0.5);
        Pair<Double, Double> expected = new Pair<>(0.0, -4.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test2_x10() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{-10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 10, 0.5);
        Pair<Double, Double> expected = new Pair<>(0.0, -4.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test2_x100() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{-10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 100, 0.5);
        Pair<Double, Double> expected = new Pair<>(0.0, -4.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test2_x1000() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{-10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 100, 0.5);
        Pair<Double, Double> expected = new Pair<>(0.0, -4.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test2_1() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{-10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, -5}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{10, 5}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{5, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
        };

        Pair<Double, Double> actual = new Solver2DWithBruteforce().solve(inequalities);
        Pair<Double, Double> expected = new Pair<>(0.0, -4.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    /**
     * min Y
     * Y >= 1 * x − 2
     * Y >= 2 * x − 4
     * Y >= 4 * x − 12
     * Y <= 4 * x + 7
     * Y <= 2 * x + 4
     * */
    @Test
    public void test3_x1() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[] {1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1, 0.5);
        Pair<Double, Double> expected = new Pair<>(-3.0, -5.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test3_x10() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[] {1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 10, 0.5);
        Pair<Double, Double> expected = new Pair<>(-3.0, -5.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test3_x109() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[] {1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 109, 0.5);
        Pair<Double, Double> expected = new Pair<>(-3.0, -5.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test3_x1097() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[] {1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1097, 0.5);
        Pair<Double, Double> expected = new Pair<>(-3.0, -5.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    /**
     * min Y
     * Y >= 1 * x − 2
     * Y >= 2 * x − 4
     * Y >= 4 * x − 12
     * Y <= 4 * x + 7
     * Y <= 2 * x + 4
     *
     * Y = 14 * x - 27 * y
     *
     * x = X
     * y = ( Y - 14 * X ) / 27
     *
     * optimum = ( -3, 37 / 27 )
     *
     * */
    @Test
    public void test3_1_x1() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[] {1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(14, 27, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1, 0.5);
        Pair<Double, Double> expected = new Pair<>(-3.0, 37.0 / 27);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test3_1_x117() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[] {1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(14, 27, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 117, 0.5);
        Pair<Double, Double> expected = new Pair<>(-3.0, 37.0 / 27);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test3_1_x1013() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[] {1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[] {2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(14, 27, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1013, 0.5);
        Pair<Double, Double> expected = new Pair<>(-3.0, 37.0 / 27);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    /**
     * min y
     * {
     *  y <= 0
     *  x <= 9
     *  y >= -4 * x - 16
     *  y >= -3 * x - 12
     *  y >= -2 * x - 12
     *  y >= -x - 16
     * }
     *
     * expected (9, -25)
     * */
    @Test
    public void test4_x1() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, -25.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test4_x17() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 17, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, -25.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test4_x1013() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1013, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, -25.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test4_x9998() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(0, 1, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 9998, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, -25.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    /**
     * min ( -3 * x + 7 * y )
     * {
     *  y <= 0
     *  x <= 9
     *  y >= -4 * x - 16
     *  y >= -3 * x - 12
     *  y >= -2 * x - 12
     *  y >= -x - 16
     * }
     *
     * expected (9, -25)
     * expected (9, 2/7)
     * */
    @Test
    public void test4_1_x1() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(-3, 7, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, 2.0 / 7.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test4_1_x17() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(-3, 7, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 17, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, 2.0 / 7.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test4_1_x1013() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(-3, 7, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 1013, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, 2.0 / 7.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    @Test
    public void test4_1_x9998() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };

        LPTask lpTask = new LPTaskGenerator().generate(-3, 7, inequalities);
        Solver2DWithBreed solver2D = new Solver2DWithBreed(new Solver2DWithBruteforce());

        Pair<Double, Double> actual = solver2D.solve(lpTask, 9998, 0.5);
        Pair<Double, Double> expected = new Pair<>(9.0, 2.0 / 7.0);

        Assert.assertEquals(expected.getValue0(), actual.getValue0(), 0.001);
        Assert.assertEquals(expected.getValue1(), actual.getValue1(), 0.001);
    }

    /**
     *
     * take LPTask from file with
     * @param filename
     * then if
     * @param withBruteforce
     * is true, then use Solver2DWithBruteforce, else just Solver2D
     * then add more parallel ineqs with DataBreeder by
     * @param breedRate
     * After all compare result with
     * @param expected
     * then assert
     *
     * */
    protected void test(String filename, Pair<Double, Double> expected, int breedRate, boolean withBruteforce) throws IOException, URISyntaxException {
        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2DWithBreed(withBruteforce ? new Solver2DWithBruteforce() : new Solver2D())
                .solve(lpTask, breedRate, 0.5);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    private class Solver2DWithBreed {
        private DataBreeder dataBreeder = new DataBreeder();
        private Solver2D solver2D;

        public Solver2DWithBreed(Solver2D solver2D) {
            this.solver2D = solver2D;
        }

        public Pair<Double, Double> solve(LPTask lpTask, int rate, double offset) {
            Inequality[] inequalities = new Transformer2D().transform(lpTask);
            Inequality[] breeded = dataBreeder.breed(inequalities, rate, offset);
            Pair<Double, Double> result = solver2D.solve(breeded);
            return Transformer2D.transformResultBack(lpTask, result);
        }

    }

}
