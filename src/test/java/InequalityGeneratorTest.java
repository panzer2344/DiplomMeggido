import model.Inequality;
import org.junit.Assert;
import org.junit.Test;
import util.InequalitiesGenerator;

import static model.Inequality.ZERO_CONSTRAINT;

public class InequalityGeneratorTest {

    @Test
    public void test1() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(1, -1, 1, 2, 3, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{1, 1}, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{0, 2}, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 1}, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    @Test
    public void test2() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(1, -1, 1, 2, 4, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{1, 1}, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1.0 / 3.0, 5.0 / 3.0}, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1 / 3.0, 5.0 / 3.0}, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1, 1}, Inequality.Sign.LESS_OR_EQUAL, !ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    @Test
    public void test3() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(-1, 1, -1, -2, 3, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{-1, -1}, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{0, -2}, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -1}, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    @Test
    public void test4() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(-1, 1, -1, -2, 4, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{-1, -1}, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1.0 / 3.0, -5.0 / 3.0}, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1 / 3.0, -5.0 / 3.0}, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -1}, Inequality.Sign.GREAT_OR_EQUAL, !ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    @Test
    public void test5() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(1, 1, 1, 2, 3, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{1, 1}, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 1.5}, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 2}, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    @Test
    public void test6() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(1, 1, 1, 2, 4, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{1, 1}, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 4.0 / 3.0}, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 5.0 / 3.0}, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, 2}, Inequality.Sign.LESS_OR_EQUAL, ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    @Test
    public void test7() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(1, 1, -1, -2, 3, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{1, -1}, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -1.5}, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -2}, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    @Test
    public void test8() {
        Inequality[] actual = new InequalitiesGenerator()
                .generate(1, 1, -1, -2, 4, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT);
        Inequality[] expected = new Inequality[]{
                new Inequality(new double[]{1, -1}, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -4.0 / 3.0}, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -5.0 / 3.0}, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT),
                new Inequality(new double[]{1, -2}, Inequality.Sign.GREAT_OR_EQUAL, ZERO_CONSTRAINT)};
        compareAndAssert(expected, actual);
    }

    private void compareAndAssert(Inequality[] expected, Inequality[] actual) {
        for(int i = 0; i < actual.length; i++) {
            Assert.assertArrayEquals(expected[i].getCoeffs(), actual[i].getCoeffs(), 0.0000001);
            Assert.assertEquals(expected[i].getSign(), actual[i].getSign());
            Assert.assertEquals(expected[i].isZeroConstraint(), actual[i].isZeroConstraint());
        }
    }

}
