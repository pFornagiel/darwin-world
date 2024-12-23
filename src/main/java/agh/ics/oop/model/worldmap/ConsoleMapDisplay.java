package agh.ics.oop.model.worldmap;

public class ConsoleMapDisplay implements MapChangeListener {
  private int amountOfUpdates = 0;
  private static final String UPDATE_MESSAGE = "Map id: %s%nUpdate number: %d%n";

  @Override
  public synchronized void mapChanged(WorldMap worldMap, String message) {
    amountOfUpdates++;
    System.out.printf(UPDATE_MESSAGE, worldMap.getId(), amountOfUpdates);
    System.out.println(message);
    System.out.println(worldMap.toString());
  }
}
