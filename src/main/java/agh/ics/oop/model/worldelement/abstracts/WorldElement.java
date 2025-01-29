package agh.ics.oop.model.worldelement.abstracts;

import agh.ics.oop.model.util.Vector2d;

/**
 * Represents a basic element within the simulated world that has a position
 * and provides a textual representation.
 */
public interface WorldElement {

  /**
   * Gets the current position of this element in the world coordinate system.
   * @return Vector2d representing the element's position coordinates
   */
  Vector2d getPosition();

  /**
   * Provides a string representation of the world element, typically used
   * for map visualization and debugging purposes. Implementations should
   * return a symbol or short identifier representing the element type.
   * @return Character(s) representing this element in text-based displays
   */
  String toString();
}

