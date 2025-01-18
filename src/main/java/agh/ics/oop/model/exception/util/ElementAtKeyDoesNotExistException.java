package agh.ics.oop.model.exception.util;

public class ElementAtKeyDoesNotExistException extends RuntimeException {
  private static final String ERROR_MESSAGE =
      "Element at key %s does not exist and cannot be decremented";
  public ElementAtKeyDoesNotExistException(String key) {
    super(ERROR_MESSAGE.formatted(key));
  }
}
