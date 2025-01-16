package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.simulation.SimulationVisitor;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;
import agh.ics.oop.model.worldmap.abstracts.AbstractAnimalMap;

import java.util.*;

public class FireWorldMap extends AbstractAnimalMap<FireMapTile> {
  private final Random rand = new Random();
  private final HashSet<Vector2d> firePositionSet = new HashSet<>();

//  for spreadFire method
  private static final Vector2d MOVE_RIGHT = new Vector2d(1,0);
  private static final Vector2d MOVE_LEFT = new Vector2d(-1,0);
  private static final Vector2d MOVE_UP = new Vector2d(0,-1);
  private static final Vector2d MOVE_DOWN = new Vector2d(0,1);
  private static final List<Vector2d> MOVE_LIST = List.of(MOVE_RIGHT, MOVE_LEFT, MOVE_UP, MOVE_DOWN);

  int maxFireDuration = 0;

  public FireWorldMap(int mapWidth, int mapHeight, AnimalFactory animalFactory, int maxFireDuration) {
    super(mapWidth, mapHeight, animalFactory);
    this.maxFireDuration = maxFireDuration;
  }

  @Override
  protected void initialiseTileMap(int mapWidth, int mapHeight) {
    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        Vector2d position = new Vector2d(x,y);
        tileMap.put(position, new FireMapTile(position));
      }
    }
  }

  public boolean isOnFire(Vector2d position){
    return tileMap.get(position).isOnFire();
  }

  public void setOnFire(Vector2d position){
    tileMap.get(position).setOnFire();
    deletePlantAtPosition(position);
    firePositionSet.add(position);
  }

  public void extenguish(Vector2d position){
    tileMap.get(position).extenguish();
    firePositionSet.remove(position);
  }

  public Set<Vector2d> getFirePositionSet(){
    return Collections.unmodifiableSet(firePositionSet);
  }

  @Override
  public void accept(SimulationVisitor visitor){
    visitor.visit(this);
  }

  @Override
  public void killDyingCreature(Animal animal) {
    super.killDyingCreature(animal);
    if(firePositionSet.contains(animal.getPosition())){
      killAnimal(animal);
    }
  }

  public void randomFireOutburst(){
    Set<Vector2d> plantPositionSet = getPlantPositionSet();
    List<Vector2d> fireApplicablePositionList = plantPositionSet.stream()
                                                              .filter(e -> !firePositionSet.contains(e))
                                                              .toList();
    if(!fireApplicablePositionList.isEmpty()){
      int randomIndex = rand.nextInt(fireApplicablePositionList.size());
      setOnFire(fireApplicablePositionList.get(randomIndex));
    }
  }

  public void spreadFire(){
    HashSet<Vector2d> updatePositionList = new HashSet<>();
    for(Vector2d position : getFirePositionSet()){
        MOVE_LIST.stream()
            .map(move -> determinePositionAfterMove(position, move))
            .filter(p -> p != position && !isOnFire(p) && isPlantGrown(p))
            .forEach(updatePositionList::add);
    }
    for (Vector2d position : updatePositionList) {
      setOnFire(position);
    }
  }

  public void updateFireDuration(){
    HashSet<Vector2d> toExtenguishSet = new HashSet<>();
    for(Vector2d position : getFirePositionSet()){
      tileMap.get(position).updateFireDuration();
      if(tileMap.get(position).getFireDuration() > maxFireDuration){
        toExtenguishSet.add(position);
      }
    }
    for(Vector2d position : toExtenguishSet){
      extenguish(position);
    }
  }

  @Override
  public String toString() {
    Boundary currentBoundary = getBoundaries();
    String map = "";
    for(int i=0;i<currentBoundary.upperBoundary().getY();i++){
      for(int j=0; j<currentBoundary.upperBoundary().getX();j++){
        FireMapTile position = tileMap.get(new Vector2d(j,i));
        if(position.isOccupied()){
          map += position.getElementList().getFirst().toString() + ' ';
        } else if(position.isOnFire()) {
          map += "+ ";
        } else {
          map += tileMap.get(new Vector2d(j,i)).isPlantGrown() ? "# " : "O ";
        }
      }
      map += "\n";
    }
    return map;
  }
}
