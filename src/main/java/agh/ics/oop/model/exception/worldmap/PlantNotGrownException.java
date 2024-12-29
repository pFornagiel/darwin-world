package agh.ics.oop.model.exception.worldmap;

import agh.ics.oop.model.util.Vector2d;

public class PlantNotGrownException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Plant at %s is not grown!";

  public PlantNotGrownException(Vector2d position) {
    super(ERROR_MESSAGE.formatted(position));
  }
}
