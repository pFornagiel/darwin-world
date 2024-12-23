package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

public class IncorrectPositionException extends Exception {
  public IncorrectPositionException(Vector2d incorrectPosition) {
    super("Position (%d, %d) is not correct".formatted(incorrectPosition.getX(), incorrectPosition.getY()));
  }
}
