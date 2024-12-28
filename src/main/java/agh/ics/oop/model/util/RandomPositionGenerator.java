package agh.ics.oop.model.util;

import java.util.Iterator;
import java.util.Random;

public class RandomPositionGenerator implements Iterable<Vector2d> {
  private final int maxWidth;
  private final int maxHeight;
  private int numberOfElements;
  private final int[] numberArray;
  private int currentLenght;

  public RandomPositionGenerator(int maxWidth, int maxHeight, int numberOfElements) {
//    Inclusive range (0,0) - (maxWidth, maxHeight)
    this.maxWidth = maxWidth+1;
    this.maxHeight = maxHeight+1;
    this.numberOfElements = numberOfElements;
    this.currentLenght = maxWidth * maxHeight;
    this.numberArray = new int[currentLenght];
    for (int i = 0; i < currentLenght; i++) {
      numberArray[i] = i;
    }

  }

  public Vector2d get(int index) {
    currentLenght -= 1;
    numberOfElements--;
    int number = numberArray[index];
    numberArray[index] = numberArray[currentLenght];
    numberArray[currentLenght] = number;
    return new Vector2d(number / maxWidth, number % maxHeight);
  }



  @Override
  public Iterator<Vector2d> iterator() {
    return new RandomPositionGeneratorIterator();
  }

  private class RandomPositionGeneratorIterator implements Iterator<Vector2d> {
    private static final Random rand = new Random();


    @Override
    public boolean hasNext() {
      return numberOfElements > 0;
    }

    @Override
    public Vector2d next() {
      return get(rand.nextInt(currentLenght));
    }
  }
}
