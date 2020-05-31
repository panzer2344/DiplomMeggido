package util;

import model.Inequality;
import model.LPTask;

public class LPTaskGenerator {

    // todo: make tests
    // todo: make description
    public LPTask generate(double a, double b, Inequality[] inequalities) {
        double[] A = new double[inequalities.length];
        double[] B = new double[inequalities.length];
        double[] C = new double[inequalities.length];

        for(int i = 0; i < inequalities.length; i++) {
            double a_i = inequalities[i].getA() - a * ( inequalities[i].isZeroConstraint() ? 0 : 1 );
            double b_i = -b * ( inequalities[i].isZeroConstraint() ? 0 : 1 );
            double c_i = inequalities[i].getFree();

            Inequality.Sign sign = inequalities[i].getSign();
            if(Inequality.Sign.LESS_OR_EQUAL.equals(sign) || Inequality.Sign.LESS.equals(sign)) {
                a_i *= -1;
                b_i *= -1;
                c_i *= -1;
            }

            A[i] = a_i;
            B[i] = b_i;
            C[i] = c_i;
        }

        return new LPTask(a, b, A, B, C);
    }

}
