package agh.ics.oop.model.exception.worldmap;

import agh.ics.oop.model.util.Vector2d;

public class PlantAlreadyGrownException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Cannot grow plant at %s: plant is grown already.";
  public PlantAlreadyGrownException(Vector2d position) {
    super(ERROR_MESSAGE.formatted(position));
  }
}
