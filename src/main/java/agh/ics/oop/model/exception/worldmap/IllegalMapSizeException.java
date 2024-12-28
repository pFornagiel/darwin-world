package agh.ics.oop.model.exception.worldmap;

public class IllegalMapSizeException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Map size %d x %d cannot be negative";
  public IllegalMapSizeException(int mapWidth, int mapHeight) {
    super(ERROR_MESSAGE.formatted(mapWidth, mapHeight));
  }
}
