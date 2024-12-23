package agh.ics.oop.model.util;
import agh.ics.oop.model.exception.IllegalMoveArgumentException;

import java.util.ArrayList;

public class OptionsParser {

  public static ArrayList<MoveDirection> parse(String[] args){
    ArrayList<MoveDirection> moves = new ArrayList<>();
    for(String arg: args){
      addToMoveList(arg, moves);
    }
    return moves;
  }

  private static void addToMoveList(String arg, ArrayList<MoveDirection> moves) {
    switch (arg) {
      case "f", "forward" -> moves.add(MoveDirection.FORWARD);
      case "b", "backward" -> moves.add(MoveDirection.BACKWARD);
      case "r", "right" -> moves.add(MoveDirection.RIGHT);
      case "l", "left" -> moves.add(MoveDirection.LEFT);
      default -> throw new IllegalMoveArgumentException(arg);
    }
  }
}
