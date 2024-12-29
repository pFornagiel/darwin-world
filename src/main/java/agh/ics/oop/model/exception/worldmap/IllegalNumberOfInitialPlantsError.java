package agh.ics.oop.model.exception.worldmap;

public class IllegalNumberOfInitialPlantsError extends RuntimeException {
  private static final String ERROR_MESSAGE = "Number of plants for map of size: %d x %d cannot exceed %d";
  public IllegalNumberOfInitialPlantsError(int mapWidth, int mapHeight) {
    super(ERROR_MESSAGE.formatted(mapWidth, mapHeight, mapWidth*mapHeight));
  }
}
