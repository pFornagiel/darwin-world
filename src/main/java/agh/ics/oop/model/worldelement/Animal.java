package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.exception.worldelement.CreatureAlreadyDeadError;
import agh.ics.oop.model.exception.worldelement.CreatureStillAliveError;
import agh.ics.oop.model.simulation.WorldElementVisitor;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Animal implements WorldElement, LivingCreature {

  private int energyGainedByEating = 0;
  private int energyUsedForReproduction = 0;

  private Direction orientation;
  private Vector2d position;
  private Genotype genotype;
  private int energy = 0;
  private int plantEatenCounter;
  private int lifespan = 0;
  private int dayOfDeath = -1;

  private final HashSet<LivingCreature> childrenSet = new HashSet<>();

//  for testing purposes
  private static final String[] ORIENTATION_STRING_ARRAY = {"^","↗",">","↘","v","↙","<","↖"};

  public Animal(){
    this.orientation = Direction.NORTH;
    this.position = new Vector2d(2,2);
  }

  public Animal(Vector2d position){
    this.orientation = Direction.NORTH;
    this.position = position;
  }

  public void initialiseGenotype(int numberOfGenes){
    this.genotype = new Genotype(numberOfGenes);
  }

  public void initialiseGenotype(
      int numberOfGenes,
      Animal firstAnimal,
      Animal secondAnimal,
      int maxAmountOfMutations,
      int minAmountOfMutations,
      int energyNeededToReproduce
  ){
    this.genotype = new Genotype(
     numberOfGenes,
     firstAnimal,
     secondAnimal,
     maxAmountOfMutations,
     minAmountOfMutations,
     energyNeededToReproduce
    );
  }
  public void setEnergyGainedByEating(int energyGainedByEating){
    this.energyGainedByEating = energyGainedByEating;
  }
  public void setEnergyUsedForReproduction(int energyUsedForReproduction){
    this.energyUsedForReproduction = energyUsedForReproduction;
  }

// Energy
  @Override
  public int getEnergy() {
    return energy;
  }
  @Override
  public int drainEnergy(int energy) {
    this.energy -= energy;
    return this.energy;
  }
  @Override
  public int drainEnergy() {
    this.energy -= energyUsedForReproduction;
    return this.energy;
  }
  @Override
  public int gainEnergy() {
    this.energy += energyGainedByEating;
    return this.energy;
  }
  @Override
  public int gainEnergy(int energy) {
    this.energy += energy;
    return this.energy;
  }


  //  Lifespan
  @Override
  public int getLifespan() {
    return lifespan;
  }
  @Override
  public void updateLifespan(){
    lifespan += 1;
  }
  @Override
  public void kill(){
    if(dayOfDeath != -1){
      throw new CreatureAlreadyDeadError(this);
    }
    dayOfDeath = lifespan;
    energy = 0;
    position = null;
  }
  @Override
  public int getDayOfDeath() {
    if(dayOfDeath == -1){
      throw new CreatureStillAliveError(this);
    }
    return dayOfDeath;
  }

//  Children
  @Override
  public List<LivingCreature> getChildren() {
    return new ArrayList<>(childrenSet);
  }
  @Override
  public int getAmountOfChildren() {
    return childrenSet.size();
  }
  @Override
  public int getAmountOfDescendants() {
    int count = 0;
    for(LivingCreature creature : childrenSet){
      count += 1 + creature.getAmountOfChildren();
    }
    return count;
  }
  @Override
  public void addChild(LivingCreature child){
    childrenSet.add(child);
  }

//  Genes
  @Override
  public Genotype getGenotype() {
    return this.genotype;
  }
  @Override
  public int getGene(int geneIndex){
    return genotype.getGene(geneIndex);
  }
  @Override
  public int getNextGene(){
    return genotype.getNextGene();
  }
  @Override
  public int getRandomGene(){
    return genotype.getRandomGene();
  }


//  Position and direction
  public Vector2d getPosition() {
    return position;
  }
  public Direction getOrientation() {
    return orientation;
  }
  public boolean isAt(Vector2d position){
    return this.position.equals(position);
  }

//  Eating
  @Override
  public void eat() {
    gainEnergy();
    plantEatenCounter += 1;
  }
  @Override
  public int getEatenPlants(){
    return plantEatenCounter;
  }

  private Vector2d getNewAnimalPosition(Vector2d nextMove, MoveValidator moveValidator){
    Vector2d newPosition = position.add(nextMove);
    if(moveValidator.canMoveTo(newPosition)){
      return newPosition;
    }
    return position;
  }

  @Override
  public void move(Direction direction, MoveValidator moveValidator){
//    Vector2d nextMove = orientation.toUnitVector();
//    switch(direction){
//      case Direction.RIGHT -> orientation = orientation.next();
//      case Direction.LEFT -> orientation = orientation.previous();
//      case Direction.FORWARD -> position = getNewAnimalPosition(nextMove, moveValidator);
//      case Direction.BACKWARD -> position = getNewAnimalPosition(nextMove.opposite(), moveValidator);
//    }
  }

  @Override
  public String toString() {
    return switch(this.orientation){
      case NORTH -> ORIENTATION_STRING_ARRAY[0];
      case NORTH_EAST -> ORIENTATION_STRING_ARRAY[1];
      case EAST -> ORIENTATION_STRING_ARRAY[2];
      case SOUTH_EAST -> ORIENTATION_STRING_ARRAY[3];
      case SOUTH -> ORIENTATION_STRING_ARRAY[4];
      case SOUTH_WEST -> ORIENTATION_STRING_ARRAY[5];
      case WEST -> ORIENTATION_STRING_ARRAY[6];
      case NORTH_WEST -> ORIENTATION_STRING_ARRAY[7];
    };
  }

  public String toStringPositionInformation(){
    return "Animal: [position=%s, orientation=%s]".formatted(this.position, this.orientation);
  }

  //  Visitor
  @Override
  public void acceptVisitor(WorldElementVisitor visitor) {
    visitor.visit(this);
  }
}
