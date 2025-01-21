package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.ConfigTestSingleton;
import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigPlant;
import agh.ics.oop.model.exception.worldelement.CreatureAlreadyDeadError;
import agh.ics.oop.model.exception.worldelement.CreatureStillAliveError;
import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;
import agh.ics.oop.model.worldelement.util.Genotype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

  private final ConfigAnimal animalConfig = ConfigTestSingleton.getAnimalConfig();
  private final ConfigPlant plantConfig = ConfigTestSingleton.getPlantConfig();
  private final AnimalFactory animalFactory = new AnimalFactory(
      animalConfig.genomeLength(),
      animalConfig.maxMutations(),
      animalConfig.minMutations(),
      animalConfig.energyToReproduce(),
      animalConfig.energyConsumedByParents(),
      plantConfig.energyPerPlant(),
      animalConfig.initialEnergy(),
      animalConfig.behaviorVariant()
  );
  private static final Vector2d INITIAL_VECTOR = new Vector2d(1,1);
  private Animal animal;

  @BeforeEach
  void setUp() {
    animal = animalFactory.createAnimal(INITIAL_VECTOR);
  }

  @Test
  void testEnergyGain() {
    // Given
    int initialEnergy = animal.getEnergy();
    int energyGainedByEating = plantConfig.energyPerPlant();

    // When
    animal.gainEnergy();

    // Then
        assertEquals(initialEnergy + energyGainedByEating, animal.getEnergy(), "Animal should gain the energy specified for eating.");
  }

  @Test
  void testEnergyDrain() {
    // Given
    int initialEnergy = animal.getEnergy();
    int energyUsedForMovement = 1;

    // When
    animal.drainEnergy();

    // Then
    assertEquals(initialEnergy - energyUsedForMovement, animal.getEnergy(), "Animal's energy should decrease by the drained amount.");
  }

  @Test
  void testLifespanUpdate() {
    // Given
    int initialLifespan = animal.getLifespan();

    // When
    animal.updateLifespan();

    // Then
    assertEquals(initialLifespan + 1, animal.getLifespan(), "Animal's lifespan should increase by 1 each update.");
  }

  @Test
  void testDeathHandling() {
    // When
    animal.kill();

    // Then
    assertFalse(animal.isAlive(), "Animal should be marked as dead after being killed.");
    assertThrows(CreatureAlreadyDeadError.class, animal::kill, "Killing an already dead animal should throw an exception.");
  }

  @Test
  void testDayOfDeath() {
    // When
    animal.updateLifespan();
    animal.updateLifespan();
    animal.kill();

    // Then
    assertEquals(2, animal.getDayOfDeath(), "Animal's day of death should match its lifespan at the time of death.");
  }

  @Test
  void testErrorOnAliveDayOfDeath() {
    // Then
    assertThrows(CreatureStillAliveError.class, animal::getDayOfDeath, "Accessing day of death for a live animal should throw an exception.");
  }

  @Test
  void testReproductionCapability() {
    // When
    animal.gainEnergy(animalConfig.energyToReproduce());
    boolean canReproduce = animal.doesHaveEnoughEnergyToReproduce();

    // Then
    assertTrue(canReproduce, "Animal with sufficient energy should be able to reproduce.");
  }

  @Test
  void testReproductionEnergyDrain() {
    // Given
    animal.gainEnergy(animalConfig.energyToReproduce());
    int initialEnergy = animal.getEnergy();

    // When
    animal.drainEnergyDuringReproduction();

    // Then
    assertEquals(initialEnergy - animalConfig.energyToReproduce(), animal.getEnergy(), "Animal should lose energy required for reproduction.");
  }

  @Test
  void testChildrenManagement() {
    // Given
    Animal child = animalFactory.createAnimal(new Vector2d(2,2));

    // When
    animal.addChild(child);

    // Then
    assertEquals(1, animal.getAmountOfChildren(), "Animal should correctly track the number of children.");
    assertEquals(child, animal.getChildren().getFirst(), "Child should be added to the animal's children list.");
  }

  @Test
  void testRotationAndGeneActivation() {
    // Given
    Direction initialOrientation = animal.getOrientation();

    // When
    int activatedGene = animal.activateNextGene();
    animal.rotateAndActivate();

    // Then
    assertNotEquals(initialOrientation, animal.getOrientation(), "Animal's orientation should change after rotation.");
    assertEquals(activatedGene, animal.getCurrentGene(), "The activated gene should match the current gene index.");
  }

  @Test
  void testEatingIncreasesPlantCounter() {
    // Given
    int initialPlantCounter = animal.getEatenPlants();

    // When
    animal.eat();

    // Then
    assertEquals(initialPlantCounter + 1, animal.getEatenPlants(), "Eating a plant should increment the plant counter.");
  }

  @Test
  void testMovement() {
    // Given
    Vector2d initialPosition = animal.getPosition();
    animal.setOrientation(Direction.NORTH);

    // When
    animal.setPosition(new Vector2d(5, 6));

    // Then
    assertNotEquals(initialPosition, animal.getPosition(), "Animal's position should update after movement.");
  }

  @Test
  void testEquality() {
    // Given
    Animal sameAnimal = new BaseAnimal(animal.getPosition());

    // Then
    assertNotEquals(animal, sameAnimal, "Two animals with different IDs should not be equal.");
    assertEquals(animal, animal, "An animal should be equal to itself.");
  }
}
