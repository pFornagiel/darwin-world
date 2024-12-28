package agh.ics.oop.model.util.random;

import java.util.Random;

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
