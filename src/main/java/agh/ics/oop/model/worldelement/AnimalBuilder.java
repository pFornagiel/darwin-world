package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.util.Vector2d;

public class AnimalBuilder {
  private final int maxAmountOfMutations;
  private final int minAmountOfMutations;
  private final int energyNeededToReproduce;
  private final int energyUsedForReproduction;
  private final int numberOfGenes;
  private final int energyPerPlant;


  public AnimalBuilder(
      int numberOfGenes,
      int maxAmountOfMutations,
      int minAmountOfMutations,
      int energyNeededToReproduce,
      int energyUsedForReproduction,
      int energyPerPlant
  ) {
    this.maxAmountOfMutations = maxAmountOfMutations;
    this.minAmountOfMutations = minAmountOfMutations;
    this.energyNeededToReproduce = energyNeededToReproduce;
    this.energyUsedForReproduction = energyUsedForReproduction;
    this.numberOfGenes = numberOfGenes;
    this.energyPerPlant = energyPerPlant;
  }

  private void configureGainedEnergy(Animal animal){
    animal.setEnergyGainedByEating(energyPerPlant);
    animal.setEnergyUsedForReproduction(energyUsedForReproduction);
  }

  public Animal createAnimal(){
    Animal newAnimal = new Animal();
    newAnimal.initialiseGenotype(numberOfGenes);
    configureGainedEnergy(newAnimal);
    return newAnimal;
  }

  public Animal createAnimal(Vector2d position){
    Animal newAnimal = new Animal(position);
    newAnimal.initialiseGenotype(numberOfGenes);
    configureGainedEnergy(newAnimal);
    return newAnimal;
  }

  public Animal createAnimal(Animal firstAnimal, Animal secondAnimal){
    Animal newAnimal = new Animal(firstAnimal.getPosition());
    newAnimal.initialiseGenotype(
        numberOfGenes,
        firstAnimal,
        secondAnimal,
        maxAmountOfMutations,
        minAmountOfMutations,
        energyNeededToReproduce
    );
    configureGainedEnergy(newAnimal);
    newAnimal.gainEnergy(energyUsedForReproduction);
    firstAnimal.addChild(newAnimal);
    secondAnimal.addChild(newAnimal);
    return newAnimal;
  }


}
