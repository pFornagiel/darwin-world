package agh.ics.oop.model.exception.worldelement;

import agh.ics.oop.model.worldelement.Animal;

public class NotEnoughEnergyToReproduceException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Animal %s does not have enough energy to reproduce: Needed energy: %d, Animal energy: %d.";

  public NotEnoughEnergyToReproduceException(Animal firstAnimal, Animal secondAnimal, int energyNeededToReproduce ) {
    super(
        firstAnimal.getEnergy() < energyNeededToReproduce ?
        ERROR_MESSAGE.formatted(firstAnimal, energyNeededToReproduce, firstAnimal.getEnergy()) :
        ERROR_MESSAGE.formatted(secondAnimal, energyNeededToReproduce, secondAnimal.getEnergy())
    );
  }
}
