import data.breeder.DataBreeder;
import model.Inequality;
import model.LPTask;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import solver.Solver2D;
import solver.Solver2DWithBruteforce;
import util.InequalitiesGenerator;
import util.LPTaskGenerator;

import java.util.Arrays;
import java.util.Date;

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

    @Test
    public void speedComparing_realistic_1(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 1500, 15, true);
    }

    @Test
    public void speedCheck_only_Meggido(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 1000000, 50000, false);
    }

    @Test
    public void speedCheck_only_Meggido_2(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 1000000, 10000, false);
    }

    @Test
    public void speedComparing_realistic_2(){
        InequalitiesGenerator generator = new InequalitiesGenerator();
        comparingLoop_realistic(generator, 2000, 20, true);
    }

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
