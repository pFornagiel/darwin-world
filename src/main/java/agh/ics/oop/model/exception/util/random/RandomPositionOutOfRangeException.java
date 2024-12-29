package agh.ics.oop.model.exception.util.random;

public class RandomPositionOutOfRangeException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Requested random position out of range.";
  public RandomPositionOutOfRangeException() {
    super(ERROR_MESSAGE);
  }
}
