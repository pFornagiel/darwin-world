package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Animal;
import agh.ics.oop.model.worldelement.Grass;
import agh.ics.oop.model.worldelement.WorldElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.sqrt;

//Use WorldElement as generic type, because we wish to get Grass elements with objectAt as well
//Define grassField to use Animal as mainElement type
public class GrassField extends AbstractWorldMap<Animal> {
  private final HashMap<Vector2d, Grass> grassPositionMap = new HashMap<>();
  private static final Vector2d MAP_PADDING = new Vector2d(1, 1);

  public GrassField(int numberOfGrassElements) {
    super();

    int elementPositionBound = (int)sqrt(numberOfGrassElements * 10);
    RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(elementPositionBound, elementPositionBound, numberOfGrassElements);
    for(Vector2d grassPosition : randomPositionGenerator) {
      grassPositionMap.put(grassPosition, new Grass(grassPosition));
      updateMapBoundaries(grassPosition);
    }
  }

  private void updateMapBoundaries(Vector2d elementPosition){
//    Vector2d is immutable, so assigning element's position to upper and lower map boundaries
//    should not cause problems

    if(mapBoundaries == null){
      mapBoundaries = new Boundary(elementPosition, elementPosition);
      return;
    }

    Vector2d currentUpperBoundary = mapBoundaries.upperBoundary();
    Vector2d currentLowerBoundary = mapBoundaries.lowerBoundary();

    if(currentUpperBoundary.getX() < elementPosition.getX() || currentUpperBoundary.getY() < elementPosition.getY()){
      currentUpperBoundary = new Vector2d(Math.max(elementPosition.getX(), currentUpperBoundary.getX()), Math.max(elementPosition.getY(), currentUpperBoundary.getY()));
    }
    if(currentLowerBoundary.getX() > elementPosition.getX() || currentLowerBoundary.getY() > elementPosition.getY()){
      currentLowerBoundary = new Vector2d(Math.min(elementPosition.getX(), currentLowerBoundary.getX()), Math.min(elementPosition.getY(), currentLowerBoundary.getY()));
    }

    mapBoundaries = new Boundary(currentLowerBoundary, currentUpperBoundary);
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    Vector2d currentPosition = animal.getPosition();
    animal.move(direction,this);
    if(currentPosition != animal.getPosition()){
      mainElementPositionMap.put(animal.getPosition(), animal);
      mainElementPositionMap.put(currentPosition, null);
      updateMapBoundaries(animal.getPosition());
    }
    notifyAccordingToMove("Animal", direction, currentPosition, animal.getPosition());
  }

  @Override
  public void place(Animal animal) throws IncorrectPositionException {
    super.place(animal);
    updateMapBoundaries(animal.getPosition());
    notifyAccordingToMove("Animal", animal.getPosition());
  }

  @Override
  public WorldElement objectAt(Vector2d position) {
    Grass grass = grassPositionMap.get(position);
    WorldElement animal = super.objectAt(position);
    return animal != null ? animal : grass;
  }

  @Override
  public ArrayList<WorldElement> getElements() {
    ArrayList<WorldElement> allElementsList = super.getElements();
    for (Map.Entry<Vector2d, Grass> entry : grassPositionMap.entrySet()) {
      Grass entryValue = entry.getValue();
      if (entryValue != null) {
        allElementsList.add(entryValue);
      }
    }

    return allElementsList;
  }

  @Override
  public boolean isOccupied(Vector2d position) {
    boolean occupiedByAnimal =  super.isOccupied(position);
    return occupiedByAnimal || grassPositionMap.get(position) != null;
  }

  public boolean isOccupiedByAnimal(Vector2d position) {
//    Use the standard abstractClass hashmap
    return super.isOccupied(position);
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return !isOccupiedByAnimal(position);
  }

  @Override
  public Boundary getCurrentBounds() {
    return new Boundary(mapBoundaries.lowerBoundary().substract(MAP_PADDING), mapBoundaries.upperBoundary().add(MAP_PADDING));
  }
}
