package agh.ics.oop.model.util;

public enum Direction {
  NORTH,
  NORTH_EAST,
  EAST,
  SOUTH_EAST,
  SOUTH,
  SOUTH_WEST,
  WEST,
  NORTH_WEST;

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
    return switch(this){
      case NORTH -> DIRECTION_NAMES[0];
      case NORTH_EAST -> DIRECTION_NAMES[1];
      case EAST -> DIRECTION_NAMES[2];
      case SOUTH_EAST -> DIRECTION_NAMES[3];
      case SOUTH -> DIRECTION_NAMES[4];
      case SOUTH_WEST -> DIRECTION_NAMES[5];
      case WEST -> DIRECTION_NAMES[6];
      case NORTH_WEST -> DIRECTION_NAMES[7];
    };
  }

  public Direction next() {
    return switch(this) {
      case NORTH -> NORTH_EAST;
      case NORTH_EAST -> EAST;
      case EAST -> SOUTH_EAST;
      case SOUTH_EAST -> SOUTH;
      case SOUTH -> SOUTH_WEST;
      case SOUTH_WEST -> WEST;
      case WEST -> NORTH_WEST;
      case NORTH_WEST -> NORTH;
    };
  }

  public Direction previous() {
    return switch(this) {
      case NORTH -> NORTH_WEST;
      case NORTH_WEST -> WEST;
      case WEST -> SOUTH_WEST;
      case SOUTH_WEST -> SOUTH;
      case SOUTH -> SOUTH_EAST;
      case SOUTH_EAST -> EAST;
      case EAST -> NORTH_EAST;
      case NORTH_EAST -> NORTH;
    };
  }

  public Direction opposite() {
    return switch(this) {
      case NORTH -> SOUTH;
      case NORTH_EAST -> SOUTH_WEST;
      case EAST -> WEST;
      case SOUTH_EAST -> NORTH_WEST;
      case SOUTH -> NORTH;
      case SOUTH_WEST -> NORTH_EAST;
      case WEST -> EAST;
      case NORTH_WEST -> SOUTH_EAST;
    };
  }

  public Direction rotate(int n){
    int currentIndex = this.ordinal();
    return Direction.values()[(currentIndex + n) % Direction.values().length];
  }

  public Vector2d toUnitVector() {
    return switch(this) {
      case NORTH -> DIRECTION_UNIT_VECTORS[0];
      case NORTH_EAST -> DIRECTION_UNIT_VECTORS[1];
      case EAST -> DIRECTION_UNIT_VECTORS[2];
      case SOUTH_EAST -> DIRECTION_UNIT_VECTORS[3];
      case SOUTH -> DIRECTION_UNIT_VECTORS[4];
      case SOUTH_WEST -> DIRECTION_UNIT_VECTORS[5];
      case WEST -> DIRECTION_UNIT_VECTORS[6];
      case NORTH_WEST -> DIRECTION_UNIT_VECTORS[7];
    };
  }





}
