package agh.ics.oop.model.worldmap.abstracts;

import agh.ics.oop.model.util.OrderMap;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Genotype;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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

  protected void incrementGenotype(Genotype genotype){
    orderedAmountOfGenotypes.increment(genotype);
  }
  protected void decrementGenotype(Genotype genotype){
    orderedAmountOfGenotypes.decrement(genotype);
  }

  protected void killAnimal(Animal animal){
    removeElement(animal);
    animal.kill();
    deadAnimalCount++;
    deadAnimalLifespanSum += animal.getLifespan();
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
    TreeSet<Animal> animalSet = new TreeSet<>(objectsAt(position));
    if(isPlantGrown(position)){
      animalSet.getFirst().eat();
      deletePlantAtPosition(position);
    }
  }

  @Override
  public void breedCreatures(Vector2d position){
    TreeSet<Animal> animalSet = new TreeSet<>(objectsAt(position));
    if(animalSet.size() > 1){
      Animal firstAnimal = animalSet.getFirst();
      animalSet.remove(firstAnimal);
      Animal secondAnimal = animalSet.getFirst();
      animalSet.remove(secondAnimal);

      while(firstAnimal.doesHaveEnoughEnergyToReproduce() && secondAnimal.doesHaveEnoughEnergyToReproduce()) {
        Animal newAnimal = animalFactory.createAnimal(firstAnimal, secondAnimal);
        placeElement(newAnimal);

        firstAnimal.drainEnergyDuringReproduction();
        secondAnimal.drainEnergyDuringReproduction();

        animalSet.add(firstAnimal);
        animalSet.add(secondAnimal);

        firstAnimal = animalSet.getFirst();
        animalSet.remove(firstAnimal);
        secondAnimal = animalSet.getFirst();
        animalSet.remove(secondAnimal);
      }
    }
  }

  @Override
  public void growPlants(int amountOfPlants){
    randomPlantGrowth(amountOfPlants);
  }

//  Getting information
  private int getAmountOfAnimals(){
    return getElements().size();
  }

  private double getAverageEnergy(){
    return getElements().stream()
        .mapToInt(Animal::getEnergy)
        .average()
        .orElse(0.0);
  }
  private double getAverageChildren(){
    return getElements().stream()
        .mapToInt(animal -> animal.getChildren().size())
        .average()
        .orElse(0.0);
  }
  private double getAverageLifespan(){
    return (double) deadAnimalLifespanSum / deadAnimalCount;
  }

  private List<Genotype> getMostPopularGenotypes(){
    return orderedAmountOfGenotypes.toSortedList().stream()
        .map(Map.Entry::getKey)
        .toList();
  }

  private Set<Vector2d> getOccupiedMapTiles(){
    return Stream.concat(getElementPositionSet().stream(), getPlantPositionSet().stream()).collect(Collectors.toSet());
  }
}
