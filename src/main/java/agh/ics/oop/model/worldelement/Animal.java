package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.simulation.WorldElementVisitor;
import agh.ics.oop.model.worldmap.MapDirection;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldmap.MapTile;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement, LivingCreature {
  private MapDirection orientation;
  private Vector2d position;
  private int energy;
  private int plantEatenCounter;
  private int childrenCount;
  private int descendantCount;
  private int daysLived;
  private int dayOfDeath;

  ArrayList<Integer> genes = new ArrayList<>();
  private static final String[] ORIENTATION_STRING_ARRAY = {"^", ">", "v", "<"};


  public Animal(){
    this.orientation = MapDirection.NORTH;
    this.position = new Vector2d(2,2);
  }

  public Animal(Vector2d position){
    this.orientation = MapDirection.NORTH;
    this.position = position;
  }

  @Override
  public int getEnergy() {
    return 0;
  }

  @Override
  public int getLifespan() {
    return 0;
  }

  @Override
  public List<LivingCreature> getChildren() {
    return null;
  }

  @Override
  public Genotype getGenotype() {
    return null;
  }

  public Vector2d getPosition() {
    return position;
  }

  public MapDirection getOrientation() {
    return orientation;
  }

  public boolean isAt(Vector2d position){
    return this.position.equals(position);
  }

  @Override
  public void acceptVisitor(WorldElementVisitor visitor) {

  }

  @Override
  public void consume(MapTile mapTile, int energy) {

  }

  private Vector2d getNewAnimalPosition(Vector2d nextMove, MoveValidator moveValidator){
    Vector2d newPosition = position.add(nextMove);
    if(moveValidator.canMoveTo(newPosition)){
      return newPosition;
    }
    return position;
  }

  @Override
  public void move(MoveDirection direction, MoveValidator moveValidator){
    Vector2d nextMove = orientation.toUnitVector();
    switch(direction){
      case MoveDirection.RIGHT -> orientation = orientation.next();
      case MoveDirection.LEFT -> orientation = orientation.previous();
      case MoveDirection.FORWARD -> position = getNewAnimalPosition(nextMove, moveValidator);
      case MoveDirection.BACKWARD -> position = getNewAnimalPosition(nextMove.opposite(), moveValidator);
    }
  }

  @Override
  public String toString() {
    return switch(this.orientation){
      case NORTH -> ORIENTATION_STRING_ARRAY[0];
      case EAST -> ORIENTATION_STRING_ARRAY[1];
      case SOUTH -> ORIENTATION_STRING_ARRAY[2];
      case WEST -> ORIENTATION_STRING_ARRAY[3];
    };
  }

  public String toStringPositionInformation(){
    return "Animal: [position=%s, orientation=%s]".formatted(this.position, this.orientation);
  }
}
