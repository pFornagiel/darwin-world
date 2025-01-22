package agh.ics.oop.model.worldmap.abstracts;

import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.exception.worldmap.ElementNotOnTheMapException;
import agh.ics.oop.model.exception.worldmap.IllegalMapSizeException;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.random.RandomPlantGrowthPositionGenerator;
import agh.ics.oop.model.util.random.WeightedEquatorRandomPositionGenerator;
import agh.ics.oop.model.worldelement.abstracts.WorldElement;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.model.worldmap.util.Boundary;

import java.util.*;

public abstract class AbstractWorldMap<E extends WorldElement, M extends MapTile<E>> implements WorldMap<E> {
  private static final Vector2d LOWER_BOUNDARY = new Vector2d(0,0);

  protected final Boundary mapBoundaries;
  private final UUID id;

  protected final HashMap<Vector2d, M> tileMap = new HashMap<>();
  protected final HashSet<Vector2d> plantPositionSet = new HashSet<>();
  protected final HashSet<Vector2d> elementPositionSet = new HashSet<>();
  protected final HashSet<Vector2d> verdantFieldPositionSet = new HashSet<>();
  protected final HashSet<E> elementSet = new HashSet<>();



  public AbstractWorldMap(int mapWidth, int mapHeight) {
//    Validation
    if(mapWidth < 0 || mapHeight < 0) {
      throw new IllegalMapSizeException(mapWidth, mapHeight);
    }

    this.id = UUID.randomUUID();
    this.mapBoundaries = new Boundary(LOWER_BOUNDARY, new Vector2d(mapWidth,mapHeight));

    initialiseTileMap(mapWidth,mapHeight);
    initialiseVerdantFields(mapWidth,mapHeight);
  }

  abstract protected void initialiseTileMap(int mapWidth, int mapHeight);

  private void initialiseVerdantFields(int mapWidth, int mapHeight){
    WeightedEquatorRandomPositionGenerator verdantPositionGenerator = new WeightedEquatorRandomPositionGenerator(mapWidth,mapHeight);
    for(Vector2d verdantTilePosition: verdantPositionGenerator){
      tileMap.get(verdantTilePosition).setVerdant();
      verdantFieldPositionSet.add(verdantTilePosition);
    }
  }
//  Placing and removing elements
  public void removeElement(E element){
    Vector2d elementPosition = element.getPosition();
    if(!tileMap.get(elementPosition).containsElement(element)){
      throw new ElementNotOnTheMapException(element);
    }
    M tile = tileMap.get(elementPosition);
    tile.removeElement(element);
    if(!tile.isOccupied()){
      elementPositionSet.remove(elementPosition);
    }
    elementSet.remove(element);

  }

  @Override
  public void placeElement(E element, Vector2d position) {
    if(!canMoveTo(position)){
      throw new IncorrectPositionException(position);
    }
    tileMap.get(position).addElement(element);
    elementPositionSet.add(position);
    elementSet.add(element);

  }
  @Override
  public void placeElement(E element) {
    placeElement(element, element.getPosition());
  }

  @Override
  public boolean isOccupied(Vector2d position) {
    return !tileMap.get(position).isOccupied();
  }

//  Move handler
  @Override
  public Vector2d determinePositionAfterMove(Vector2d position, Vector2d move) {
    Vector2d newPosition = position.add(move);
    if(!canMoveTo(newPosition)){
      return position;
    }

    int upperBoundaryX = mapBoundaries.upperBoundary().getX();
    if(newPosition.getX() >= upperBoundaryX)
      return new Vector2d(newPosition.getX() % upperBoundaryX, newPosition.getY());
    if(newPosition.getX() < 0){
      return new Vector2d(upperBoundaryX+newPosition.getX(), newPosition.getY());
    }

    return newPosition;
  }

  @Override
  public boolean canMoveTo(Vector2d newPosition) {
    return newPosition.getY() < mapBoundaries.upperBoundary().getY() &&
           newPosition.getY() >= mapBoundaries.lowerBoundary().getY();
  }

  @Override
  public void moveElementTo(E element, Vector2d position) {
    if(!canMoveTo(position)){
      throw new ElementNotOnTheMapException(element);
    }
    Vector2d oldPosition = element.getPosition();

    removeElement(element);
    placeElement(element, position);
  }

  //  Plant managing
  public void growPlantAtPosition(Vector2d position){
    M plantTile = tileMap.get(position);
    plantTile.growPlant();
    plantPositionSet.add(position);
  }

  public void deletePlantAtPosition(Vector2d position){
    M plantTile = tileMap.get(position);
    plantTile.eatPlant();
    plantPositionSet.remove(position);
  }

  public void randomPlantGrowth(int numberOfPlants){
    RandomPlantGrowthPositionGenerator<M> initialPlantPositionGenerator = new RandomPlantGrowthPositionGenerator<>(tileMap, numberOfPlants);
    for(Vector2d plantTilePosition: initialPlantPositionGenerator){
      growPlantAtPosition(plantTilePosition);
    }
  }

  @Override
  public boolean isPlantGrown(Vector2d position) {
    return tileMap.get(position).isPlantGrown();
  }

  public int getAmountOfPlants(){
    return plantPositionSet.size();
  }

  public Set<Vector2d> getVerdantFieldPositionSet(){
    return Collections.unmodifiableSet(verdantFieldPositionSet);
  }

  //  Listing elements
  @Override
  public Set<E> objectsAt(Vector2d position) {
    return Collections.unmodifiableSet(tileMap.get(position).getElementSet());
  }

  @Override
  public Set<E> getElements() {
    return Collections.unmodifiableSet(elementSet);
  }

  @Override
  public int getAmountOfElements(){
    return elementSet.size();
  }

  @Override
  public Set<Vector2d> getElementPositionSet() {
    return Collections.unmodifiableSet(elementPositionSet);
  }

  @Override
  public Set<Vector2d> getPlantPositionSet(){
    return Collections.unmodifiableSet(plantPositionSet);
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
  

}
