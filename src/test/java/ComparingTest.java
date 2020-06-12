import data.breeder.DataBreeder;
import model.Inequality;
import model.LPTask;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import solver.Solver2D;
import solver.Solver2DWithBruteforce;
import util.InequalitiesGenerator;
import util.LPTaskGenerator;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static model.Inequality.Sign.GREAT_OR_EQUAL;
import static model.Inequality.Sign.LESS_OR_EQUAL;
import static model.Inequality.ZERO_CONSTRAINT;

public class ComparingTest {

    /**
     * test of 3rd party library: apache_math .
     * It will be used for speed comparing. LinearOptimization from this library will compare with implementation from this project.
     *
     * Method, used in library: Simplex-method
     *
     * init test to meet this library:
     *
     * min ( x/10 + y )
     * {
     *     x - y <= 0
     *     -x - y <= 0
     * }
     *
     * expected: (0, 0)
     *
     * */
    @Test
    public void apacheMath_initTest() {
        OptimizationData[] lpTask = new OptimizationData[] {
                GoalType.MINIMIZE,
                new LinearObjectiveFunction(new double[]{ 1.0 / 10.0, 1 }, 0.0), //  y = x/10,
                new LinearConstraintSet(
                        new LinearConstraint(new double[]{1, -1}, Relationship.LEQ, 0), // x - y <= 0 ~ y >= x
                        new LinearConstraint(new double[]{-1, -1}, Relationship.LEQ, 0) // -x - y <= 0 ~ y >= -x
                )
        };
        PointValuePair pointValuePair = new SimplexSolver().optimize(lpTask);

        Assert.assertArrayEquals(new double[]{0.0, 0.0}, pointValuePair.getPoint(), 0.000001);
    }

    @Ignore
    @Test
    public void speedComparing_1() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{1, -2}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{2, -4}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{4, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{4, 7}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{2, 4}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)};
        Pair<Double, Double> expected = new Pair<>(-3.0, -5.0);

        comparingLoop(0, 1, inequalities, expected, 2000, 20, 0.5);
    }

    @Ignore
    @Test
    public void speedComparing_2() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{0, 0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT), // y <= 0
                new Inequality(new double[]{1, -9}, GREAT_OR_EQUAL, ZERO_CONSTRAINT), // x <= 9
                new Inequality(new double[]{-4, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -4 * x - 16
                new Inequality(new double[]{-3, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -3 * x - 12
                new Inequality(new double[]{-2, -12}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT), // y >= -2 * x - 12
                new Inequality(new double[]{-1, -16}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT) //  y >= -x - 16
        };
        Pair<Double, Double> expected = new Pair<>(9.0, -25.0);

        comparingLoop(0, 1, inequalities, expected, 2000, 20, 0.5);
    }

    @Ignore
    @Test
    public void speedComparing_3() {
        Inequality[] inequalities = new Inequality[]{
                new Inequality(new double[]{-1.0, -1.0}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1.0, -1.0}, GREAT_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{1.0, 1.0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT),
                new Inequality(new double[]{-1.0, 1.0}, LESS_OR_EQUAL, !ZERO_CONSTRAINT)
        };
        Pair<Double, Double> expected = new Pair<>(0.0, -1.0);

        comparingLoop(0, 1, inequalities, expected, 2000, 20, 0.5);
    }

    @Ignore
    @Test
    public void speedComparing_realistic_1(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 1500, 15, true);
    }

    @Ignore
    @Test
    public void speedCheck_only_Meggido(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 1000000, 50000, false);
    }

    @Ignore
    @Test
    public void speedCheck_only_Meggido_2(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 1000000, 10000, false);
    }

    @Ignore
    @Test
    public void speedComparing_realistic_2(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 2000, 20, true);
    }

    /*
    @Ignore
    @Test
    public void buildPointsForGraph() {
        String valuesStringMeggido = "2, 269, 126, 327, 343, 134, 152, 175, 205, 237, 249, 295, 282, 313, 370, 403, 334, 439, 537, 497, 579, 684, 628, 659, 731, 689, 774, 809, 932, 1009, 1172, 1135, 1045, 2869, 3135, 3266, 3076, 3690, 1247, 3368, 3693, 3404, 3617, 1603, 1519, 3411, 1673, 4049, 2020, 3268, 4525, 2062, 4912, 2046, 4830, 1997, 5219, 2466, 5230, 2251, 4884, 2367, 5802, 2600, 5582, 2642, 6206, 3101, 6350, 2715, 5792, 2963, 6504, 2836, 6148, 2990, 6022, 3141, 6224, 3225, 5349, 3347, 7228, 3674, 5932, 3382, 6104, 3369, 6864, 9117, 15390, 15128, 15668, 14871, 11118, 9802, 16938, 11354, 12305, 8481";
        String valuesStringSimplex = "17, 10, 10, 9, 9, 14, 25, 27, 43, 57, 61, 76, 86, 127, 122, 142, 174, 190, 193, 215, 235, 273, 287, 308, 366, 373, 418, 477, 460, 584, 521, 555, 715, 632, 670, 733, 762, 942, 808, 844, 937, 944, 1007, 1069, 1082, 1308, 1202, 1273, 1462, 1391, 1724, 1534, 1913, 1664, 2080, 1821, 2149, 2000, 2239, 2545, 2312, 2441, 2596, 2763, 2960, 3027, 3423, 3441, 3571, 3859, 3905, 4215, 3998, 4121, 4146, 4457, 4647, 4958, 5637, 5396, 5816, 5995, 6217, 6803, 6960, 7391, 7474, 8358, 8486, 8621, 9178, 9532, 9999, 10622, 11248, 11660, 12046, 15002, 16104, 14327";
        int rateStart = 1;
        int rateFinish = 1000000;
        //int rateFinish = 2000;
        //int rateFinish = 1500;
        int step = 10000;
        //int step = 50000;
        //int step = 20;
        //int step = 15;
        int countForRate = 8;
        //int countForRate = 8;

        StringBuilder resultBuilderMeggido = new StringBuilder();
        StringBuilder resultBuilderSimplex = new StringBuilder();

        String[] valuesMeggido = valuesStringMeggido.split(", ");
        String[] valuesSimplex = valuesStringSimplex.split(", ");
        for(int i = 0; i < valuesMeggido.length; i++) {
            long x = rateStart + i * step * countForRate;
            long y_Meggido = Long.parseLong(valuesMeggido[i]);
            long y_Simplex = Long.parseLong(valuesSimplex[i]);
            resultBuilderMeggido.append("(").append(x).append(";").append(y_Meggido).append(")");
            resultBuilderSimplex.append("(").append(x).append(";").append(y_Simplex).append(")");
        }

        System.out.println(resultBuilderMeggido.toString());
        System.out.println(resultBuilderSimplex.toString());
    }
     */

    private void comparingLoop(
            double a,
            double b,
            Inequality[] inputIneqs,
            Pair<Double, Double> expected,
            int maxRate,
            int step,
            double offset) {
        long[] meggidoTime = new long[maxRate / step];
        long[] simplexTime = new long[maxRate / step];

        for(int rate = 1; rate < maxRate; rate += step) {
            Inequality[] inequalities = new DataBreeder().breed(inputIneqs, rate, offset);

            LPTask lpTaskMeggido = new LPTaskGenerator().generate(a, b, inequalities);
            Solver2D meggidoSolver = new Solver2DWithBruteforce();

            long startTimeMeggido = new Date().getTime();
            Pair<Double, Double> solutionMeggido = meggidoSolver.solve(lpTaskMeggido);
            long endTimeMeggido = new Date().getTime();

            meggidoTime[rate / step] = endTimeMeggido - startTimeMeggido;

            Assert.assertEquals(expected.getValue0(), solutionMeggido.getValue0(), 0.000001);
            Assert.assertEquals(expected.getValue1(), solutionMeggido.getValue1(), 0.000001);

            SimplexSolver simplexSolver = new SimplexSolver();
            OptimizationData[] lpTaskSimplex = new OptimizationData[]{
                    GoalType.MINIMIZE,
                    new LinearObjectiveFunction(new double[]{a, b}, 0.0),
                    getLCSetFromInequalities(inequalities)
            };
            long startTimeSimplex = new Date().getTime();
            PointValuePair solutionSimplex = simplexSolver.optimize(lpTaskSimplex);
            long endTimeSimplex = new Date().getTime();

            simplexTime[rate / step] = endTimeSimplex - startTimeSimplex;

            Assert.assertEquals(expected.getValue0(), solutionSimplex.getPoint()[0], 0.000001);
            Assert.assertEquals(expected.getValue1(), solutionSimplex.getPoint()[1], 0.000001);
        }

        System.out.println("Meggido: " + Arrays.toString(meggidoTime));
        System.out.println("Simplex: " + Arrays.toString(simplexTime));
    }

    private void comparingLoop_realistic(InequalitiesGenerator generator, int maxRate, int step, boolean needSimplex) {
        long[] meggidoTime = new long[maxRate / step];
        long[] simplexTime = new long[maxRate / step];

        for(int rate = 1; rate < maxRate; rate += step) {
            Inequality[] top = generator.generate(30, -30, 5, 15, 3 * rate, LESS_OR_EQUAL, !ZERO_CONSTRAINT);
            Inequality[] bot = generator.generate(-30, 30, -5, -15, 3 * rate, GREAT_OR_EQUAL, !ZERO_CONSTRAINT);
            Inequality[] left = generator.generate(1, 1, 10, 15, rate, LESS_OR_EQUAL, ZERO_CONSTRAINT);
            Inequality[] right = generator.generate(1, 1, -10, -15, rate, GREAT_OR_EQUAL, ZERO_CONSTRAINT);

            Inequality[] allIneqs = generator.mergeIneqs(top, bot, left, right);
            Pair<Double, Double> expected = new Pair<>(0.0, -5.0);

            LPTask lpTaskMeggido = new LPTaskGenerator().generate(0, 1, allIneqs);
            Solver2D meggidoSolver = new Solver2DWithBruteforce();

            long startTimeMeggido = new Date().getTime();
            Pair<Double, Double> solutionMeggido = meggidoSolver.solve(lpTaskMeggido);
            long endTimeMeggido = new Date().getTime();

            meggidoTime[rate / step] = endTimeMeggido - startTimeMeggido;

            Assert.assertEquals(expected.getValue0(), solutionMeggido.getValue0(), 0.0000001);
            Assert.assertEquals(expected.getValue1(), solutionMeggido.getValue1(), 0.0000001);

            if(needSimplex) {

                SimplexSolver simplexSolver = new SimplexSolver();
                OptimizationData[] lpTaskSimplex = new OptimizationData[]{
                        GoalType.MINIMIZE,
                        //new LinearObjectiveFunction(new double[]{13, -5}, 0.0),
                        new LinearObjectiveFunction(new double[]{0, 1}, 0.0),
                        getLCSetFromInequalities(allIneqs)
                };
                long startTimeSimplex = new Date().getTime();
                PointValuePair solutionSimplex = simplexSolver.optimize(lpTaskSimplex);
                long endTimeSimplex = new Date().getTime();

                simplexTime[rate / step] = endTimeSimplex - startTimeSimplex;

                Assert.assertEquals(expected.getValue0(), solutionSimplex.getPoint()[0], 0.0000001);
                Assert.assertEquals(expected.getValue1(), solutionSimplex.getPoint()[1], 0.0000001);

            }

        }

        System.out.println("Meggido: " + Arrays.toString(meggidoTime));
        System.out.println("Simplex: " + Arrays.toString(simplexTime));
    }

    private LinearConstraintSet getLCSetFromInequalities(Inequality[] inequalities) {
        LinearConstraint[] linearConstraints = new LinearConstraint[inequalities.length];
        for(int i = 0; i < inequalities.length; i++) linearConstraints[i] = getLCFromInequality(inequalities[i]);
        return new LinearConstraintSet(linearConstraints);
    }

    private LinearConstraint getLCFromInequality(Inequality inequality) {
        double[] coeffs = new double[2];
        coeffs[0] = -inequality.getA();
        coeffs[1] = inequality.isZeroConstraint() ? 0 : 1;
        Relationship sign = inequality.getSign().isLessOrEqual() ? Relationship.LEQ : Relationship.GEQ;
        return new LinearConstraint(coeffs, sign, inequality.getFree());
    }


}
