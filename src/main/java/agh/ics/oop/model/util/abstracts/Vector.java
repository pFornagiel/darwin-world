package agh.ics.oop.model.util.abstracts;

/**
 * Represents a vector with common operations such as addition, subtraction, and comparisons.
 * Implementing classes define the dimensionality and component-specific logic.
 *
 * @param <T> the type of the vector implementing this interface (enables method chaining and type safety).
 */
public interface Vector<T extends Vector<T>> {

  /**
   * Adds the components of the specified vector to this vector and returns the result as a new instance.
   *
   * @param other the vector to add (must not be null).
   * @return a new vector resulting from the addition.
   */
  T add(T other);

  /**
   * Subtracts the components of the specified vector from this vector and returns the result as a new instance.
   *
   * @param other the vector to subtract (must not be null).
   * @return a new vector resulting from the subtraction.
   */
  T substract(T other);

  /**
   * Returns a new vector with all components of this vector negated.
   *
   * @return the opposite vector (e.g., {@code (-x, -y)} for 2D).
   */
  T opposite();

  /**
   * Checks if all components of this vector are less than or equal to the corresponding components of the specified vector.
   *
   * @param other the vector to compare against (must not be null).
   * @return {@code true} if all components of this vector are ≤ those of {@code other}, {@code false} otherwise.
   */
  boolean precedes(T other);

  /**
   * Checks if all components of this vector are greater than or equal to the corresponding components of the specified vector.
   *
   * @param other the vector to compare against (must not be null).
   * @return {@code true} if all components of this vector are ≥ those of {@code other}, {@code false} otherwise.
   */
  boolean follows(T other);

  /**
   * Returns a new vector where each component is the maximum of the corresponding components of this vector and the specified vector.
   *
   * @param other the vector to compare (must not be null).
   * @return the "upper right" vector (e.g., maximum coordinates in 2D).
   */
  T upperRight(T other);

  /**
   * Returns a new vector where each component is the minimum of the corresponding components of this vector and the specified vector.
   *
   * @param other the vector to compare (must not be null).
   * @return the "bottom left" vector (e.g., minimum coordinates in 2D).
   */
  T bottomLeft(T other);

  /**
   * Returns a string representation of the vector's components (format depends on implementation).
   *
   * @return formatted string (e.g., {@code "(x, y)"} for 2D).
   */
  @Override
  String toString();

  /**
   * Compares this vector with the specified object for equality.
   *
   * @param other the object to compare with.
   * @return {@code true} if {@code other} is equal to this vector, {@code false} otherwise.
   */
  @Override
  boolean equals(Object other);

  /**
   * Returns a hash code value consistent with the vector's component values.
   *
   * @return a hash code value for this vector.
   */
  @Override
  int hashCode();
}