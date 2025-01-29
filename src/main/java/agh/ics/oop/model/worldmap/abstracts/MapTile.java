package agh.ics.oop.model.worldmap.abstracts;

import agh.ics.oop.model.worldelement.abstracts.WorldElement;
import agh.ics.oop.model.exception.worldmap.PlantAlreadyGrownException;
import agh.ics.oop.model.exception.worldmap.PlantNotGrownException;

import java.util.List;
import java.util.Set;
/**
 * Represents a tile on the world map that can contain elements and manage plant growth.
 *
 * @param <E> the type of elements contained in this tile, which must extend {@link WorldElement}
 */
public interface MapTile<E extends WorldElement> {

  /**
   * Checks if this tile is verdant (preferred by plants).
   *
   * @return {@code true} if the tile is verdant, {@code false} otherwise
   */
  boolean isVerdant();

  /**
   * Marks this tile as verdant.
   */
  void setVerdant();

  /**
   * Checks if a plant is currently grown on this tile.
   *
   * @return {@code true} if a plant is present, {@code false} otherwise
   */
  boolean isPlantGrown();

  /**
   * Grows a plant on this tile. The tile must not already have a plant.
   *
   * @throws PlantAlreadyGrownException if a plant is already present on this tile
   */
  void growPlant();

  /**
   * Removes the plant from this tile. The tile must have a plant present.
   *
   * @throws PlantNotGrownException if no plant exists on this tile
   */
  void eatPlant();

  /**
   * Checks if any elements are present on this tile.
   *
   * @return {@code true} if at least one element is present, {@code false} otherwise
   */
  boolean isOccupied();

  /**
   * Adds an element to this tile.
   *
   * @param element the element to add
   * @return {@code true} if the element was added successfully, {@code false} if it was already present
   */
  boolean addElement(E element);

  /**
   * Removes an element from this tile.
   *
   * @param element the element to remove
   * @return {@code true} if the element was removed successfully, {@code false} if it was not present
   */
  boolean removeElement(E element);

  /**
   * Checks if the specified element is present on this tile.
   *
   * @param element the element to check for
   * @return {@code true} if the element is present, {@code false} otherwise
   */
  boolean containsElement(E element);

  /**
   * Returns an unmodifiable view of the elements on this tile.
   *
   * @return an unmodifiable {@link Set} containing all elements on this tile
   */
  Set<E> getElementSet();

  /**
   * Returns a copied list of the elements on this tile.
   *
   * @return a {@link List} containing all elements on this tile, in no specific order
   */
  List<E> getElementList();
}