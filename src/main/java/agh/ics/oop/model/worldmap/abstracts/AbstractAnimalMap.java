package agh.ics.oop.model.worldmap.abstracts;

import agh.ics.oop.model.util.OrderMap;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractAnimalMap<M extends MapTile<Animal>> extends AbstractWorldMap<Animal,M> implements SimulatableMap<Animal> {
  private final AnimalFactory animalFactory;

  private final OrderMap<Genotype> orderedAmountOfGenotypes = new OrderMap<>();
  protected int deadAnimalLifespanSum;
  protected int deadAnimalCount;

  public AbstractAnimalMap(int mapWidth, int mapHeight, AnimalFactory animalFactory) {
    super(mapWidth, mapHeight);
    this.animalFactory = animalFactory;
  }

  public void incrementGenotypeCount(Genotype genotype){
    orderedAmountOfGenotypes.increment(genotype);
  }
  public void decrementGenotypeCount(Genotype genotype){
    orderedAmountOfGenotypes.decrement(genotype);
  }

  @Override
  public Animal createNewAnimalOnMap(Vector2d position){
    Animal animal = animalFactory.createAnimal(position);
    placeElement(animal);
    incrementGenotypeCount(animal.getGenotype());
    return animal;
  }

  @Override
  public Animal createNewAnimalOnMap(Animal firstParent, Animal secondParent){
    Animal animal = animalFactory.createAnimal(firstParent, secondParent);
    placeElement(animal);
    incrementGenotypeCount(animal.getGenotype());
    return animal;
  }

  protected void killAnimal(Animal animal){
    removeElement(animal);
    animal.kill();
    deadAnimalCount++;
    deadAnimalLifespanSum += animal.getLifespan();
    decrementGenotypeCount(animal.getGenotype());
  }

  @Override
  public void killDyingCreature(Animal animal){
    if(animal.getEnergy() <= 0){
      killAnimal(animal);
    }
  }

  @Override
  public void rotateCreature(Animal animal){
    animal.rotateAndActivate();
  }

  @Override
  public void moveCreature(Animal animal){
    Vector2d position = animal.getPosition();
    Vector2d move = animal.getOrientation().toUnitVector();
    Vector2d newPosition = determinePositionAfterMove(position, move);
    if(position == newPosition){
      animal.setOrientation(animal.getOrientation().opposite());
    } else {
      moveElementTo(animal, newPosition);
      animal.setPosition(newPosition);
    }
    animal.drainEnergy();
  }

  @Override
  public void consumePlant(Vector2d position){
    ArrayList<Animal> animalList = new ArrayList<>(objectsAt(position));
    Collections.sort(animalList);
    if(isPlantGrown(position)){
      animalList.getFirst().eat();
      deletePlantAtPosition(position);
    }
  }

  @Override
  public void breedCreatures(Vector2d position){
    if(objectsAt(position).size() > 1){
      ArrayList<Animal> animalList = new ArrayList<>(objectsAt(position));
      Collections.sort(animalList);
      Animal firstParent = animalList.getFirst();
      Animal secondParent = animalList.get(1);

      if(firstParent.doesHaveEnoughEnergyToReproduce() && secondParent.doesHaveEnoughEnergyToReproduce()) {
        createNewAnimalOnMap(firstParent,secondParent);

        firstParent.drainEnergyDuringReproduction();
        secondParent.drainEnergyDuringReproduction();
      }
    }
  }

  @Override
  public void growPlants(int amountOfPlants){
    randomPlantGrowth(amountOfPlants);
  }


  public double getAverageEnergy(){
    return getElements().stream()
        .mapToInt(Animal::getEnergy)
        .average()
        .orElse(0.0);
  }
  public double getAverageChildren(){
    return getElements().stream()
        .mapToInt(animal -> animal.getChildren().size())
        .average()
        .orElse(0.0);
  }
  public double getAverageLifespan(){
    return (double) deadAnimalLifespanSum / deadAnimalCount;
  }

  public List<Genotype> getMostPopularGenotypes(){
    return orderedAmountOfGenotypes.toSortedList().stream()
        .map(Map.Entry::getKey)
        .toList();
  }

  public Set<Vector2d> getOccupiedMapTiles(){
    return Stream.concat(getElementPositionSet().stream(), getPlantPositionSet().stream()).collect(Collectors.toSet());
  }

  public int getAmountOfFreeFields(){
    return tileMap.size() - elementPositionSet.size();
  }
}
