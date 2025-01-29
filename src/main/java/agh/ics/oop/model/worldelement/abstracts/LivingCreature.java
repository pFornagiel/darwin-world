package agh.ics.oop.model.worldelement.abstracts;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.model.exception.worldelement.CreatureStillAliveError;
import agh.ics.oop.model.exception.worldelement.CreatureAlreadyDeadError;

import java.util.List;

/**
 * Represents a living creature in the simulated world, providing core functionality
 * for movement, energy management, reproduction, and genetic behavior. Living creatures
 * have positions, orientations, energy levels, genotypes, and can interact with their environment.
 */
public interface LivingCreature extends WorldElement {

  /**
   * Returns the current Direction the creature is facing.
   * @return The creature's current facing Direction.
   */
  Direction getOrientation();

  /**
   * Sets the creature's orientation to the specified direction.
   * @param orientation The new Direction to face.
   */
  void setOrientation(Direction orientation);

  /**
   * Gets the creature's current position in the world.
   * @return The current position as a Vector2d coordinate.
   */
  Vector2d getPosition();

  /**
   * Moves the creature to a new position in the world.
   * @param position The new position to set.
   */
  void setPosition(Vector2d position);

  /**
   * Checks if the creature is located at the specified position.
   * @param position The position to check against.
   * @return true if the creature's current position matches the given position.
   */
  boolean isAt(Vector2d position);

  /**
   * Handles the creature eating a plant, increasing its energy and plant consumption count.
   */
  void eat();

  /**
   * Gets the total number of plants the creature has eaten.
   * @return The count of consumed plants.
   */
  int getEatenPlants();

  /**
   * Returns the creature's current energy level.
   * @return Current energy value.
   */
  int getEnergy();

  /**
   * Increases energy by the standard amount gained from eating a plant.
   * @return Updated energy level.
   */
  int gainEnergy();

  /**
   * Increases energy by a specified amount.
   * @param energy The amount of energy to add.
   * @return Updated energy level.
   */
  int gainEnergy(int energy);

  /**
   * Reduces energy by the standard movement cost.
   * @return Updated energy level.
   */
  int drainEnergy();

  /**
   * Reduces energy by a specified amount.
   * @param energy The amount of energy to subtract.
   * @return Updated energy level.
   */
  int drainEnergy(int energy);

  /**
   * Checks if the creature has sufficient energy to reproduce.
   * @return true if energy meets or exceeds reproduction threshold.
   */
  boolean doesHaveEnoughEnergyToReproduce();

  /**
   * Increments the creature's lifespan by one day.
   */
  void updateLifespan();

  /**
   * Gets the total number of days the creature has been alive.
   * @return Current lifespan in days.
   */
  int getLifespan();

  /**
   * Gets the day number when the creature died.
   * @return The day of death.
   * @throws CreatureStillAliveError if called on a living creature.
   */
  int getDayOfDeath();

  /**
   * Checks if the creature is currently alive.
   * @return true if the creature hasn't been killed.
   */
  boolean isAlive();

  /**
   * Marks the creature as deceased and clears its position.
   * @throws CreatureAlreadyDeadError if called on an already dead creature.
   */
  void kill();

  /**
   * Returns the creature's Genotype.
   * @return The creature's Genotype.
   */
  Genotype getGenotype();

  /**
   * Gets the gene value at a specific index in the genotype.
   * @param geneIndex The index to query.
   * @return The gene value at the specified position.
   */
  int getGene(int geneIndex);

  /**
   * Returns the current active gene index in the Genotype.
   * @return The index of the currently active gene.
   */
  int getCurrentGene();

  /**
   * Advances to and returns the next gene in the genotype sequence.
   * @return The newly activated gene value.
   */
  int activateNextGene();

  /**
   * Activates the next gene and rotates orientation based on its value.
   * Combines gene activation with directional change for movement.
   */
  void rotateAndActivate();

  /**
   * Gets a list of direct offspring created by this creature.
   * @return List of child creatures.
   */
  List<LivingCreature> getChildren();

  /**
   * Returns the number of direct offspring.
   * @return Count of immediate children.
   */
  int getAmountOfChildren();

  /**
   * Calculates total number of descendants including children's offspring.
   * @return Total descendants count.
   */
  int getAmountOfDescendants();

  /**
   * Registers a new offspring for this creature.
   * @param child The new child creature to add.
   */
  void addChild(LivingCreature child);
}