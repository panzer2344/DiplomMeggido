package solver;

import model.Inequality;
import org.javatuples.Pair;

import java.util.HashSet;
import java.util.Set;

public class Solver2DWithBruteforce extends Solver2D {
    @Override
    protected Pair<Double, Double> recursiveSolve() {
        if(getBotLength() <= 3) return bruteForceSolve();
        return super.recursiveSolve();
    }

    /**
     * works correctly only if there are one bot inequality
     * */
    protected Pair<Double, Double> bruteForceSolve() {
        double min = Double.POSITIVE_INFINITY;
        // merge two arrays into one common
        //Inequality[] allIneqs = new Inequality[top.length + bot.length];
        //System.arraycopy(top, 0, allIneqs, 0, top.length);
        //System.arraycopy(bot, 0, allIneqs, top.length, bot.length);

        Set<Inequality> allIneqsSet = new HashSet<>();

        for(int i = 0; i < top.length; i++) {
            boolean isFirstNeeded = true;
            for(int j = 0; j < top.length; j++) {
                if(i == j) break;
                Inequality nonSuitable = getNonSuitableOnParallel(top[i], top[j]);
                if(top[i].equals(nonSuitable)) {
                    isFirstNeeded = false;
                    break;
                }
            }
            if(isFirstNeeded) allIneqsSet.add(top[i]);
        }

        for(int i = 0; i < bot.length; i++) {
            boolean isFirstNeeded = true;
            for(int j = 0; j < bot.length; j++) {
                if(i == j) break;
                Inequality nonSuitable = getNonSuitableOnParallel(bot[i], bot[j]);
                if(bot[i].equals(nonSuitable)) {
                    isFirstNeeded = false;
                    break;
                }
            }
            if(isFirstNeeded) allIneqsSet.add(bot[i]);
        }

        Inequality[] allIneqs = allIneqsSet.toArray(new Inequality[0]);

        // compute all intersections and find minimum from all of this
        double resultX = Double.NaN;
        for(int i = 0; i < allIneqs.length; i++) {
            for(int j = i + 1; j < allIneqs.length; j++) {
                Inequality firstCandidate = allIneqs[i];
                Inequality secondCandidate = allIneqs[j];

                if(isParallel(firstCandidate, secondCandidate)) break;

                double intersection = getIntersection(firstCandidate, secondCandidate);
                double y = firstCandidate.computeFuncR2(intersection);

                if(Double.compare(y, getMinFunctionFeasibleValue(intersection, bot)) != 0) break;

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
}
