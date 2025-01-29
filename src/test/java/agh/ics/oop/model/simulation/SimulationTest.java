package agh.ics.oop.model.simulation;

import agh.ics.oop.model.ConfigTestSingleton;
import agh.ics.oop.model.configuration.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
  private static final ConfigAnimal animalConfig = ConfigTestSingleton.getAnimalConfig();
  private static final ConfigMap mapConfig = ConfigTestSingleton.getMapConfig();
  private static final ConfigPlant plantConfig = ConfigTestSingleton.getPlantConfig();

  private Simulation simulation;

  @BeforeEach
  void setUp() {
    simulation = new Simulation(mapConfig, animalConfig, plantConfig);
  }

  @Test
  void testSimulationStartsAtDayZero() {
    // Given (setup is already done in @BeforeEach)

    // When
    int dayCount = simulation.getDayCount();

    // Then
    assertEquals(0, dayCount, "Simulation should start at day zero.");
  }

  @Test
  void testTogglePause() {
    // Given (setup is already done in @BeforeEach)

    // Then
    assertFalse(simulation.isPuased(), "Simulation should not be paused initially.");
    simulation.togglePause();
    assertTrue(simulation.isPuased(), "Simulation should be paused after toggling.");
    simulation.togglePause();
    assertFalse(simulation.isPuased(), "Simulation should resume after toggling again.");
  }

  @Test
  void testBaseSimulationStepsTime() {
    // Given
    int initialDayCount = simulation.getDayCount();

    // When
    SimulationEngine simulationEngine = new SimulationEngine(simulation);
    simulationEngine.runAsync();
    // Wait for simulation to proceed
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println("Thread interrupted: " + e.getMessage());
    }

    // Then
    assertTrue(initialDayCount < simulation.getDayCount(), "Day count should increment by 1.");
    assertFalse(simulation.isPuased(), "Simulation should not be paused when running");
  }

  @Test
  void testFireSimulationSteps() {
    // Given
    ConfigMap fireMapConfig = new ConfigMap(20, 20, 100, MapVariant.FIRES, 5,4,false);
    Simulation fireSimulation = new Simulation(fireMapConfig, animalConfig, plantConfig);

    int initialDayCount = fireSimulation.getDayCount();

    // When
    SimulationEngine simulationEngine = new SimulationEngine(simulation);
    simulationEngine.runAsync();
    // Wait for simulation to proceed
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println("Thread interrupted: " + e.getMessage());
    }

    // Then
    assertTrue(initialDayCount < simulation.getDayCount(), "Day count should increment by 1.");
    assertFalse(simulation.isPuased(), "Simulation should not be paused when running");
  }
}
