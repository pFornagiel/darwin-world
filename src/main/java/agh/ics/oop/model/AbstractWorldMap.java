package agh.ics.oop.model;

import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap<MainElementType extends  WorldElement> implements  WorldMap<MainElementType> {
  protected final HashMap<Vector2d, MainElementType> mainElementPositionMap = new HashMap<>();
  protected final MapVisualizer mapVisualizer;
  protected final LinkedList<MapChangeListener> mapChangeListeners  = new LinkedList<>();
  protected Boundary mapBoundaries;
  private final UUID id;

  public AbstractWorldMap() {
    this.mapVisualizer = new MapVisualizer(this);
    this.id = UUID.randomUUID();
  }

  public void addToListeners(MapChangeListener listener){
    mapChangeListeners.add(listener);
  }

  public void removeFromListeners(MapChangeListener listener){
    mapChangeListeners.remove(listener);
  }

  public void notifyAllListeners(String messege){
    for(MapChangeListener listener : mapChangeListeners){
      listener.mapChanged(this, messege);
    }
  }

  public void notifyAccordingToMove(String objectTypeName, MoveDirection direction, Vector2d oldPosition, Vector2d newPosition){
    switch(direction){
      case LEFT, RIGHT -> notifyAllListeners("%s at %s turned %s".formatted(objectTypeName, oldPosition, direction));
      case FORWARD, BACKWARD -> {
        if(oldPosition != newPosition){
          notifyAllListeners("%s at %s moved %s to %s".formatted(objectTypeName, oldPosition, direction, newPosition));
        } else {
          notifyAllListeners("%s at %s did not move!".formatted(objectTypeName, oldPosition));
        }
      }
    }
  }

  public void notifyAccordingToMove(String objectType, Vector2d position){
    notifyAllListeners("%s was placed at %s".formatted(objectType, position));
  }

  @Override
  public void place(MainElementType element) throws IncorrectPositionException {
    Vector2d elementDefaultPosition = element.getPosition();
    if(!canMoveTo(elementDefaultPosition)){
      throw new IncorrectPositionException(elementDefaultPosition);
    }
    mainElementPositionMap.put(elementDefaultPosition, element);
  }

  @Override
  public boolean isOccupied(Vector2d position) {
    return mainElementPositionMap.get(position) != null;
  }

  @Override
  public WorldElement objectAt(Vector2d position) {
    return mainElementPositionMap.get(position);
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    boolean isObjectAtGivenPosition = isOccupied(position);
    return !isObjectAtGivenPosition;
  }

  @Override
  public ArrayList<WorldElement> getElements() {
    ArrayList<WorldElement> allElementsList = new ArrayList<>();

    for (Map.Entry<Vector2d, MainElementType> entry : mainElementPositionMap.entrySet()) {
      MainElementType entryValue = entry.getValue();
      if (entryValue != null) {
        allElementsList.add( entryValue);
      }
    }

    return allElementsList;
  }

  @Override
  public void move(MainElementType element, MoveDirection direction) {}

  @Override
  public Boundary getCurrentBounds() {
    return this.mapBoundaries;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public void removeMainElementsFromWorld() {

    Iterator<Map.Entry<Vector2d, MainElementType>> iterator = mainElementPositionMap.entrySet().iterator();
    while (iterator.hasNext()) {
      iterator.next(); // Move to the next entry
      iterator.remove(); // Remove the current entry
    }

  }

  @Override
  public String toString() {
    Boundary currentBoundary = getCurrentBounds();
    return this.mapVisualizer.draw(currentBoundary.lowerBoundary(), currentBoundary.upperBoundary());
  }
}
