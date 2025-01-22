package agh.ics.oop.model.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

  @Test
  public void testToString() {
    assertEquals("North", Direction.NORTH.toString(), "toString should return 'North' for NORTH");
    assertEquals("South-East", Direction.SOUTH_EAST.toString(), "toString should return 'South-East' for SOUTH_EAST");
  }

  @Test
  public void testGetRandomDirection() {
    Direction randomDirection = Direction.getRandomDirection();
    assertNotNull(randomDirection, "getRandomDirection should not return null");
    assertTrue(randomDirection instanceof Direction, "getRandomDirection should return a valid Direction");
  }

  @Test
  public void testNext() {
    assertEquals(Direction.NORTH_EAST, Direction.NORTH.next(), "Next of NORTH should be NORTH_EAST");
    assertEquals(Direction.NORTH, Direction.NORTH_WEST.next(), "Next of NORTH_WEST should be NORTH");
  }

  @Test
  public void testPrevious() {
    System.out.println(Direction.NORTH.next());
    assertEquals(Direction.NORTH_WEST, Direction.NORTH.previous(), "Previous of NORTH should be NORTH_WEST");
    assertEquals(Direction.NORTH, Direction.NORTH_EAST.previous(), "Previous of NORTH_EAST should be NORTH");
  }

  @Test
  public void testOpposite() {
    assertEquals(Direction.SOUTH, Direction.NORTH.opposite(), "Opposite of NORTH should be SOUTH");
    assertEquals(Direction.NORTH_WEST, Direction.SOUTH_EAST.opposite(), "Opposite of SOUTH_EAST should be NORTH_WEST");
  }

  @Test
  public void testRotate() {
    assertEquals(Direction.EAST, Direction.NORTH.rotate(2), "Rotating NORTH by 2 should result in EAST");
    assertEquals(Direction.NORTH_WEST, Direction.NORTH.rotate(-1), "Rotating NORTH by -1 should result in NORTH_WEST");
  }

  @Test
  public void testToUnitVector() {
    assertEquals(new Vector2d(0, -1), Direction.NORTH.toUnitVector(), "Unit vector of NORTH should be (0, -1)");
    assertEquals(new Vector2d(-1, 1), Direction.SOUTH_WEST.toUnitVector(), "Unit vector of SOUTH_WEST should be (-1, 1)");
  }

}
