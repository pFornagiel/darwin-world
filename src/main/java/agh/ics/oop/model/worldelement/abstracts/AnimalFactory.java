package agh.ics.oop.model.worldelement.abstracts;

import agh.ics.oop.model.configuration.BehaviorVariant;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.BaseAnimal;
import agh.ics.oop.model.worldelement.CrazyAnimal;
import agh.ics.oop.model.util.Direction;

import java.util.Random;

public class AnimalFactory {
  private final int maxAmountOfMutations;
  private final int minAmountOfMutations;
  private final int energyNeededToReproduce;
  private final int energyUsedForReproduction;
  private final int numberOfGenes;
  private final int energyPerPlant;
  private final int initialAmountOfEnergy;
  private final BehaviorVariant behaviorVariant;

  public AnimalFactory(
      int numberOfGenes,
      int maxAmountOfMutations,
      int minAmountOfMutations,
      int energyNeededToReproduce,
      int energyUsedForReproduction,
      int energyPerPlant,
      int initialAmountOfEnergy,
      BehaviorVariant behaviorVariant
  ) {
    this.maxAmountOfMutations = maxAmountOfMutations;
    this.minAmountOfMutations = minAmountOfMutations;
    this.energyNeededToReproduce = energyNeededToReproduce;
    this.energyUsedForReproduction = energyUsedForReproduction;
    this.numberOfGenes = numberOfGenes;
    this.energyPerPlant = energyPerPlant;
    this.initialAmountOfEnergy = initialAmountOfEnergy;
    this.behaviorVariant = behaviorVariant;
  }

  private void configureEnergy(Animal animal){
    animal.setEnergyGainedByEating(energyPerPlant);
    animal.setEnergyNeededForReproduction(energyNeededToReproduce);
  }

  private Animal createAnimalBaseVariant(Vector2d position){
    Animal newAnimal = switch (behaviorVariant){
      case FULL_PREDESTINATION -> new BaseAnimal(position);
      case CRAZINESS -> new CrazyAnimal(position);
    };
    configureEnergy(newAnimal);
    return newAnimal;
  }

  public Animal createAnimal(Vector2d position){
    Animal newAnimal = createAnimalBaseVariant(position);
    newAnimal.gainEnergy(initialAmountOfEnergy);
    newAnimal.initialiseGenotype(numberOfGenes);
    return newAnimal;
  }

  public Animal createAnimal(Animal firstAnimal, Animal secondAnimal){
    Animal newAnimal = createAnimalBaseVariant(firstAnimal.getPosition());
    newAnimal.initialiseGenotype(
        numberOfGenes,
        firstAnimal,
        secondAnimal,
        maxAmountOfMutations,
        minAmountOfMutations,
        energyNeededToReproduce
    );
    newAnimal.gainEnergy(energyUsedForReproduction * 2);
    newAnimal.setOrientation(Direction.getRandomDirection());
    firstAnimal.addChild(newAnimal);
    secondAnimal.addChild(newAnimal);
    return newAnimal;
  }


}
