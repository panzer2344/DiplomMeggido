package solver;

import median.MedianFinder;
import model.Inequality;
import model.LPTask;
import org.javatuples.Pair;
import solver.splitter.Splitter2D;
import solver.transformer.Transformer2D;

import java.util.*;

import static model.Inequality.Sign.*;

public class Solver2D {

  private final MedianFinder medianFinder = new MedianFinder();

  private Inequality[] top = null;
  private Inequality[] bot = null;
  private double leftBorder = Double.NEGATIVE_INFINITY;
  private double rightBorder = Double.POSITIVE_INFINITY;

  private final double EPSILON = Math.pow(10, -15);

  public Solver2D() {}

  /**
   * TODO: make tests
   * */
  public Pair<Double, Double> solve(LPTask lpTask) {
    return solve(new Transformer2D().transform(lpTask));
  }

  public Pair<Double, Double> solve(Inequality[] inequalities){
    Splitter2D.Splitted splitted = new Splitter2D().split(inequalities);

    leftBorder = getLeftBorder(splitted.getZero());
    rightBorder = getRightBorder(splitted.getZero());

    top = splitted.getTop();
    bot = splitted.getBottom();

    return recursiveSolve();
  }

  // updated[17.05.2020] : added dropping top inequalities actions
  /*** TODO : need to test this action, and write some more integration tests for all R^2 algo */
  protected Pair<Double, Double> recursiveSolve() {
    if(bot.length <= 1) return bruteForceSolve(top, bot, leftBorder, rightBorder);

    Set<Inequality> nonSuitable = new HashSet<>();
    double[] botIntersections = getIntersections(bot, nonSuitable, leftBorder, rightBorder);
    bot = removeFromArray(bot, nonSuitable);

    nonSuitable.clear();
    double[] topIntersections = getIntersections(top, nonSuitable, leftBorder, rightBorder);
    top = removeFromArray(top, nonSuitable);

    double[] intersections = mergeArrays(botIntersections, topIntersections);

    double median = medianFinder.find(intersections);

    Pair<Inequality, Inequality> bottomIneqsAtX = getFeasibleBottomIneqsAtX(median, bot);
    Pair<Inequality, Inequality> topIneqsAtX = getFeasibleTopIneqsAtX(median, top);

    double minFuncFeasibleValue = getMinFunctionFeasibleValue(median, bottomIneqsAtX);
    double maxFuncFeasibleValue = getMaxFunctionFeasibleValue(median, topIneqsAtX);

    double leftBottomIncline = getLeftBottomIncline(bottomIneqsAtX);
    double rightBottomIncline = getRightBottomIncline(bottomIneqsAtX);
    //double leftTopIncline = getLeftTopIncline(topIneqsAtX);
    //double rightTopInline = getRightTopIncline(topIneqsAtX);

    if (isFeasible(minFuncFeasibleValue, maxFuncFeasibleValue)) {
      if (isOptimum(leftBottomIncline, rightBottomIncline)) {
        double y = bottomIneqsAtX.getValue0().computeFuncR2(median);
        return new Pair<>(median, y);
      } else {
        if (isOptimumOnLeft(leftBottomIncline)) {
            rightBorder = median - EPSILON;
        } else if (isOptimumOnRight(rightBottomIncline)) {
            leftBorder = median + EPSILON;
        }
      }
    } /* else {
        if ( isFeasibleOnLeft(leftBottomIncline, leftTopIncline) ) {
            rightBorder = median;
        } else if ( isFeasibleOnRight(rightBottomIncline, rightTopInline) ) {
            leftBorder = median;
        }
    } */

    return recursiveSolve();
  }


  /**
   * works correctly only if there are one bot inequality
   * */
  protected Pair<Double, Double> bruteForceSolve(Inequality[] top, Inequality[] bot, double leftBorder, double rightBorder) {
    double min = Double.POSITIVE_INFINITY;
    // merge two arrays into one common
    Inequality[] allIneqs = new Inequality[top.length + bot.length];
    System.arraycopy(top, 0, allIneqs, 0, top.length);
    System.arraycopy(bot, 0, allIneqs, top.length, bot.length);

    // compute all intersections and find minimum from all of this
    double resultX = Double.NaN;
    for(int i = 0; i < allIneqs.length; i++) {
      for(int j = i + 1; j < allIneqs.length; j++) {
        Inequality firstCandidate = allIneqs[i];
        Inequality secondCandidate = allIneqs[j];

        double intersection = getIntersection(firstCandidate, secondCandidate);
        double y = firstCandidate.computeFuncR2(intersection);

        // if in feasible set, then try to exchange minimum
        if(intersection >= leftBorder && intersection <= rightBorder) {
          if(y < min) {
            min = y;
            resultX = intersection;
          }
        }
      }
    }

    // if no minimum founded in feasible set among intersections
    if(Double.isNaN(resultX)) {
      // then try to exchange minimum by border values
      for (Inequality inequality : allIneqs) {
        double onLeft = inequality.computeFuncR2(leftBorder);
        double onRight = inequality.computeFuncR2(rightBorder);
        // firstly compare values on borders
        // then compare winner with min
        if (onLeft < onRight) {
          if (onLeft < min) {
            min = onLeft;
            resultX = leftBorder;
          }
        } else {
          if (onRight < min) {
            min = onRight;
            resultX = rightBorder;
          }
        }
      }
    }

    return new Pair<>(resultX, min);
  }

  /**
   * @param inequalities input inequalities array, for splitting and filtering
   * @param leftBorder leftBorder of feasible set
   * @param rightBorder rightBorder of feasible set
   * @param nonSuitable array for outputting, contains all inequalities, that marked non suitable
   * split by pairs and delete some inequalities for suitable and move nonSuitable from input array to output list parameter in runtime
   * @return array of intersections
   * */
  protected double[] getIntersections(Inequality[] inequalities, Collection<Inequality> nonSuitable, double leftBorder, double rightBorder) {
    // create list for storing result intersections
    List<Double> intersections = new LinkedList<>();
    // stack for inequalities, used for splitting on pairs
    Stack<Inequality> inequalityStack = new Stack<>();
    for(Inequality inequality : inequalities) inequalityStack.push(inequality);
    // test pairs . add all testPair output, null values too
    while(inequalityStack.size() > 1)  intersections.add(testPair(inequalityStack, nonSuitable, leftBorder, rightBorder));
    // filter intersection on non null values, then map list to array and return
    return intersections.stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue).toArray();
  }

  /**
   * @param inequalityStack
   * @param nonSuitableIneqs
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
  protected Double testPair(Stack<Inequality> inequalityStack, Collection<Inequality> nonSuitableIneqs, double leftBorder, double rightBorder) {
    Inequality first = inequalityStack.pop();
    Inequality second = inequalityStack.pop();

    // if all is suitable, then nonSuitable = null, else its = inequality, which we need to delete, another inequality must push
    Inequality nonSuitable;

    nonSuitable = getNonSuitableOnParallel(first, second);
    if(nonSuitable != null) {
      nonSuitableIneqs.add(nonSuitable);
      inequalityStack.push( nonSuitable.equals(first) ? second : first );
      return null;
    }

    nonSuitable = getNonSuitableOnFeasibility(first, second, leftBorder, rightBorder);
    if(nonSuitable != null) {
      nonSuitableIneqs.add(nonSuitable);
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
   * if intersection is left to the left border, then we need to delete constraint with lower A coeff,
   * else if intersection is right to the right border, then we need to delete constraint with greater A coeff
   * ex. test({ y >= x + 3.0 , y >= -x - 1 }) => notSuitable = { y >= -x - 1 }
   * */
  protected Inequality getNonSuitableOnFeasibility(Inequality first, Inequality second, double leftBorder, double rightBorder) {
    if(first == null || second == null) throw new IllegalArgumentException();

    if(isTopConstraint(first) && isBottomConstraint(second)) throw new IllegalArgumentException();
    if(isBottomConstraint(first) && isTopConstraint(second)) throw new IllegalArgumentException();

    double intersection = getIntersection(first, second);

    // updated[17.05.2020] : added checking on suitable on feasibility for TOP ineqs
    // todo: need to test this adding
    if(isTopConstraint(first)) {
      if(intersection > rightBorder) return getWithSmallerIncline(first, second);
      if(intersection < leftBorder) return getWithGreaterIncline(first, second);
    } else if(isBottomConstraint(second)) {
      if(intersection > rightBorder) return getWithGreaterIncline(first, second);
      if(intersection < leftBorder) return getWithSmallerIncline(first, second);
    }

    return null;
  }

  /**
   * it's equal to F+(X) = min{for i in I+}( sigma_i * X + gamma_i ) for R^2 case from Diplom report
   * this function compute maximum value of Y in feasible set of Y's (it's equals to minimum value of all top inequalities in X)
   * so it's depends only on top inequalities and X where from Y computes
   * @param x - X value where from Y will be computed
   * @param topIneqs - top inequalities for computing Y ( contains sigma and gamma for R^2 case)
   * @return maximum value of Y in feasible set of Y's
   * */
  protected double getMaxFunctionFeasibleValue(double x, Inequality[] topIneqs) {
    //double min = Double.POSITIVE_INFINITY;
    //for(Inequality topIneq : topIneqs) min = Math.min(topIneq.computeFuncR2(x), min);
    //return min;
    return getMaxFunctionFeasibleValue(x, getFeasibleTopIneqsAtX(x, topIneqs));
  }

  /**
   * it's equal to F_(X) = max{for i in I_}( sigma_i * X + gamma_i ) for R^2 case from Diplom report
   * this function compute minimum value of Y in feasible set of Y's (it's equals to maximum value of all bottom inequalities in X)
   * so it's depends only on bottom inequalities and X where from Y computes
   * @param x - X value where from Y will be computed
   * @param bottomIneqs - bottom inequalities for computing Y ( contains sigma and gamma for R^2 case)
   * @return minimum value of Y in feasible set of Y's
   * */
  protected double getMinFunctionFeasibleValue(double x, Inequality[] bottomIneqs) {
    //double max = Double.NEGATIVE_INFINITY;
    //for(Inequality bottomIneq : bottomIneqs) max = Math.max(bottomIneq.computeFuncR2(x), max);
    //return max;
    return getMinFunctionFeasibleValue(x, getFeasibleBottomIneqsAtX(x, bottomIneqs));
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
   * need to use only for parallel lines of the same type of constraints(bottom or top)
   * @return constraint with maximal value of free coeffs
   * ex. getMostUpper({ y <= x + 2.0, y <= x + 3.0 }) = { y <= x + 3.0 }
   * */
  protected Inequality getMostUpper(Inequality first, Inequality second) {
    return Double.compare(first.getFree(), second.getFree()) > 0 ? first : second;
  }

  /**
   * need to use only for parrallel lines of the same type of constraints(bottom or top)
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
   * @return X of intersection of this two
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
   * ex. getWithSmallerIncline({ y >= x + 3 , y >= -x - 1 }) = { y >= -x - 1 }
   * */
  protected Inequality getWithSmallerIncline(Inequality first, Inequality second) {
    return Double.compare(first.getA(), second.getA()) > 0 ? second : first;
  }

  /**
   * find left incline from bottom border.
   * before this operation need to find two bottom inequalities, which are intersect at X
   * @param bottomIneqsAtX - two inequalities from bottom border, which are intersects at X
   * @return value of left bottom incline ( sigma_i of inequality_i at X )
   * */
  protected double getLeftBottomIncline(Pair<Inequality, Inequality> bottomIneqsAtX) {
    return Math.min(bottomIneqsAtX.getValue0().getA(), bottomIneqsAtX.getValue1().getA());
  }

  /**
   * find right incline from bottom border.
   * before this operation need to find two bottom inequalities, which are intersect at X
   * @param bottomIneqsAtX - two inequalities from bottom border, which are intersects at X
   * @return value of right bottom incline ( sigma_i of inequality_i at X )
   * */
  protected double getRightBottomIncline(Pair<Inequality, Inequality> bottomIneqsAtX) {
    return Math.max(bottomIneqsAtX.getValue0().getA(), bottomIneqsAtX.getValue1().getA());
  }

  /**
   * find left incline from top border.
   * before this operation need to find two top inequalities, which are intersect at X
   * @param topIneqsAtX - two inequalities from top border, which are intersects at X
   * @return value of left top incline ( sigma_i of inequality_i at X )
   * */
  protected double getLeftTopIncline(Pair<Inequality, Inequality> topIneqsAtX) {
    return Math.max(topIneqsAtX.getValue0().getA(), topIneqsAtX.getValue1().getA());
  }

  /**
   * find right incline from top border.
   * before this operation need to find two top inequalities, which are intersect at X
   * @param topIneqsAtX - two inequalities from top border, which are intersects at X
   * @return value of right top incline ( sigma_i of inequality_i at X )
   * */
  protected double getRightTopIncline(Pair<Inequality, Inequality> topIneqsAtX) {
    return Math.min(topIneqsAtX.getValue0().getA(), topIneqsAtX.getValue1().getA());
  }

  /**
   * @return true, if x at feasible set, false either
   * @param x x, where we check
   * @param bottomIneqs - bottom inequalities(need to find bottom border at X)
   * @param topIneqs - top inequalities(need to find top border at X)
   * */
  protected boolean isFeasible(double x, Inequality[] bottomIneqs, Inequality[] topIneqs) {
    double minFuncFeasibleValue = getMinFunctionFeasibleValue(x, bottomIneqs);
    double maxFuncFeasibleValue = getMaxFunctionFeasibleValue(x, topIneqs);
    return isFeasible(minFuncFeasibleValue, maxFuncFeasibleValue);
  }

  /**
   * @return true, if x at feasible set, false either
   * @param x x, where we check
   * @param bottomIneqsAtX - two bottom inequalities, which are intersects at X = x (need to find bottom border at X)
   * @param topIneqsAtX  - two top inequalities, which are intersects at X = x (need to find top border at X)
   * */
  protected  boolean isFeasible(double x, Pair<Inequality, Inequality> bottomIneqsAtX, Pair<Inequality, Inequality> topIneqsAtX) {
    double minFuncFeasibleValue = getMinFunctionFeasibleValue(x, bottomIneqsAtX);
    double maxFuncFeasibleValue = getMaxFunctionFeasibleValue(x, topIneqsAtX);
    return isFeasible(minFuncFeasibleValue, maxFuncFeasibleValue);
  }

  /**
   * @return true, if x at feasible set, false either
   * @param minFuncFeasibleValue - F_(X) = max{ for i in I_ }(sigma_i * X + gamma_i)
   * @param maxFuncFeasibleValue - F+(X) = min{ for i in I+ }(sigma_i * X + gamma_i)
   * */
  protected boolean isFeasible(double minFuncFeasibleValue, double maxFuncFeasibleValue) {
    return Double.compare(maxFuncFeasibleValue, minFuncFeasibleValue) >= 0;
  }

  /**
   * it's equal to F_(X) = max{for i in I_}( sigma_i * X + gamma_i ) for R^2 case from Diplom report
   * this function compute minimum value of Y in feasible set of Y's (it's equals to maximum value of all bottom inequalities in X)
   * so it's depends only on bottom inequalities and X where from Y computes
   * @param x
   * @param bottomIneqsAtX - two inequalities from bottom border, which are intersect at X
   * @return minimum value of Y in feasible set of Y's
   * */
  protected double getMinFunctionFeasibleValue(double x, Pair<Inequality, Inequality> bottomIneqsAtX) {
    return bottomIneqsAtX.getValue0().computeFuncR2(x);
  }

  /**
   * it's equal to F+(X) = min{for i in I+}( sigma_i * X + gamma_i ) for R^2 case from Diplom report
   * this function compute maximum value of Y in feasible set of Y's (it's equals to minimum value of all top inequalities in X)
   * so it's depends only on top inequalities and X where from Y computes
   * @param x
   * @param topIneqsAtX - two inequalities from top border, which are intersect at X
   * @return maximum value of Y in feasible set of Y's
   * */
  protected double getMaxFunctionFeasibleValue(double x, Pair<Inequality, Inequality> topIneqsAtX) {
    return topIneqsAtX.getValue0().computeFuncR2(x);
  }

  /**
   * find two top inequalities, which are create top border at x
   * @param x , where we take this two inequalities
   * @param topIneqs - top inequalities, from which we choose this two
   * @return pair of two bottom ineqs, intersect at x. if only one ineq are at x, so result[0] = result[1]
   * */
  protected Pair<Inequality, Inequality> getFeasibleTopIneqsAtX(double x, Inequality[] topIneqs) {
    Inequality first = null;
    Inequality second = null;
    double min = Double.POSITIVE_INFINITY;
    for(Inequality topIneq : topIneqs) {
      double y = topIneq.computeFuncR2(x);
      if(Double.compare(y, min) < 0) {
        min = y;
        first = topIneq;
        second = null;
      } else if(Double.compare(y, min) == 0) {
        if(first == null) first = topIneq;
        else second = topIneq;
      }
    }
    if(second == null) second = first;
    return new Pair<>(first, second);
  }

  /**
   * find two bottom inequalities, which are create top border at x
   * @param x , where we take this two inequalities
   * @param bottomIneqs - bottom inequalities, from which we choose this two
   * @return pair of two bottom ineqs, intersect at x. if only one ineq are at x, so result[0] = result[1]
   * */
  protected Pair<Inequality, Inequality> getFeasibleBottomIneqsAtX(double x, Inequality[] bottomIneqs) {
    Inequality first = null;
    Inequality second = null;
    double max = Double.NEGATIVE_INFINITY;
    for(Inequality bottomIneq : bottomIneqs) {
      double y = bottomIneq.computeFuncR2(x);
      if(Double.compare(y, max) > 0) {
        max = y;
        first = bottomIneq;
        second = null;
      } else if(Double.compare(y, max) == 0) {
        if(first == null) first = bottomIneq;
        else second = bottomIneq;
      }
    }
    if(second == null) second = first;
    return new Pair<>(first, second);
  }

  /**
   * use only for X not included to feasible set
   * @param leftBottomIncline - left incline in intersection of bottom border and X = x
   * @param leftTopIncline - left incline in intersection of top border and X = x
   * @return true if feasible set is on the left from current state
   * */
  protected boolean isFeasibleOnLeft(double leftBottomIncline, double leftTopIncline) {
    return Double.compare(leftBottomIncline, leftTopIncline) > 0;
  }

  /**
   * use only for X not included to feasible set
   * @param rightBottomIncline - right incline in intersection of bottom border and X = x
   * @param rightTopIncline - right incline in intersection of top border and X = x
   * @return true if feasible set is on the right from current state
   * */
  protected boolean isFeasibleOnRight(double rightBottomIncline, double rightTopIncline) {
    return Double.compare(rightBottomIncline, rightTopIncline) < 0;
  }

  /**
   * use only for X not included to feasible set
   * @param leftBottomIncline - left incline in intersection of bottom border and X = x
   * @param rightBottomIncline - right incline in intersection of bottom border and X = x
   * @param leftTopIncline - left incline in intersection of top border and X = x
   * @param rightTopIncline - right incline in intersection of top border and X = x
   * @return true, if this lp task doesn't have a solution
   * */
  protected boolean isUnsolvable(double leftBottomIncline, double rightBottomIncline, double leftTopIncline, double rightTopIncline) {
    return Double.compare(leftBottomIncline, rightTopIncline) <= 0 && Double.compare(leftTopIncline, rightBottomIncline) <= 0;
  }

  /**
   * use only for X included into feasible set
   * @param leftBottomIncline - left incline in intersection of bottom border and X = x
   * @return true, if optimum on the left from current state
   * */
  protected boolean isOptimumOnLeft(double leftBottomIncline) {
    return Double.compare(leftBottomIncline, 0) > 0;
  }

  /**
   * use only for X included into feasible set
   * @param rightBottomIncline - left incline in intersection of bottom border and X = x
   * @return true, if optimum on the right from current state
   * */
  protected boolean isOptimumOnRight(double rightBottomIncline) {
    return Double.compare(rightBottomIncline, 0) < 0;
  }

  /**
   * simple condition on is current state optimum
   * @param leftBottomIncline - left incline in intersection of bottom border and X = x
   * @param rightBottomIncline - right incline in intersection of bottom border and X = x
   * @return true, is current state is optimum
   * */
  protected boolean isOptimum(double leftBottomIncline, double rightBottomIncline) {
    return Double.compare(leftBottomIncline, 0) <=0 && Double.compare(rightBottomIncline, 0) >= 0;
  }

  protected Inequality[] removeFromArray(Inequality[] original, Collection<Inequality> toRemove) {
    Inequality[] result = new Inequality[original.length - toRemove.size()];
    int i = 0;
    for(Inequality inequality : original) {
      if( ! toRemove.contains(inequality) ) {
        result[i] = inequality;
        i++;
      }
    }
    return result;
  }

  protected double[] mergeArrays(double[] first, double[] second) {
    double[] merged = new double[first.length + second.length];
    System.arraycopy(first, 0, merged, 0, first.length);
    System.arraycopy(second, 0, merged, first.length, second.length);
    return merged;
  }
}
