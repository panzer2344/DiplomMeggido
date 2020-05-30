import com.sun.xml.internal.ws.util.StringUtils;
import data.reader.DataReader;
import model.LPTask;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import solver.Solver2D;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public void test1() throws IOException, URISyntaxException {
        String filename = "lp_task_test_data_1.txt";
        LPTask lpTask = new DataReader().readLPTask(new DataReader().readStringFromClasspath(filename));

        Pair<Double, Double> solution = new Solver2D().solve(lpTask);

        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        Assert.assertEquals(expected.getValue0(), solution.getValue0(), 0.0001);
        Assert.assertEquals(expected.getValue1(), solution.getValue1(), 0.0001);
    }

}
