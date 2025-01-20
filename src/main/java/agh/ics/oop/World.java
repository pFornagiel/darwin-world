package agh.ics.oop;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;

public class World {
  public static void main(String[] args) {
    ConfigMap mapConfig = new ConfigMap(
        20,
        20,
        1000,
        MapVariant.FIRES,
        2,
        4
        );
    ConfigAnimal animalConfig = new ConfigAnimal(
        30,
        100,
        6,
        5,
        2,
        2,
        3,
        BehaviorVariant.CRAZINESS
    );
    ConfigPlant plantConfig = new ConfigPlant(
        5,
        3,
        4
    );

    Simulation simulation = new Simulation(mapConfig, animalConfig, plantConfig);
    SimulationDataCollector dataCollector = new SimulationDataCollector(simulation);
    SimulationEngine simulationEngine = new SimulationEngine(simulation);
    simulationEngine.runAsync();
  }
}