package agh.ics.oop.model.util.random;

import agh.ics.oop.model.util.Vector2d;

import java.util.Iterator;
import java.util.Random;

public class RandomRepeatingPositionGenerator implements Iterable<Vector2d>{
  private final int maxWidth;
  private final int maxHeight;
  private int numberOfElements;

  private static final Random rand = new Random();

  public RandomRepeatingPositionGenerator(int maxWidth, int maxHeight, int numberOfElements) {
//    Exclusive range (0,0) - (maxWidth, maxHeight)
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
    this.numberOfElements = numberOfElements;
  }

  public Vector2d get() {
    numberOfElements--;
    return new Vector2d(rand.nextInt(maxWidth), rand.nextInt(maxHeight));
  }

  @Override
  public Iterator<Vector2d> iterator() {
    return new RandomPositionGeneratorIterator();
  }

  private class RandomPositionGeneratorIterator implements Iterator<Vector2d> {

    @Override
    public boolean hasNext() {
      return numberOfElements > 0;
    }

    @Override
    public Vector2d next() {
      return get();
    }
  }
}
