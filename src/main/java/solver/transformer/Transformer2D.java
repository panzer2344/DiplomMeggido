package solver.transformer;

import model.Inequality;
import model.LPTask;

import static model.Inequality.Sign.GREAT_OR_EQUAL;
import static model.Inequality.Sign.LESS_OR_EQUAL;
import static model.Inequality.ZERO_CONSTRAINT;

// todo: add more tests
// todo: add description
// todo: add class, which will use this, and then use Solver2D
// todo: add method in DataReader, which will read LPTask from text
public class Transformer2D {

    /**
     * transform LPTask like: min(ax+b), if a_i * x + b_i * x + c_i <= 0, i in {1,...,N}
     * to min y, if Y >= ( <= / > / <) sigma_i * X + gamma_i ( 0 >= ( <= / > / < ) alpha_i * x + c_i )
     * @param lpTask LPTask to transform
     * @return array of inequalities from LPTask in form above
     * */
    // todo: add tests for this method
    public Inequality[] transform(LPTask lpTask) {
        if(lpTask.A.length != lpTask.B.length || lpTask.B.length != lpTask.C.length)
            throw new IllegalArgumentException("LPTask coeff arrays should have same length");
        Inequality[] inequalities = new Inequality[lpTask.A.length];
        for(int i = 0; i < inequalities.length; i++) inequalities[i] = transform(lpTask, i);
        return inequalities;
    }

    // todo: add tests for this method
    /**
     * transform inequality with index=indexOfIneq in LPTask
     * from ax + by + c <= 0 to Y <= sigma X + gamma
     * @param lpTask LP task to transform
     * @param indexOfIneq index of inequality in LPTask
     * @return inequality in form Y <= sigma X + gamma ( LPTask.criterion = min Y )
     * */
    public Inequality transform(LPTask lpTask, int indexOfIneq){
        double a = lpTask.a;
        double b = lpTask.b;

        if(Double.compare(b, 0) == 0) throw new IllegalArgumentException("LPTask dimension < 2");

        double a_i = lpTask.A[indexOfIneq];
        double b_i = lpTask.B[indexOfIneq];
        double c_i = lpTask.C[indexOfIneq];

        double beta = b_i / b;
        double alpha = a_i - a * beta;

        if(Double.compare(beta, 0) == 0) return new Inequality(new double[]{a_i, c_i}, GREAT_OR_EQUAL, ZERO_CONSTRAINT);

        double sigma = -alpha / beta;
        double gamma = -c_i / beta;

        Inequality.Sign sign = Double.compare(beta, 0) > 0 ? LESS_OR_EQUAL : GREAT_OR_EQUAL;

        return new Inequality(new double[]{sigma, gamma}, sign, !ZERO_CONSTRAINT);
    }

}
