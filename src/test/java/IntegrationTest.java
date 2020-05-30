import data.breeder.DataBreeder;
import data.reader.DataReader;
import model.Inequality;
import model.LPTask;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import solver.Solver2D;
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
        String filename = "lp_task_test_data.txt";

        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2D().solve(lpTask);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    @Test
    public void test_with_breeder_x5() throws IOException, URISyntaxException {
        String filename = "lp_task_test_data.txt";

        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2DWithBreed().solve(lpTask, 5, 0.5);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    @Test
    public void test_with_breeder_x6() throws IOException, URISyntaxException {
        String filename = "lp_task_test_data.txt";

        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2DWithBreed().solve(lpTask, 6, 0.5);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    @Test
    public void test1() throws IOException, URISyntaxException {
        String filename = "lp_task_test_data_1.txt";
        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2D().solve(lpTask);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    @Test
    public void test1_with_breeder_x2() throws IOException, URISyntaxException {
        String filename = "lp_task_test_data_1.txt";
        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2DWithBreed().solve(lpTask, 2, 0.5);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    @Test
    public void test1_with_breeder_x5() throws IOException, URISyntaxException {
        String filename = "lp_task_test_data_1.txt";
        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2DWithBreed().solve(lpTask, 5, 0.5);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    @Test
    public void test1_with_breeder_x6() throws IOException, URISyntaxException {
        String filename = "lp_task_test_data_1.txt";
        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2DWithBreed().solve(lpTask, 6, 0.5);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

    private class Solver2DWithBreed extends Solver2D {
        private DataBreeder dataBreeder = new DataBreeder();

        public Pair<Double, Double> solve(LPTask lpTask, int rate, double offset) {
            Inequality[] inequalities = new Transformer2D().transform(lpTask);

            System.out.println(Arrays.toString(inequalities));

            Inequality[] breeded = dataBreeder.breed(inequalities, rate, offset);

            System.out.println(Arrays.toString(breeded));

            return solve(breeded);
        }

    }

}
