package agh.ics.oop.model.exception;

import agh.ics.oop.model.util.Vector2d;

public class IncorrectPositionException extends RuntimeException {
  public IncorrectPositionException(Vector2d incorrectPosition) {
    super("Position (%d, %d) is not correct".formatted(incorrectPosition.getX(), incorrectPosition.getY()));
  }
}
