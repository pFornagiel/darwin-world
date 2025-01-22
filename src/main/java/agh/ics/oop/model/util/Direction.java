package agh.ics.oop.model.util;

import java.util.Random;

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
    return values[Math.abs(currentIndex - 1) % Direction.values().length];
  }

  public Direction opposite() {
    int currentIndex = this.ordinal();
    Direction[] values = Direction.values();
    return values[(currentIndex + values.length/2) % Direction.values().length];
  }

  public Direction rotate(int n){
    int currentIndex = this.ordinal();
    return Direction.values()[Math.abs(currentIndex + n) % Direction.values().length];
  }

  public Vector2d toUnitVector() {
    int currentIndex = this.ordinal();
    return DIRECTION_UNIT_VECTORS[currentIndex];
  }


}
