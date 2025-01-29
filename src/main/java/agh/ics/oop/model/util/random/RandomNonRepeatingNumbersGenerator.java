package agh.ics.oop.model.util.random;

import java.util.Iterator;
import java.util.Random;

/**
 * A generator for producing random, non-repeating numbers within a specified range.
 * The numbers are selected from the range [0, upperBound), ensuring no repeats.
 *
 * This class implements the {@link Iterable} interface, allowing iteration over
 * the sequence of random non-repeating numbers.
 */
public class RandomNonRepeatingNumbersGenerator implements Iterable<Integer> {
  private static final Random rand = new Random();

  private final int[] numberArray;

  private int amountOfNumbers;
  private int currentLength;

  public RandomNonRepeatingNumbersGenerator(int upperBound, int amountOfNumbers) {
//  0 (inclusive) : upperBound (exclusive)
    this.currentLength = upperBound;
    this.amountOfNumbers = amountOfNumbers;
    this.numberArray = new int[upperBound];
    for (int i = 0; i < currentLength; i++) {
      numberArray[i] = i;
    }

  }

  public int get() {
    int randomIndex = rand.nextInt(currentLength);
    amountOfNumbers--;
    currentLength--;
    int number = numberArray[randomIndex];
    numberArray[randomIndex] = numberArray[currentLength];
    numberArray[currentLength] = number;
    return number;
  }

  @Override
  public Iterator<Integer> iterator() {
    return new RandomNonRepeatingNumbersGeneratorIterator();
  }

  private class RandomNonRepeatingNumbersGeneratorIterator implements Iterator<Integer> {

    @Override
    public boolean hasNext() {
      return currentLength > 0 && amountOfNumbers > 0;
    }

    @Override
    public Integer next() {
      return get();
    }
  }
}