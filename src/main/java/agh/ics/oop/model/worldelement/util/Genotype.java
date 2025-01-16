package agh.ics.oop.model.worldelement.util;

import agh.ics.oop.model.exception.worldelement.NotEnoughEnergyToReproduceException;
import agh.ics.oop.model.util.random.RandomNonRepeatingNumbersGenerator;
import agh.ics.oop.model.worldelement.abstracts.Animal;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Genotype {
  private static final int DISTINCT_GENES_NUMBER = 8;
  private static final Random rand = new Random();
  private final int[] geneArray;

  private int currentGeneIndex = 0;

//  Constructor for creating a random genotype
  public Genotype(int numberOfGenes) {
    this.geneArray = rand.ints(numberOfGenes, 0, DISTINCT_GENES_NUMBER)
        .toArray();
  }

//  Constructor for creating a genotype via breeding
  public Genotype(
      int numberOfGenes,
      Animal firstAnimal,
      Animal secondAnimal,
      int maxMutations,
      int minMutations,
      int energyNeededToReproduce
  ) {
    validateReproduction(firstAnimal, secondAnimal, energyNeededToReproduce);

    this.geneArray = new int[numberOfGenes];
    Animal dominantAnimal = firstAnimal.getEnergy() >= secondAnimal.getEnergy() ? firstAnimal : secondAnimal;
    Animal otherAnimal = dominantAnimal.equals(secondAnimal) ? firstAnimal : secondAnimal;

    combineGenes(dominantAnimal, otherAnimal, numberOfGenes);
    applyMutations(maxMutations, minMutations);
  }

  private void validateReproduction(Animal firstAnimal, Animal secondAnimal, int energyNeededToReproduce) {
    if (!firstAnimal.doesHaveEnoughEnergyToReproduce() || !secondAnimal.doesHaveEnoughEnergyToReproduce()) {
      throw new NotEnoughEnergyToReproduceException(firstAnimal, secondAnimal, energyNeededToReproduce);
    }
  }

  private void combineGenes(Animal dominant, Animal other, int numberOfGenes) {
    int pivot = (int) Math.ceil(((double) dominant.getEnergy() / (other.getEnergy() + dominant.getEnergy())) * numberOfGenes);
    int dominantStartIndex = 0;
    int dominantEndIndexExclusive = pivot;
    int otherStartIndex = pivot;
    int otherEndIndexExclusive = geneArray.length;

    boolean rightSideOfDominantGenotype = rand.nextBoolean();
    if (rightSideOfDominantGenotype) {
      dominantStartIndex = geneArray.length - pivot;
      dominantEndIndexExclusive = geneArray.length;
      otherStartIndex = 0;
      otherEndIndexExclusive = geneArray.length - pivot;
    }

    IntStream.range(dominantStartIndex, dominantEndIndexExclusive)
        .forEach(i -> geneArray[i] = dominant.getGene(i));

    IntStream.range(otherStartIndex, otherEndIndexExclusive)
        .forEach(i -> geneArray[i] = other.getGene(i));

  }

  private void applyMutations(int maxMutations, int minMutations) {
    int numberOfMutations = rand.nextInt(maxMutations) + minMutations + 1;
    RandomNonRepeatingNumbersGenerator indexGenerator = new RandomNonRepeatingNumbersGenerator(
        geneArray.length,
        numberOfMutations
    );
    indexGenerator.forEach(i -> geneArray[i] = rand.nextInt(DISTINCT_GENES_NUMBER));
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

  public int convertToInt(){
    return IntStream.range(0, geneArray.length)
        .map(i -> geneArray[geneArray.length - 1 - i] * (int) Math.pow(10, i))
        .sum();
  }

  @Override
  public int hashCode() {
    return convertToInt();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass())
      return false;
    return convertToInt() == ((Genotype) other).convertToInt();
  }

  @Override
  public String toString() {
    return Arrays.toString(geneArray);
  }
}
