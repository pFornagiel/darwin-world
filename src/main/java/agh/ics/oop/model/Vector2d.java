package agh.ics.oop.model;

public class Vector2d {
  private final int x;
  private final int y;

  public Vector2d(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }

  public boolean precedes(Vector2d other){
    return other.x <= x && other.y <= y;
  }

  public boolean follows(Vector2d other){
    return other.x >= x && other.y >= y;
  }

  public Vector2d add(Vector2d other){
    return new Vector2d(x + other.x, y + other.y);
  }

  public Vector2d substract(Vector2d other){
    return new Vector2d(x-other.x, y-other.y);
  }

  public Vector2d upperRight(Vector2d other){
    return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
  }

  public Vector2d bottomLeft(Vector2d other){
    return new Vector2d(Math.min(x, other.x), Math.min(y,other.y));
  }

  public Vector2d opposite(){
    return new Vector2d(-x, -y);
  }


  @Override
  public String toString(){
    return "(%d, %d)".formatted(x, y);
  }

  @Override
  public boolean equals(Object other){
    if (this == other)
      return true;
    if(other == null || getClass() != other.getClass()) {
      return false;
    }
    Vector2d that = (Vector2d) other;
    return x == that.x && y == that.y;
  }

  @Override
  public int hashCode(){
    return ((17 * 73) ^ x) << 7 + y;
  }
}
