package agh.ics.oop.model.util;

import agh.ics.oop.model.util.abstracts.Vector;

import java.util.Objects;

public class DoubleVector2d implements Vector<DoubleVector2d> {
  private final double x;
  private final double y;

  public DoubleVector2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public boolean precedes(DoubleVector2d other) {
    return x <= other.x && y <= other.y;
  }

  public boolean follows(DoubleVector2d other) {
    return x >= other.x && y >= other.y;
  }

  public DoubleVector2d add(DoubleVector2d other) {
    return new DoubleVector2d(x + other.x, y + other.y);
  }
  public DoubleVector2d substract(DoubleVector2d other) {
      return new DoubleVector2d(x - other.x, y - other.y);
  }

  public DoubleVector2d multiply(double scalar) {
    return new DoubleVector2d(x * scalar, y * scalar);
  }

  public DoubleVector2d upperRight(DoubleVector2d other) {
    return new DoubleVector2d(Math.max(x, other.x), Math.max(y, other.y));
  }

  public DoubleVector2d bottomLeft(DoubleVector2d other) {
    return new DoubleVector2d(Math.min(x, other.x), Math.min(y, other.y));
  }

  public DoubleVector2d opposite() {
    return new DoubleVector2d(-x, -y);
  }

  @Override
  public String toString() {
    return "(%f, %f)".formatted(x, y);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    DoubleVector2d that = (DoubleVector2d) other;
    return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x,y);
  }
}
