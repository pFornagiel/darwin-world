package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.random.RandomChanceGenerator;
import agh.ics.oop.model.worldelement.abstracts.Animal;

public class CrazyAnimal extends Animal {
  private static final double CRAZINESS_CHANCE = .2;
  private static final RandomChanceGenerator chanceGenerator = new RandomChanceGenerator(CRAZINESS_CHANCE);

  public CrazyAnimal(Vector2d position) {
    super(position);
  }

  @Override
  public int activateNextGene() {
    if(chanceGenerator.randomResult()){
      return genotype.activateRandomGene();
    }
    return super.activateNextGene();
  }
}
