package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WorldMapTest {


  private BaseWorldMap worldMap;
  private AnimalFactory animalFactory;

  private static final ConfigAnimal animalConfig = ConfigTestSingleton.getAnimalConfig();
  private static final ConfigMap mapConfig = ConfigTestSingleton.getMapConfig();
  private static final ConfigPlant plantConfig = ConfigTestSingleton.getPlantConfig();

  @BeforeEach
  public void setUp() {
    animalFactory = new AnimalFactory(
        animalConfig.genomeLength(),
        animalConfig.maxMutations(),
        animalConfig.minMutations(),
        animalConfig.energyToReproduce(),
        animalConfig.energyConsumedByParents(),
        plantConfig.energyPerPlant(),
        animalConfig.initialEnergy(),
        animalConfig.behaviorVariant()
    );
    worldMap = new BaseWorldMap(mapConfig.width(), mapConfig.height(), animalFactory);
  }

  @Test
  public void testMapProperlyInitialised() {
    // Given
    int mapWidth = mapConfig.width();
    int mapHeight = mapConfig.height();

    // Then
    assertEquals(
        mapWidth*mapHeight,
        worldMap.getAmountOfFreeFields(),
        "Amount of free fields should equal the amount of all fields on the map"
    );
    assertEquals(
        0,
        worldMap.getElements().size(),
        "Total number of elements after initialisation should be 0"
    );
  }

  @Test
  void testGrowPlants() {
    // Given
    int initialPlantCount = worldMap.getPlantPositionSet().size();

    // When
    worldMap.growPlants(5);

    // Then
    assertEquals(
        initialPlantCount + 5,
        worldMap.getPlantPositionSet().size(),
        "Five additional plants should have grown on the map."
    );
  }


  @Test
  void testAddAnimal() {
    // Given
    Vector2d position = new Vector2d(2, 2);
    Animal animal = worldMap.createNewAnimalOnMap(position);

    // Then & Then
    assertTrue(
        worldMap.isOccupied(position),
        "Position should be occupied after adding an animal."
    );
    assertEquals(
        animal,
        worldMap.objectsAt(position).toArray()[0],
        "The animal at the position should be the same as the one added."
    );
  }

  @Test
  void testRemoveAnimal() {
    // Given
    Vector2d position = new Vector2d(3, 3);
    Animal animal = worldMap.createNewAnimalOnMap(position);

    // When
    worldMap.removeElement(animal);

    // Then
    assertFalse(
        worldMap.isOccupied(position),
        "Position should not be occupied after removing the animal."
    );
    assertEquals(
        0,
        worldMap.objectsAt(position).size(),
        "The position should be empty after the animal is removed."
    );

    assertEquals(
        0,
        worldMap.getAmountOfElements(),
        "Amount of elements should be zero"
    );
  }

  @Test
  void testMoveAnimal() {
    // Given
    Vector2d initialPosition = new Vector2d(1, 1);
    Vector2d newPosition = new Vector2d(2, 2);
    Animal animal = worldMap.createNewAnimalOnMap(initialPosition);
    animal.setOrientation(Direction.SOUTH_EAST);

    // When
    worldMap.moveCreature(animal);

    // Then
    assertFalse(
        worldMap.isOccupied(initialPosition),
        "The initial position should no longer be occupied."
    );
    assertTrue(
        worldMap.isOccupied(newPosition),
        "The new position should now be occupied."
    );
    assertTrue(animal.isAt(newPosition),
        "The animals position should be set to new position."
    );
    assertTrue(worldMap.objectsAt(newPosition).contains(animal),
        "Object set at the new position should only contain animal."
    );
    assertEquals(
        1,
        worldMap.objectsAt(newPosition).size(),
        "New position should only contain 1 element"
    );

  }


  @Test
  void testMultipleAnimalsAtSamePosition() {
    // Given
    Vector2d position = new Vector2d(0, 0);
    Animal animal1 = worldMap.createNewAnimalOnMap(position);
    Animal animal2 = worldMap.createNewAnimalOnMap(position);


    // Then & Then
    Set<Animal> animalsAtPosition = worldMap.objectsAt(position);
    assertEquals(
        2,
        animalsAtPosition.size(),
        "Two animals should occupy the same position."
    );
    assertTrue(
        animalsAtPosition.contains(animal1),
        "The first animal should be at the position."
    );
    assertTrue(
        animalsAtPosition.contains(animal2),
        "The second animal should be at the position."
    );
  }

  @Test
  void testMapBoundaries() {
    // Given
    Vector2d outOfBoundsPosition = new Vector2d(-1, -1);

    // When & Then
    assertThrows(
        IncorrectPositionException.class,
        () -> worldMap.createNewAnimalOnMap(outOfBoundsPosition),
        "Placing an animal outside the map should throw an exception."
    );
  }

  @Test
  void testAnimalEatsPlant() {
    // Given
    Vector2d position = new Vector2d(2, 2);
    worldMap.growPlantAtPosition(position);
    Animal animal = worldMap.createNewAnimalOnMap(position);

    // When
    worldMap.consumePlant(position);

    // Then
    assertEquals(
        plantConfig.energyPerPlant() + animalConfig.initialEnergy(),
        animal.getEnergy(),
        "Animal energy should increase by the plant's energy value."
    );
    assertFalse(
        worldMap.isPlantGrown(position),
        "The plant should no longer exist after being eaten."
    );
  }

  @Test
  void testKillDyingCreature() {
    // Given
    Vector2d position = new Vector2d(1, 1);
    Animal animal = worldMap.createNewAnimalOnMap(position);
    animal.drainEnergy(animal.getEnergy()); // Drain energy to 0

    // When
    worldMap.killDyingCreature(animal);

    // Then
    assertFalse(worldMap.isOccupied(position), "The animal should be removed from the map.");
    assertFalse(animal.isAlive(), "The animal should be dead.");
  }

  @Test
  void testRotateCreature() {
    // Given
    Vector2d position = new Vector2d(1, 1);
    Animal animal = worldMap.createNewAnimalOnMap(position);
    Direction initialOrientation = animal.getOrientation();
    int initialGene = animal.getCurrentGene();

    // When
    worldMap.rotateCreature(animal);

    // Then
    assertNotEquals(
        initialOrientation,
        animal.getOrientation(),
        "The animal's orientation should have changed."
    );
    assertEquals(
        animal.getOrientation(),
        initialOrientation.rotate(initialGene),
        "Initial orientation should rotate according to initial gene."
    );
  }

  @Test
  void testBreedCreatures() {
    // Given
    Vector2d position = new Vector2d(2, 2);

    Animal parent1 = worldMap.createNewAnimalOnMap(position);
    Animal parent2 = worldMap.createNewAnimalOnMap(position);
    parent1.gainEnergy(animalConfig.energyToReproduce());
    parent2.gainEnergy(animalConfig.energyToReproduce());

    // When
    worldMap.breedCreatures(position);
    Set<Animal> animals = worldMap.objectsAt(position);
    Animal child = animals.stream().filter(animal -> !animal.equals(parent1) && !animal.equals(parent2)).findFirst().orElse(null);

    // Then
    assertEquals(
        3,
        animals.size(),
        "There should be three animals at the position after breeding."
    );
    assertNotNull(child, "A child animal should have been created.");
    assertEquals(
        animalConfig.energyConsumedByParents() * 2,
        child.getEnergy(),
        "Child should have energy equal to the energy of both parents used for breeding."
    );
    assertTrue(
        parent1.getChildren().contains(child),
        "The first parent should recognize the child."
    );
    assertTrue(
        parent2.getChildren().contains(child),
        "The second parent should recognize the child."
    );
    assertEquals(
        1,
        parent1.getAmountOfChildren(),
        "First parent should have only one child."
    );
    assertEquals(
        1,
        parent2.getAmountOfChildren(),
        "Second parent should have only one child."
    );
    assertEquals(
        1,
        parent1.getAmountOfDescendants(),
        "First parent should have only one descendant."
    );
    assertEquals(
        1,
        parent2.getAmountOfDescendants(),
        "Second parent should have only one descendant."
    );
  }

  @Test
  void testGetAverageEnergy() {
    // Given
    Animal animal1 = worldMap.createNewAnimalOnMap(new Vector2d(1, 1));
    Animal animal2 = worldMap.createNewAnimalOnMap(new Vector2d(2, 2));

    animal1.gainEnergy(10);
    animal2.gainEnergy(20);

    // When
    double averageEnergy = worldMap.getAverageEnergy();

    // Then
    assertEquals(
        animalConfig.initialEnergy() + 15.0,
        averageEnergy,
        "The average energy should be the mean of the animals' energies."
    );
  }

  @Test
  void testMoveAnimalOutOfBounds() {
    // Given
    Vector2d initialPosition = new Vector2d(0, 0); // Corner of the map
    Animal animal = worldMap.createNewAnimalOnMap(initialPosition);
    Direction initialOrientation = Direction.NORTH_WEST;
    animal.setOrientation(initialOrientation);

    // When
    worldMap.moveCreature(animal);

    // Then
    assertEquals(
        initialPosition,
        animal.getPosition(),
        "The animal should not move out of bounds and should stay at the initial position."
    );
    assertEquals(
        initialOrientation.opposite(),
        animal.getOrientation(),
        "The animal should rotate by 180 degrees."
    );
  }



}
