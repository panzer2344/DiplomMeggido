import model.Inequality;
import model.LPTask;
import org.junit.Assert;
import org.junit.Test;
import solver.transformer.Transformer2D;
import util.LPTaskGenerator;

import static model.Inequality.Sign.GREAT_OR_EQUAL;
import static model.Inequality.Sign.LESS_OR_EQUAL;
import static model.Inequality.ZERO_CONSTRAINT;

public class LPTaskGeneratorTest {

    @Test
    public void test() {
        double a = 1;
        double b = 1;
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT)};

        LPTask lpTask = new LPTaskGenerator().generate(a, b, inequalities);
        Inequality[] transformedBack = new Transformer2D().transform(lpTask);

        Assert.assertArrayEquals(inequalities, transformedBack);
    }

}
