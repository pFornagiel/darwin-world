package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.exception.worldmap.IllegalMapSizeException;
import agh.ics.oop.model.exception.worldmap.IllegalNumberOfInitialPlantsError;
import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.random.RandomChanceGenerator;
import agh.ics.oop.model.util.random.WeightedEquatorRandomPositionGenerator;
import agh.ics.oop.model.worldelement.Animal;
import agh.ics.oop.model.worldelement.WorldElement;

import java.util.*;

public class BaseWorldMap implements WorldMap {
  private static final Vector2d LOWER_BOUNDARY = new Vector2d(0,0);
  private static final double VERDANT_TILES_PERCENTAGE = .2;
  private static final double GROWTH_SUCCESS_PERCENTAGE = .8;
  private static final RandomChanceGenerator chanceGenerator = new RandomChanceGenerator(GROWTH_SUCCESS_PERCENTAGE);

  protected final Boundary mapBoundaries;
  private final UUID id;

  protected final HashMap<Vector2d, MapTile> tileMap = new HashMap<>();
  protected final HashMap<Animal, Vector2d> animalMap = new HashMap<>();

  protected final MapVisualizer mapVisualizer;
  protected final LinkedList<MapChangeListener> mapChangeListeners  = new LinkedList<>();

  public BaseWorldMap(int mapWidth, int mapHeight, int initialNumberOfPlants) {
//    Validation
    if(mapWidth < 0 || mapHeight < 0) {
      throw new IllegalMapSizeException(mapWidth, mapHeight);
    }
    if(initialNumberOfPlants < 0 || initialNumberOfPlants > mapWidth * mapHeight) {
      throw new IllegalNumberOfInitialPlantsError(mapWidth, mapHeight);
    }

    this.mapVisualizer = new MapVisualizer(this);
    this.id = UUID.randomUUID();
    this.mapBoundaries = new Boundary(LOWER_BOUNDARY, new Vector2d(mapWidth,mapHeight));

//    Initialize tileMap
    for (int x = 0; x <= mapWidth; x++) {
      for (int y = 0; y <= mapHeight; y++) {
        Vector2d position = new Vector2d(x,y);
        tileMap.put(position, new MapTile(position));
      }
    }

    WeightedEquatorRandomPositionGenerator positionGenerator = new WeightedEquatorRandomPositionGenerator(mapWidth,mapHeight, VERDANT_TILES_PERCENTAGE);
    for(Vector2d verdantTilePosition: positionGenerator){
      tileMap.get(verdantTilePosition).setVerdant();
    }
  }

//  Manage animalMap and tileMap
  private void addAnimalAtPosition(Animal animal, Vector2d position){
    tileMap.get(position).addAnimal(animal);
    animalMap.put(animal, position);
  }
  private void deleteAnimalAtPosition(Animal animal){
    tileMap.get(animal.getPosition()).removeAnimal(animal);
    animalMap.remove(animal);
  }

//  State checking
  @Override
  public boolean isOccupied(Vector2d position) {
    return !tileMap.get(position).isOccupied();
  }
  @Override
  public boolean canMoveTo(Vector2d position) {
    return position.getY() < mapBoundaries.upperBoundary().getY() && position.getY() > mapBoundaries.lowerBoundary().getY();
  }

//  Moving and placing
  @Override
  public void move(Animal animal, MoveDirection direction) {
// To Override
  }
  @Override
  public void place(Animal animal) throws IncorrectPositionException {
    Vector2d elementDefaultPosition = animal.getPosition();
    if(!canMoveTo(elementDefaultPosition)){
      throw new IncorrectPositionException(elementDefaultPosition);
    }
    addAnimalAtPosition(animal, elementDefaultPosition);
  }

//  Listing elements
  @Override
  public Set<Animal> objectsAt(Vector2d position) {
    return tileMap.get(position).getAnimalSet();
  }
  @Override
  public ArrayList<WorldElement> getElements() {
    return new ArrayList<>(animalMap.keySet());
  }


//  Getters
  @Override
  public Boundary getBoundaries() {
    return this.mapBoundaries;
  }
  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String toString() {
//    Temporary solution
    Boundary currentBoundary = getBoundaries();
    String map = "";
    for(int i=0;i<=currentBoundary.upperBoundary().getY();i++){
      for(int j=0; j<=currentBoundary.upperBoundary().getX();j++){
        map += tileMap.get(new Vector2d(j,i)).isVerdant() ? "■ " : "□ ";
      }
      map += "\n";
    }
    return map;
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

}
