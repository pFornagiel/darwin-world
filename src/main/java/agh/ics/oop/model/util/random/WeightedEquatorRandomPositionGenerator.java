package agh.ics.oop.model.util.random;

import agh.ics.oop.model.exception.util.random.RandomPositionOutOfRangeException;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class WeightedEquatorRandomPositionGenerator implements Iterable<Vector2d> {
  private static final Random rand = new Random();
  private static final double MEAN_CONSTANT = 2.;
  private static final double VARIANCE_CONSTANT= 8.;

  private final int maxHeight;
  private int numberOfElements;

  private final ArrayList<ArrayList<Vector2d>> rowArray = new ArrayList<>();
  private final double[] rowWeights;
  private double totalWeight;


  public WeightedEquatorRandomPositionGenerator(int maxWidth, int maxHeight, int numberOfElements) {
//    Inclusive range (0,0) - (maxWidth, maxHeight)
    maxWidth += 1;
    maxHeight += 1;
    this.maxHeight = maxHeight;
    this.numberOfElements = numberOfElements;

    for (int i = 0; i < this.maxHeight; i++) {
      ArrayList<Vector2d> row = new ArrayList<>();
      for (int j = 0; j < maxWidth; j++) {
        row.add(new Vector2d(j,i));
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

    ArrayList<Vector2d> currentRow = rowArray.get(row);
    int idx = rand.nextInt(currentRow.size());
    Vector2d randomVector = currentRow.get(idx);
    currentRow.remove(idx);
    if(currentRow.isEmpty()) {
      totalWeight -= rowWeights[row];
    }
    return randomVector;
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
