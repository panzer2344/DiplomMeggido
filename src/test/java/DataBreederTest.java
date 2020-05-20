import data.breeder.DataBreeder;
import model.Inequality;
import org.junit.Test;

import static model.Inequality.Sign.GREAT_OR_EQUAL;
import static model.Inequality.Sign.LESS_OR_EQUAL;
import static model.Inequality.ZERO_CONSTRAINT;

// todo: javaDoc
// todo: some more tests
// todo: make DataBreeder for LPTask input
public class DataBreederTest {

    @Test
    public void test1() {
        Inequality[] original = new Inequality[] {
                new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT)};

        Inequality[] expected = new Inequality[] {
                new Inequality(new double[]{1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 2}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 2}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, -1}, GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, -2}, GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 1}, LESS_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 2}, LESS_OR_EQUAL, ZERO_CONSTRAINT)};

        int rate = 2;
        double offset = 1.0;

        Inequality[] result = new DataBreeder().breed(original, rate, offset);

        Transformer2DTest.checkIneqsOnEqual(expected, result);
    }

}
