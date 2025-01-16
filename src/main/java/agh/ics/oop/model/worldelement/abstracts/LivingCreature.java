package agh.ics.oop.model.worldelement.abstracts;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.worldelement.util.Genotype;

import java.util.List;

public interface LivingCreature extends WorldElement {
  Direction getOrientation();
  void setOrientation(Direction orientation);
  Vector2d getPosition();
  void setPosition(Vector2d position);

  void eat();
  int getEatenPlants();

  int getEnergy();
  int gainEnergy();
  int gainEnergy(int energy);
  int drainEnergy();
  int drainEnergy(int energy);
  boolean doesHaveEnoughEnergyToReproduce();

  void updateLifespan();
  int getLifespan();
  int getDayOfDeath();
  boolean isAlive();
  void kill();

  Genotype getGenotype();
  int getGene(int geneIndex);
  int getCurrentGene();
  int activateNextGene();
  void rotateAndActivate();

  List<LivingCreature> getChildren();
  int getAmountOfChildren();
  int getAmountOfDescendants();
  void addChild(LivingCreature child);
}
