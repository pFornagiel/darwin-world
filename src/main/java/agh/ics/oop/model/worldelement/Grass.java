package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.util.Vector2d;

import java.util.Objects;

public class Grass implements WorldElement {
  public Vector2d position;

  public Grass(Vector2d position) {
    this.position = position;
  }

  public Vector2d getPosition() {
    return position;
  }

  @Override
  public String toString() {
    return "*";
  }

//  The Bushes should be considered equal if they occupy the same position
  @Override
  public int hashCode() {
    return Objects.hash(position);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if(other == null || getClass() != other.getClass()) {
      return false;
    }
    Grass that = (Grass) other;
    return position.equals(that.position);
  }
}
