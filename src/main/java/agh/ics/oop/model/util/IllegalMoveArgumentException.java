package agh.ics.oop.model.util;

public class IllegalMoveArgumentException extends RuntimeException {
  private static final String ERROR_MESSAGE = "%s is not a legal move specification [f,b,l,r]";

  public IllegalMoveArgumentException(String argument) {
    super(ERROR_MESSAGE.formatted(argument));
  }
}
