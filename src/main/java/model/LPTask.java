package model;

/***
 * LP task:
 * min (ax + by), if
 *    a_i * x + b_i * y + c_i <= 0, i in {1,...,N}
 */
public class LPTask {
    public double a;
    public double b;
    public double[] A;
    public double[] B;
    public double[] C;

    public LPTask(double a, double b, double[] a1, double[] b1, double[] c) {
        this.a = a;
        this.b = b;
        A = a1;
        B = b1;
        C = c;
    }
}
