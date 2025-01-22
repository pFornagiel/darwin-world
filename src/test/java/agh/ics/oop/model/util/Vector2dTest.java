package agh.ics.oop.model.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {

  @Test
  public void inequalityForTwoDifferentVectors() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(1, -1);

    // Then
    assertNotEquals(V1, V2, "Vectors V1 and V2 should not be equal");
  }

  @Test
  public void equalityForTwoSameVectors() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(1, 1);

    // Then
    assertEquals(V1, V2, "Vectors V1 and V2 should be equal");
  }

  @Test
  public void properStringFormatting() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);

    // Then
    assertEquals("(1, 1)", V1.toString(), "String representation of V1 should be (1, 1)");
    assertNotEquals("(1,1)", V1.toString(), "String representation of V1 should not be (1,1)");
  }

  @Test
  public void precedingVectors() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(0, 1);
    Vector2d V3 = new Vector2d(2, 1);

    // When & Then
    assertTrue(V2.precedes(V1), "Vector V2 should precede V1");
    assertFalse(V3.precedes(V1), "Vector V3 should not precede V1");
  }

  @Test
  public void testFollows() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(2, 1);
    Vector2d V3 = new Vector2d(0, 1);

    // Then
    assertFalse(V3.follows(V1), "Vector V3 should not follow V1");
    assertTrue(V2.follows(V1), "Vector V2 should follow V1");
  }

  @Test
  public void testUpperRight() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(2, 0);
    Vector2d V3 = new Vector2d(2, 1);
    Vector2d V4 = new Vector2d(0, 0);
    Vector2d V5 = new Vector2d(0, 1);

    // Then
    assertEquals(V3, V1.upperRight(V2), "Upper right of V1 and V2 should be V3");
    assertNotEquals(V5, V1.upperRight(V4), "Upper right of V1 and V4 should not be V5");
  }

  @Test
  public void testBottomLeft() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(2, 0);
    Vector2d V3 = new Vector2d(2, 1);
    Vector2d V4 = new Vector2d(0, 0);

    // Then
    assertNotEquals(V3, V1.bottomLeft(V2), "Bottom left of V1 and V2 should not be V3");
    assertEquals(V4, V1.bottomLeft(V4), "Bottom left of V1 and V4 should be V4");
  }

  @Test
  public void testAdd() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(2, 2);
    Vector2d V3 = new Vector2d(3, 3);

    // Then
    assertEquals(V3, V1.add(V2), "Sum of V1 and V2 should be V3");
  }

  @Test
  public void testSubtract() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(2, 2);
    Vector2d V3 = new Vector2d(-1, -1);

    // Then
    assertEquals(V3, V1.substract(V2), "Difference between V1 and V2 should be V3");
  }

  @Test
  public void testOpposite() {
    // Given
    Vector2d V1 = new Vector2d(1, 1);
    Vector2d V2 = new Vector2d(-1, -1);

    // Then
    assertEquals(V2, V1.opposite(), "Opposite of V1 should be V2");
  }
}
