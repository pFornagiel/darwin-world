package agh.ics.oop.model;

public enum MapDirection {
  NORTH,
  SOUTH,
  WEST,
  EAST;

  private final static Vector2d[] DIRECTION_UNIT_VECTORS = {
      new Vector2d(0,1),
      new Vector2d(1,0) ,
      new Vector2d(0,-1),
      new Vector2d(-1,0)
  };

  private final static String[] DIRECTION_NAMES = {
      "Północ",
      "Wschód",
      "Południe",
      "Zachód"
  };

  @Override
  public String toString(){
    return switch(this){
      case NORTH -> DIRECTION_NAMES[0];
      case EAST -> DIRECTION_NAMES[1];
      case SOUTH -> DIRECTION_NAMES[2];
      case WEST -> DIRECTION_NAMES[3];
    };
  }

  public MapDirection next(){
    return switch(this){
      case NORTH -> EAST;
      case EAST -> SOUTH;
      case SOUTH -> WEST;
      case WEST -> NORTH;
    };
  }

  public MapDirection previous(){
    return switch(this){
      case NORTH -> WEST;
      case EAST -> NORTH;
      case SOUTH -> EAST;
      case WEST -> SOUTH;
    };
  }

  public Vector2d toUnitVector(){
    return switch(this){
      case NORTH -> DIRECTION_UNIT_VECTORS[0];
      case EAST -> DIRECTION_UNIT_VECTORS[1];
      case SOUTH -> DIRECTION_UNIT_VECTORS[2];
      case WEST -> DIRECTION_UNIT_VECTORS[3];
    };
  }


}
