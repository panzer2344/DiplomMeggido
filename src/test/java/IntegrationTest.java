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
import java.util.Arrays;

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
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 1, false);
    }

    @Test
    public void test_with_breeder_x2() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 2, false);
    }

    @Test
    public void test_with_breeder_x5() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 5, false);
    }

    @Test
    public void test_with_breeder_x6() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 6, false);
    }

    @Test
    public void test_with_breeder_x100() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 100, true);
    }

    @Test
    public void test_with_breeder_x1000() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 1000, true);
    }

    @Test
    public void test_with_breeder_x100000() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 100000, true);
    }

    /**
     * 1 000 000 is perhaps max size (in practice) for run this alg in test suites.
     * 10 000 000 . when run this size, java throw OutOfMemoryError: GC overhead, JavaHeapMemoryError: memory oversized etc.
     * */
    @Test
    public void test_with_breeder_x1000000() throws IOException, URISyntaxException {
        test("lp_task_test_data.txt", new Pair<>(0.0, -1.0), 1000000, true);
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
            return solver2D.solve(breeded);
        }

    }

}
