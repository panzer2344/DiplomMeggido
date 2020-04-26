package solver.splitter;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.Inequality;

import java.util.LinkedList;
import java.util.List;

import static model.Inequality.Sign.*;

public class Splitter2D {

  public Splitter2D() { }

  public Splitted split(Inequality[] inequalities) {
    List<Inequality> top = new LinkedList<>();
    List<Inequality> bottom = new LinkedList<>();
    List<Inequality> zero = new LinkedList<>();

    for(Inequality inequality : inequalities) {
      if(inequality.isZeroConstraint()) zero.add(inequality);
      else {
        Inequality.Sign sign = inequality.getSign();
        if(GREAT.equals(sign) || GREAT_OR_EQUAL.equals(sign)) bottom.add(inequality);
        else if(LESS.equals(sign) || LESS_OR_EQUAL.equals(sign)) top.add(inequality);
        else System.out.println("Error! : inequality with bad sign: " + inequality.toString());
      }
    }

    return new Splitted(top.toArray(new Inequality[0]), bottom.toArray(new Inequality[0]), zero.toArray(new Inequality[0]));
  }

  /**
   *
   * contains three types of constraint
   * bottom - like y >= ax + b
   * top - like y <= ax + b
   * and zero - like 0 <= (>=) ax + b
   *
   * */
  @Data
  @AllArgsConstructor
  public static class Splitted {
    private Inequality[] top;
    private Inequality[] bottom;
    private Inequality[] zero;
  }

}
