package data.breeder;

import model.Inequality;

import static model.Inequality.Sign.LESS;
import static model.Inequality.Sign.LESS_OR_EQUAL;

public class DataBreeder {

    /**
     * for all ineq in input make copy, with offset out from the feasible set
     * @param originalIneqs - input array of inequalities to breed
     * @param rate - number of offseted copies of the each ineq
     * @param offset - distance from original ineq to first offsetted copy
     * @return array of offsetted ineqs
     * */
    public Inequality[] breed(Inequality[] originalIneqs, int rate, double offset) {
        if(Double.compare(offset, 0) <= 0) throw new IllegalArgumentException("offset should be greater then 0");

        Inequality[] result = new Inequality[originalIneqs.length * rate];
        for(int i = 0; i < originalIneqs.length; i++) {
            Inequality originalIneq = originalIneqs[i];
            double sign = LESS_OR_EQUAL.equals(originalIneq.getSign()) || LESS.equals(originalIneq.getSign()) ? 1.0 : -1.0;
            for(int j = 0; j < rate; j++) {
                double[] coeffs = new double[]{ originalIneq.getA(), originalIneq.getFree() + sign * offset * j };
                Inequality offsettedCopy = new Inequality(coeffs, originalIneq.getSign(), originalIneq.isZeroConstraint());
                result[i * rate + j] = offsettedCopy;
            }
        }
        return result;
    }
}
