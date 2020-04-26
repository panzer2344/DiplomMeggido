package solver;

import model.Inequality;

import java.util.Stack;

import static model.Inequality.Sign.*;

public class Solver2D {

  public Solver2D() {

  }


  /**
   * @param inequalityStack
   * @param leftBorder
   * @param rightBorder
   *
   * @return intersection of two first inequalities, if all of this two is suitable, else null
   *
   * for testing pairs, we will put all testable constraints into stack
   * then take two constraint, union them into pair, test this pair,
   *    if all is ok then move their intersection into list with intersection for future median finding
   *    if one of this two constraints not suitable for future finding (parallel ,of out of feasible x set), then
   *      drop this one, and another move to stack
   * */
  protected Double testPair(Stack<Inequality> inequalityStack, double leftBorder, double rightBorder) {
    Inequality first = inequalityStack.pop();
    Inequality second = inequalityStack.pop();

    // if all is suitable, then nonSuitable = null, else its = inequality, which we need to delete, another inequality must push
    Inequality nonSuitable;

    nonSuitable = getNonSuitableOnParallel(first, second);
    if(nonSuitable != null) {
      inequalityStack.push( nonSuitable.equals(first) ? second : first );
      return null;
    }

    nonSuitable = getNonSuitableOnFeasibility(first, second, leftBorder, rightBorder);
    if(nonSuitable != null) {
      inequalityStack.push( nonSuitable.equals(first) ? second : first );
      return null;
    }

    // if all is suitable, then we return their intersection
    return getIntersection(first, second);
  }

  /**
   * @param first
   * @param second
   * @return constraint, which become not suitable after test on parallel lines, or null if all is suitable
   * ex. test({ y >= x + 3.0 , y >= x + 5.0 }) => notSuitable={ y >= x + 3.0 } ,
   *   because this line is below the second one, and they are both bottom constraint
   * */
  protected Inequality getNonSuitableOnParallel(Inequality first, Inequality second) {
    if(first == null || second == null) throw new IllegalArgumentException();

    if(isTopConstraint(first) && isBottomConstraint(second)) throw new IllegalArgumentException();
    if(isBottomConstraint(first) && isTopConstraint(second)) throw new IllegalArgumentException();

    if( ! isParallel(first, second) ) return null;
    else return isBottomConstraint(first) ? getMostLower(first, second) : getMostUpper(first, second);
  }

  /**
   * @param first
   * @param second
   * @param leftBorder
   * @param rightBorder
   * @return constraint, which become not suitable after test on feasibility of intersection of lines, or null if all is suitable
   * if intersection is left to the left border, then we need to delete constraint with greater A coeff,
   * else if intersection is right to the right border, then we need to delete constraint with lower A coeff
   * ex. test({ y >= x + 3.0 , y >= -x - 1 }) => notSuitable = { y >= x + 3.0 }
   * */
  protected Inequality getNonSuitableOnFeasibility(Inequality first, Inequality second, double leftBorder, double rightBorder) {
    if(first == null || second == null) throw new IllegalArgumentException();

    if(isTopConstraint(first) && isBottomConstraint(second)) throw new IllegalArgumentException();
    if(isBottomConstraint(first) && isTopConstraint(second)) throw new IllegalArgumentException();

    double intersection = getIntersection(first, second);
    if(intersection > rightBorder) return getWithSmallerIncline(first, second);
    if(intersection < leftBorder) return getWithGreaterIncline(first, second);
    return null;
  }

  /**
   * @param zeros - zero constraints, which from we take left border
   * @return left X border (ex. getLeftBorder({x >= -2.0, x >= 5.0}) => 5.0)
   * */
  protected double getLeftBorder(Inequality[] zeros) {
    double leftBorder = Double.NEGATIVE_INFINITY;
    for(Inequality zero : zeros) leftBorder = isLeftConstraint(zero) ? Math.max(leftBorder, xFromZero(zero)) : leftBorder;
    return leftBorder;
  }

  /**
   * @param zeros - zero constraints, which from we take right border
   * @return right X border (ex. getRightBorder({x <= -2.0, x <= 5.0}) => -2.0)
   * */
  protected double getRightBorder(Inequality[] zeros) {
    double rightBorder = Double.POSITIVE_INFINITY;
    for(Inequality zero : zeros) rightBorder = isRightConstraint(zero) ? Math.min(rightBorder, xFromZero(zero)) : rightBorder;
    return rightBorder;
  }

  /**
   * @param zero - zero constraint
   * @return
   * */
  protected double xFromZero(Inequality zero) {
    return - zero.getFree() / zero.getA();
  }

  /**
   * @param zero - zero constraint
   * @return true, if its left constraint like x >= 2.0 (0 <= x - 2.0)
   * */
  protected boolean isLeftConstraint(Inequality zero) {
    return Double.compare(zero.getA(), 0) > 0 && (LESS.equals(zero.getSign()) || LESS_OR_EQUAL.equals(zero.getSign())) ||
        Double.compare(zero.getA(), 0) < 0 && (GREAT.equals(zero.getSign()) || GREAT_OR_EQUAL.equals(zero.getSign()));
  }

  /**
   * @param zero - zero constraint
   * @return true, if its right constraint like x <= -5.0 (0 >= x + 5.0)
   * */
  protected boolean isRightConstraint(Inequality zero) {
    return Double.compare(zero.getA(), 0) > 0 && (GREAT.equals(zero.getSign()) || GREAT_OR_EQUAL.equals(zero.getSign())) ||
        Double.compare(zero.getA(), 0) < 0 && (LESS.equals(zero.getSign()) || LESS_OR_EQUAL.equals(zero.getSign()));
  }

  /**
   * @return true if constraint like y >= -3.0 * x + 5.0
   * */
  protected boolean isBottomConstraint(Inequality constraint) {
    return GREAT.equals(constraint.getSign()) || GREAT_OR_EQUAL.equals(constraint.getSign());
  }

  /**
   * @return true if constraint like y <= -2.0 * x - 6.0
   * */
  protected boolean isTopConstraint(Inequality constraint) {
    return LESS.equals(constraint.getSign()) || LESS_OR_EQUAL.equals(constraint.getSign());
  }

  /**
   * need to use only for same type of constraints(bottom or top)
   * @return constraint with maximal value of free coeffs
   * ex. getMostUpper({ y <= x + 2.0, y <= x + 3.0 }) = { y <= x + 3.0 }
   * */
  protected Inequality getMostUpper(Inequality first, Inequality second) {
    return Double.compare(first.getFree(), second.getFree()) > 0 ? first : second;
  }

  /**
   * need to use only for same type of constraints(bottom or top)
   * @return constraint with minimal value of free coeffs
   * ex. getMostLower({ y >= x + 2.0, y >= x + 3.0 }) = { y >= x + 2.0 }
   * */
  protected Inequality getMostLower(Inequality first, Inequality second) {
    return Double.compare(first.getFree(), second.getFree()) > 0 ? second : first;
  }

  /**
   * @return true, if inequalities is parallel ( with equals A coeff )
   * */
  protected boolean isParallel(Inequality first, Inequality second) {
    return Double.compare(first.getA(), second.getA()) == 0;
  }

  /**
   * @param first - first inequality
   * @param second - second inequality
   * @return intersection of this two
   * */
  protected double getIntersection(Inequality first, Inequality second) {
    return (second.getFree() - first.getFree()) / (first.getA() - second.getA());
  }

  /**
   * @param first - first inequality
   * @param second - second inequality
   * @return inequality with greater incline ( A coeff )
   * ex. getWithGreaterIncline({ y >= x + 3 , y >= -x - 1 }) = { y >= x + 3 }
   * */
  protected Inequality getWithGreaterIncline(Inequality first, Inequality second) {
    return Double.compare(first.getA(), second.getA()) > 0 ? first : second;
  }

  /**
   * @param first - first inequality
   * @param second - second inequality
   * @return inequality with smaller  incline ( A coeff )
   * ex. getWithGreaterIncline({ y >= x + 3 , y >= -x - 1 }) = { y >= -x - 1 }
   * */
  protected Inequality getWithSmallerIncline(Inequality first, Inequality second) {
    return Double.compare(first.getA(), second.getA()) > 0 ? second : first;
  }

}
