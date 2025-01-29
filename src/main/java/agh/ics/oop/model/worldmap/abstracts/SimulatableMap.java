package agh.ics.oop.model.worldmap.abstracts;
import agh.ics.oop.model.datacollectors.DataVisitor;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.simulation.SimulationVisitor;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.model.worldelement.abstracts.LivingCreature;

import java.util.List;
import java.util.Set;

/**
 * Represents a map that can be simulated in terms of life cycle and interactions of creatures and plants.
 * This interface defines methods for managing creatures' movements, reproduction, consumption of plants,
 * and tracking simulation statistics such as average energy, lifespan, and genotype popularity.
 *
 * @param <E> the type of living creature on this map, which must extend {@link LivingCreature}
 */
public interface SimulatableMap<E extends LivingCreature> extends WorldMap<E> {

  /**
   * Checks if the creature's energy is zero or below and removes it from the map if so.
   *
   * @param creature the creature to check and potentially kill
   */
  void killDyingCreature(E creature);

  /**
   * Creates a new creature at the specified position on the map.
   *
   * @param position the position where the new creature will be placed
   * @return the newly created creature
   */
  E createNewCreatureOnMap(Vector2d position);

  /**
   * Creates a new creature on the map from two parent creatures.
   *
   * @param parent1 the first parent creature
   * @param parent2 the second parent creature
   * @return the newly created animal
   */
  E createNewCreatureOnMap(E parent1, E parent2);

  /**
   * Rotates the creature's orientation to a new direction based on its genotype.
   *
   * @param creature the animal to rotate
   */
  void rotateCreature(E creature);

  /**
   * Moves the creature in its current orientation direction, adjusting its position on the map.
   *
   * @param creature the animal to move
   */
  void moveCreature(E creature);

  /**
   * Allows the creature at the specified position to consume a plant if present.
   * The plant is removed from the position after consumption.
   *
   * @param position the position to check for plant consumption
   */
  void consumePlant(Vector2d position);

  /**
   * Breeds two creatures at the specified position if they have sufficient energy.
   *
   * @param position the position where breeding is attempted
   */
  void breedCreatures(Vector2d position);

  /**
   * Grows a specified number of new plants on the map in valid positions.
   *
   * @param amountOfPlants the number of plants to grow
   */
  void growPlants(int amountOfPlants);

  /**
   * Returns the current number of creatures on the map.
   *
   * @return the count of creatures
   */
  int getAmountOfElements();

  /**
   * Returns the current number of plants on the map.
   *
   * @return the count of plants
   */
  int getAmountOfPlants();

  /**
   * Returns the number of map tiles that are unoccupied by creatures or plants.
   *
   * @return the count of free tiles
   */
  int getAmountOfFreeFields();

  /**
   * Calculates the average energy level of all creatures currently on the map.
   *
   * @return the average energy, or 0.0 if there are no creatures
   */
  double getAverageEnergy();

  /**
   * Calculates the average number of offspring per creature.
   *
   * @return the average number of children, or 0.0 if there are no creatures
   */
  double getAverageChildren();

  /**
   * Calculates the average lifespan of all deceased creatures.
   *
   * @return the average lifespan. Returns 0.0 if no creatures have died yet to avoid division by zero.
   */
  double getAverageLifespan();

  /**
   * Retrieves a list of genotypes sorted by their prevalence in the current creature population.
   *
   * @return a list of genotypes from most to least common
   */
  List<Genotype> getMostPopularGenotypes();

  /**
   * Increments the tracking count for the specified genotype.
   *
   * @param genotype the genotype to increment
   */
  void incrementGenotypeCount(Genotype genotype);

  /**
   * Decrements the tracking count for the specified genotype.
   *
   * @param genotype the genotype to decrement
   */
  void decrementGenotypeCount(Genotype genotype);

  /**
   * Returns a set of all map positions currently occupied by creatures or plants.
   *
   * @return a set of occupied positions
   */
  Set<Vector2d> getOccupiedMapTiles();

  /**
   * Accepts a simulation visitor to collect or process simulation data.
   *
   * @param visitor the visitor to accept
   */
  void accept(SimulationVisitor visitor);

  /**
   * Accepts a data visitor and returns the collected simulation data.
   *
   * @param visitor the data visitor to accept
   * @return the simulation data collected by the visitor
   */
  SimulationData acceptData(DataVisitor visitor);
}