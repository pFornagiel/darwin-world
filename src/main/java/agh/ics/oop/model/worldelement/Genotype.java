package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.exception.worldelement.NotEnoughEnergyToReproduceException;
import agh.ics.oop.model.util.random.RandomNonRepeatingNumbersGenerator;
import agh.ics.oop.model.worldelement.abstracts.Animal;

import java.util.Random;

public class Genotype {
  private static final int DISTINCT_GENES_NUMBER = 8;
  private static final Random rand = new Random();
  private final int[] geneArray;

  private int currentGeneIndex = 0;

  public Genotype(int numberOfGenes) {
    this.geneArray = new int[numberOfGenes];
    for(int i = 0; i < numberOfGenes; i++){
      this.geneArray[i] = rand.nextInt(DISTINCT_GENES_NUMBER);
    }
  }

//  Constructor for creating a genotype via reproduction
  public Genotype(
      int numberOfGenes,
      Animal firstAnimal,
      Animal secondAnimal,
      int maxAmountOfMutations,
      int minAmountOfMutations,
      int energyNeededToReproduce
  ) {
//    Validation
    if(!firstAnimal.doesHaveEnoughEnergyToReproduce() || !secondAnimal.doesHaveEnoughEnergyToReproduce()){
      throw new NotEnoughEnergyToReproduceException(firstAnimal,secondAnimal,energyNeededToReproduce);
    }

    this.geneArray = new int[numberOfGenes];
    Animal dominantAnimal = firstAnimal.getEnergy() >= secondAnimal.getEnergy() ? firstAnimal : secondAnimal;
    Animal otherAnimal = dominantAnimal.equals(secondAnimal) ? firstAnimal : secondAnimal;

    int pivot = (int) Math.ceil(((double) dominantAnimal.getEnergy() / (otherAnimal.getEnergy() + dominantAnimal.getEnergy())) * numberOfGenes);
    int dominantStartIndex = 0;
    int dominantEndIndexExclusive = pivot;
    int otherStartIndex = pivot;
    int otherEndIndexExclusive = geneArray.length;

    boolean rightSideOfDominantGenotype = rand.nextBoolean();
    if(rightSideOfDominantGenotype){
        dominantStartIndex = geneArray.length - pivot;
        dominantEndIndexExclusive = geneArray.length;
        otherStartIndex = 0;
        otherEndIndexExclusive = geneArray.length - pivot;
    }

    for(int i = dominantStartIndex; i < dominantEndIndexExclusive; i++){
      geneArray[i] = dominantAnimal.getGene(i);
    }
    for(int i = otherStartIndex; i < otherEndIndexExclusive; i++){
      geneArray[i] = otherAnimal.getGene(i);
    }

//    Mutations
    int numberOfMutations = rand.nextInt(maxAmountOfMutations) + minAmountOfMutations + 1;
    RandomNonRepeatingNumbersGenerator indexGenerator = new RandomNonRepeatingNumbersGenerator(numberOfGenes, numberOfMutations);
    for(int i: indexGenerator){
      geneArray[i] = rand.nextInt(DISTINCT_GENES_NUMBER);
    }
  }

  public int activateRandomGene(){
    int randomIndex = rand.nextInt(geneArray.length);
    currentGeneIndex = (randomIndex + 1) % geneArray.length;
    return geneArray[randomIndex];
  }

  public int activateNextGene(){
    int currentGene =  geneArray[currentGeneIndex];
    currentGeneIndex = (currentGeneIndex+1)%geneArray.length;
    return currentGene;
  }

  public int getGene(int geneIndex){
    return this.geneArray[geneIndex];
  }

  public int getCurrentGene(){
    return this.geneArray[currentGeneIndex];
  }

  public int toInt(){
    int number = 0;
    int multiplier = 1;
    for(int i = geneArray.length-1; i >= 0; i--){
      number += geneArray[i] * multiplier;
    }
    return number;
  }

  @Override
  public int hashCode() {
    return toInt();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null)
      return false;
    if (getClass() != other.getClass())
      return false;
    return toInt() == ((Genotype) other).toInt();
  }

  @Override
  public String toString() {
    return Integer.toString(toInt());
  }
}
