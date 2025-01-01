package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.util.MoveValidator;

import java.util.List;

public interface LivingCreature extends WorldElement {
  void move(Direction direction, MoveValidator moveValidator);

  void eat();
  int getEatenPlants();

  int getEnergy();
  int gainEnergy();
  int gainEnergy(int energy);
  int drainEnergy();
  int drainEnergy(int energy);

  void updateLifespan();
  int getLifespan();
  int getDayOfDeath();
  void kill();

  Genotype getGenotype();
  int getGene(int geneIndex);
  int getNextGene();
  int getRandomGene();

  List<LivingCreature> getChildren();
  int getAmountOfChildren();
  int getAmountOfDescendants();
  void addChild(LivingCreature child);
}
