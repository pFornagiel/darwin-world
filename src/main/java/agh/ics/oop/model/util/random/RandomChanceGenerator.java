package agh.ics.oop.model.util.random;

import java.util.Random;

/**
 * A utility class for generating random results based on a specified success chance.
 * This class allows you to simulate events with a given probability of success or failure.
 *
 * The success chance is provided as a decimal value in the range [0.0, 1.0], where:
 * - 0.0 represents a 0% chance of success (always failure)
 * - 1.0 represents a 100% chance of success (always success)
 */
public class RandomChanceGenerator {
  final static Random rand = new Random();
  private final double successChance;

  public RandomChanceGenerator(double successChance) {
    this.successChance = successChance;
  }

  public boolean randomResult() {
    return rand.nextDouble() <= successChance;
  }

  public boolean randomResultComplement() {
    return rand.nextDouble() >= successChance;
  }

}
