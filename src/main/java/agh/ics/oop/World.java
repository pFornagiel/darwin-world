package agh.ics.oop;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;

public class World {
  public static void main(String[] args) {
    ConfigMap mapConfig = new ConfigMap(
        10,
        10,
        1000,
        MapVariant.EQUATORS)
        ;
    ConfigAnimal animalConfig = new ConfigAnimal(
        5,
        100,
        6,
        5,
        2,
        2,
        3,
        BehaviorVariant.FULL_PREDESTINATION
    );
    ConfigPlant plantConfig = new ConfigPlant(
        5,
        3,
        0
    );

    SimulationEngine simulationEngine = new SimulationEngine(new Simulation(mapConfig,animalConfig,plantConfig));
    simulationEngine.runAsync();
  }
}