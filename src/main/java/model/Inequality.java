package model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * looks like y sign(<= / >= / > / <) coeffs_1 * x_1 + ... + coeffs_(n-1) * x_(n-1) + coeffs_n (free coeff)
 * ex. y GREAT(>) 3x_1 + 2x_2 + 3
 *
 * */
@Data
@AllArgsConstructor
public class Inequality {

  private double[] coeffs;
  private Sign sign;
  private boolean isZeroConstraint;

  /**
   * 0 <= A * x + ... + Free
   * @return first coeff
   * */
  public double getA() {
    return coeffs[0];
  }

  /**
   * 0 <= A * x + ... + Free
   * @return free coeff
   * */
  public double getFree() {
    return coeffs[coeffs.length - 1];
  }

  /**
   * in R2 : y = A * x + Free
   * @param x - where Y will be computed
   * @return value of Y in X
   * */
  public double computeFuncR2(double x) {
    return coeffs[0] * x + coeffs[coeffs.length - 1];
  }

  public enum Sign {
    GREAT(">"),
    GREAT_OR_EQUAL(">="),
    LESS("<"),
    LESS_OR_EQUAL("<=");

    final String stringSign;

    Sign(String stringSign) {
      this.stringSign = stringSign;
    }

    /**
     * @param  sign - value like >= <= < >
     * @return Sign enum value corresponding to input or null, if input not from Sign
     * */
    public static Sign fromString(String sign) {
      for(Sign value : values()) {
        if(value.stringSign.equals(sign)) return value;
      }
      return null;
    }

  }

  public static final boolean ZERO_CONSTRAINT = true;

}
