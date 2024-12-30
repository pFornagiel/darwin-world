package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.exception.worldmap.ElementNotOnTheMapException;
import agh.ics.oop.model.exception.worldmap.IllegalMapSizeException;
import agh.ics.oop.model.exception.worldmap.IllegalNumberOfInitialPlantsError;
import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.simulation.WorldElementVisitor;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.random.RandomPlantGrowthPositionGenerator;
import agh.ics.oop.model.util.random.WeightedEquatorRandomPositionGenerator;
import agh.ics.oop.model.worldelement.*;

import java.util.*;

public class BaseWorldMap implements WorldMap<Animal> {
  private static final Vector2d LOWER_BOUNDARY = new Vector2d(0,0);

  protected final Boundary mapBoundaries;
  private final UUID id;

  protected final HashMap<Vector2d, BaseMapTile> tileMap = new HashMap<>();
  protected final HashMap<Animal, Vector2d> animalMap = new HashMap<>();
  protected final HashSet<Vector2d> plantPositionSet = new HashSet<>();

  protected int deadAnimalCount = 0;
  protected int deadAnimalLifespanSum = 0;

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

    initialiseWorldMap(mapWidth,mapHeight,initialNumberOfPlants);
  }

//  Map initialisation
  private void initialiseWorldMap(int mapWidth, int mapHeight, int numberOfPlants){
//    Initialise tileMap HashMap
    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        Vector2d position = new Vector2d(x,y);
        tileMap.put(position, new BaseMapTile(position));
      }
    }

//    Set verdant tiles according to gaussian distribution
    WeightedEquatorRandomPositionGenerator verdantPositionGenerator = new WeightedEquatorRandomPositionGenerator(mapWidth,mapHeight);
    for(Vector2d verdantTilePosition: verdantPositionGenerator){
      tileMap.get(verdantTilePosition).setVerdant();
    }

//    Initialise the amount of plants provided in the constructor
    growPlantsAtRandomPositions(numberOfPlants);
  }

//  Manage animalMap and tileMap internally
  private void addElementAtPosition(Animal element, Vector2d position){
    tileMap.get(position).addElement(element);
    animalMap.put(element, position);
  }
  public void removeElement(Animal element){
    Vector2d elementPosition = element.getPosition();
    if(!tileMap.containsKey(elementPosition)){
      throw new ElementNotOnTheMapException(element);
    }
    tileMap.get(elementPosition).removeElement(element);
    animalMap.remove(element);

    deadAnimalCount += 1;
    deadAnimalLifespanSum += element.getLifespan();
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

//  Moving, placing, interaction
  @Override
  public void move(LivingCreature element, MoveDirection direction) {
// To Override
  }
  @Override
  public void placeElement(Animal element) throws IncorrectPositionException {
    Vector2d elementDefaultPosition = element.getPosition();
    if(!canMoveTo(elementDefaultPosition)){
      throw new IncorrectPositionException(elementDefaultPosition);
    }
    addElementAtPosition(element, elementDefaultPosition);
  }

  @Override
  public void interact(Animal element, WorldElementVisitor visitor) {
    element.acceptVisitor(visitor);
  }

  //  Plant managing
  public void growPlantAtPosition(Vector2d position){
    BaseMapTile plantTile = tileMap.get(position);
//    growPlant throws a runtime exception if plant is grown already
    plantTile.growPlant();
    plantPositionSet.add(position);
  }
  public void growPlantsAtRandomPositions(int numberOfPlants){
    RandomPlantGrowthPositionGenerator<BaseMapTile> initialPlantPositionGenerator = new RandomPlantGrowthPositionGenerator<>(tileMap, numberOfPlants);
    for(Vector2d plantTilePosition: initialPlantPositionGenerator){
      growPlantAtPosition(plantTilePosition);
    }
  }
  public void deletePlantAtPosition(Vector2d position){
    BaseMapTile plantTile = tileMap.get(position);
//    eatPlant() throws a runtime exception if plant is not grown
    plantTile.eatPlant();
    plantPositionSet.remove(position);
  }

  @Override
  public void consumePlant(LivingCreature consumer, int energy) {
    Vector2d position = consumer.getPosition();
//    whether mapTile is needed is to be seen
    consumer.consume(tileMap.get(position), energy);
    deletePlantAtPosition(position);
  }

  //  Listing elements
  @Override
  public Set<Animal> objectsAt(Vector2d position) {
    return tileMap.get(position).getElementSet();
  }
  @Override
  public ArrayList<Animal> getElements() {
    return new ArrayList<>(animalMap.keySet());
  }
//  To return map statistics
  @Override
  public void listMapStatistics(WorldMapVisitor visitor) {
    visitor.visit(this);
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
  public int getAmountOfAnimals(){
    return animalMap.size();
  }
  public int getAmountOfPlants(){
    return plantPositionSet.size();
  }
  public int getAmountOfFreeFields(){
    Set<Vector2d> fieldsOccupiedByAnimals = new HashSet<>(animalMap.values());
    return tileMap.size() - Set.of(fieldsOccupiedByAnimals, plantPositionSet).size();
  }
//  To implement
  public List<Genotype> getMostPopularGenotypes(){
    return null;
  }
  public double getAverageEnergy(){
    return animalMap.keySet().stream()
        .mapToInt(Animal::getEnergy)
        .average()
        .orElse(0.0);
  }
  public double getAverageLifespan(){
    return (double) deadAnimalLifespanSum / deadAnimalCount;
  }
  public double getAverageChildren(){
    return animalMap.keySet().stream()
        .mapToInt(animal -> animal.getChildren().size())
        .average()
        .orElse(0.0);
  }

  @Override
  public String toString() {
//    Temporary solution
    Boundary currentBoundary = getBoundaries();
    String map = "";
    for(int i=0;i<currentBoundary.upperBoundary().getY();i++){
      for(int j=0; j<currentBoundary.upperBoundary().getX();j++){
        map += tileMap.get(new Vector2d(j,i)).isVerdant() ? "■ " : "□ ";
      }
      map += "\n";
    }
    return map;
  }

//  Listeners logic
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
