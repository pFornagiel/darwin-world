package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;

public class CrazyAnimal extends Animal {
  public CrazyAnimal(Vector2d position) {
    super(position);
  }

  @Override
  public int activateNextGene() {
    return super.getGenotype().activateRandomGene();
  }
}
