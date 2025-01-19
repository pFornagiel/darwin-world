package agh.ics.oop.model.worldmap.abstracts;
import agh.ics.oop.model.datacollectors.DataVisitor;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.simulation.SimulationVisitor;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.model.worldelement.abstracts.LivingCreature;

import java.util.List;
import java.util.Set;

public interface SimulatableMap<E extends LivingCreature> extends WorldMap<E> {
  // Method to handle the killing of a dying animal
  void killDyingCreature(E creature);

  // Method to create a new animal with given position
  E createNewAnimalOnMap(Vector2d position);

  // Method to create a new animal through breeding
  E createNewAnimalOnMap(E parent1, E parent2);

  // Method to rotate an animal
  void rotateCreature(E animal);

  // Method to move an animal
  void moveCreature(E animal);

  // Method to handle consumption of plants by animals
  void consumePlant(Vector2d position);
  // Method to breed animals at a specific position
  void breedCreatures(Vector2d position);
  
  // Method to grow a specified amount of plants
  void growPlants(int amountOfPlants);

  int getAmountOfElements();

  int getAmountOfPlants();

  int getAmountOfFreeFields();

  double getAverageEnergy();

  double getAverageChildren();

  double getAverageLifespan();

  List<Genotype> getMostPopularGenotypes();

  void incrementGenotypeCount(Genotype genotype);

  void decrementGenotypeCount(Genotype genotype);

  Set<Vector2d> getOccupiedMapTiles();

  void accept(SimulationVisitor visitor);

  SimulationData acceptData(DataVisitor visitor);
}
