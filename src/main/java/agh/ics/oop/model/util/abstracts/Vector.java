package agh.ics.oop.model.util.abstracts;

public interface Vector<T extends Vector<T>> {

  T add(T other);
  T substract(T other);
  T opposite();

  boolean precedes(T other);
  boolean follows(T other);

  T upperRight(T other);
  T bottomLeft(T other);

  @Override
  String toString();

  @Override
  boolean equals(Object other);

  @Override
  int hashCode();
}