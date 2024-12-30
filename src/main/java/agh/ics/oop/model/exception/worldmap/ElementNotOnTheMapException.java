package agh.ics.oop.model.exception.worldmap;
import agh.ics.oop.model.worldelement.WorldElement;

public class ElementNotOnTheMapException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Element %s is not on the map";
  public ElementNotOnTheMapException(WorldElement element) {
    super(ERROR_MESSAGE.formatted(element));
  }
}
