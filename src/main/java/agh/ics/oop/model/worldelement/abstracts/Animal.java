package agh.ics.oop.model.worldelement.abstracts;

import agh.ics.oop.model.datacollectors.AnimalStatistics;
import agh.ics.oop.model.datacollectors.DataVisitor;
import agh.ics.oop.model.exception.worldelement.CreatureAlreadyDeadError;
import agh.ics.oop.model.exception.worldelement.CreatureStillAliveError;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.worldelement.util.Genotype;

import java.util.*;

public abstract class Animal implements WorldElement, LivingCreature, Comparable<Animal> {

  private static final Random rand = new Random();
  private static final int ENERGY_USED_FOR_MOVEMENT = 1;
  private int energyGainedByEating = 0;
  private int energyNeededForReproduction = 0;

  protected Genotype genotype;

  private Direction orientation;
  private Vector2d position;
  private int energy = 0;
  private int plantEatenCounter;
  private int lifespan = 0;
  private int dayOfDeath = -1;

  private final UUID id;

  private final HashSet<LivingCreature> childrenSet = new HashSet<>();

//  for testing purposes
  private static final String[] ORIENTATION_STRING_ARRAY = {"^","↗",">","↘","v","↙","<","↖"};

  public Animal(Vector2d position){
    this.id = UUID.randomUUID();
    this.orientation = Direction.values()[rand.nextInt(Direction.values().length)];
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
  public void setEnergyNeededForReproduction(int energyNeededForReproduction){this.energyNeededForReproduction = energyNeededForReproduction;}

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
    this.energy -= ENERGY_USED_FOR_MOVEMENT;
    return this.energy;
  }
  public int drainEnergyDuringReproduction(){
    this.energy -= energyNeededForReproduction;
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
  @Override
  public boolean doesHaveEnoughEnergyToReproduce(){
    return energy >= energyNeededForReproduction;
  }
  
  //  Lifespan
  @Override
  public int getLifespan() {
    return lifespan;
  }
  @Override
  public void updateLifespan(){
    lifespan++;
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

  @Override
  public boolean isAlive() {
    return dayOfDeath == -1;
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
  public int activateNextGene(){
    return genotype.activateNextGene();
  }
  @Override
  public int getCurrentGene(){
    return genotype.getCurrentGene();
  }

//  Position and direction
  @Override
  public Vector2d getPosition() {
    return position;
  }
  @Override
  public void setPosition(Vector2d position){
    this.position = position;
  }
  @Override
  public Direction getOrientation() {
    return orientation;
  }
  @Override
  public void setOrientation(Direction orientation) {
    this.orientation = orientation;
  }

  public void rotateAndActivate(){
    int gene = activateNextGene();
    orientation = orientation.rotate(gene);
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

  @Override
  public int compareTo(Animal other) {
    // Compare by energy
    if (this.energy != other.energy) {
      return Integer.compare(other.energy, this.energy);
    }

    // Compare by lifespan
    if (this.lifespan != other.lifespan) {
      return Integer.compare(other.lifespan, this.lifespan);
    }

    // Compare by number of children
    if (this.getAmountOfChildren() != other.getAmountOfChildren()) {
      return Integer.compare(other.getAmountOfChildren(), this.getAmountOfChildren());
    }

    return rand.nextInt(2) * 2 - 1; // random -1 or 1
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Animal other = (Animal) obj;
    return Objects.equals(id, other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
