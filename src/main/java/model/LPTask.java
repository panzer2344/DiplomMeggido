package model;

import lombok.ToString;

/***
 * LP task:
 * min (ax + by), if
 *    a_i * x + b_i * y + c_i <= 0, i in {1,...,N}
 */
@ToString
public class LPTask {
    public double a;
    public double b;
    public double[] A;
    public double[] B;
    public double[] C;

    public LPTask(double a, double b, double[] A, double[] B, double[] C) {
        this.a = a;
        this.b = b;
        this.A = A;
        this.B = B;
        this.C = C;
    }
}
