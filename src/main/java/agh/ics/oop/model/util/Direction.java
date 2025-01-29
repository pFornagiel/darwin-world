package agh.ics.oop.model.util;

import java.util.Random;

/**
 * Represents the eight directions in a 2D coordinate system.
 * Each direction is associated with a unit vector and a name.
 *
 * <p>The enum provides utility methods for:
 * <ul>
 *   <li>Getting a random direction</li>
 *   <li>Finding the next, previous, and opposite direction</li>
 *   <li>Rotating by a specified number of steps</li>
 *   <li>Converting to a unit vector representation</li>
 * </ul>
 *
 * @see Vector2d
 */
public enum Direction {
  NORTH,
  NORTH_EAST,
  EAST,
  SOUTH_EAST,
  SOUTH,
  SOUTH_WEST,
  WEST,
  NORTH_WEST;

  private static final Random rand = new Random();

  private final static Vector2d[] DIRECTION_UNIT_VECTORS = {
      new Vector2d(0,-1),
      new Vector2d(1,-1),
      new Vector2d(1,0),
      new Vector2d(1,1),
      new Vector2d(0,1),
      new Vector2d(-1,1),
      new Vector2d(-1,0),
      new Vector2d(-1,-1),
  };

  private final static String[] DIRECTION_NAMES = {
      "North",
      "North-East",
      "East",
      "South-East",
      "South",
      "South-West",
      "West",
      "North-West",
  };

  @Override
  public String toString(){
    int currentIndex = this.ordinal();
    return DIRECTION_NAMES[currentIndex];
  }

  public static Direction getRandomDirection(){
    Direction[] values = Direction.values();
    return values[rand.nextInt(values.length)];
  }

  public Direction next() {
    int currentIndex = this.ordinal();
    Direction[] values = Direction.values();
    return values[(currentIndex + 1) % Direction.values().length];

  }

  public Direction previous() {
    int currentIndex = this.ordinal();
    Direction[] values = Direction.values();
    return values[Math.floorMod(currentIndex - 1, values.length)];
  }

  public Direction opposite() {
    int currentIndex = this.ordinal();
    Direction[] values = Direction.values();
    return values[(currentIndex + values.length/2) % Direction.values().length];
  }

  public Direction rotate(int n){
    int currentIndex = this.ordinal();
    Direction[] values = Direction.values();
    return values[Math.floorMod(currentIndex + n, values.length)];
  }

  public Vector2d toUnitVector() {
    int currentIndex = this.ordinal();
    return DIRECTION_UNIT_VECTORS[currentIndex];
  }


}
