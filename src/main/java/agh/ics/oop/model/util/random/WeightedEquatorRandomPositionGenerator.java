package agh.ics.oop.model.util.random;

import agh.ics.oop.model.exception.util.random.RandomPositionOutOfRangeException;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class WeightedEquatorRandomPositionGenerator implements Iterable<Vector2d> {
  private static final Random rand = new Random();
  private static final double VERDANT_TILES_PERCENTAGE = .2;
  private static final double MEAN_CONSTANT = 2.;
  private static final double VARIANCE_CONSTANT= 8.;

  private final int maxHeight;
  private final int maxWidth;
  private int numberOfElements;

  private final ArrayList<ArrayList<Integer>> rowArray = new ArrayList<>();
  private final double[] rowWeights;
  private double totalWeight;


  public WeightedEquatorRandomPositionGenerator(int maxWidth, int maxHeight) {
//    Exclusive range (0,0) : (maxWidth, maxHeight)
    this.maxHeight = maxHeight;
    this.maxWidth = maxWidth;
    this.numberOfElements = (int)(maxWidth * maxHeight * VERDANT_TILES_PERCENTAGE);

    for (int i = 0; i < this.maxHeight; i++) {
      ArrayList<Integer> row = new ArrayList<>();
      for (int j = 0; j < maxWidth; j++) {
        row.add((maxWidth * i) + j);
      }
      rowArray.add(row);
    }

//    Simulate gaussian distribution along rows, with center of mass in the "equator" (middle of the rows)
    this.rowWeights = new double[this.maxHeight];
    for (int i = 0; i < this.maxHeight; i++) {
//      Gaussian PDF
      rowWeights[i] = Math.exp(-Math.pow((i - (this.maxHeight / MEAN_CONSTANT)) / (this.maxHeight / VARIANCE_CONSTANT), 2));
      totalWeight += rowWeights[i];
    }
  }

  public Vector2d get(int row) {
    numberOfElements--;
    if(numberOfElements < 0) {
      throw new RandomPositionOutOfRangeException();
    }

    ArrayList<Integer> currentRow = rowArray.get(row);
    int idx = rand.nextInt(currentRow.size());
    int currentNumber = currentRow.get(idx);
    Vector2d currentVector = new Vector2d(currentNumber%this.maxWidth, currentNumber/this.maxWidth);
    currentRow.remove(idx);
    if(currentRow.isEmpty()) {
      totalWeight -= rowWeights[row];
    }
    return currentVector;
  }

  @Override
  public Iterator<Vector2d> iterator() {
    return new WeightedEquatorRandomPositionGeneratorIterator();
  }

  private class WeightedEquatorRandomPositionGeneratorIterator implements Iterator<Vector2d> {


    @Override
    public boolean hasNext() {
      return numberOfElements > 0;
    }

    @Override
    public Vector2d next() {
      double random = rand.nextDouble() * totalWeight;
      double cumulative_probability = 0;
      for (int i = 0; i <= maxHeight; i++) {
        if(rowArray.get(i).isEmpty()) continue;

        cumulative_probability += rowWeights[i];
        if(random <= cumulative_probability){
          return get(i);
        }
      }
      throw new RandomPositionOutOfRangeException();
    }
  }
}
