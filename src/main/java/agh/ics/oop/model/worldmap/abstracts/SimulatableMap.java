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

  void killDyingCreature(E creature);

  E createNewAnimalOnMap(Vector2d position);

  E createNewAnimalOnMap(E parent1, E parent2);

  void rotateCreature(E animal);

  void moveCreature(E animal);

  void consumePlant(Vector2d position);

  void breedCreatures(Vector2d position);

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
