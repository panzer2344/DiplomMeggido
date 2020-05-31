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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

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
