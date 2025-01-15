package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;
import agh.ics.oop.model.worldmap.abstracts.SimulatableMap;

public interface DataVisitor {
  AnimalStatistics getAnimalStatistics(Animal animal);
  SimulationStatistics getSimulationStatistics(SimulatableMap<Animal> worldMap, int amountOfDays);
  SimulationData visitSimulationData(BaseWorldMap worldMap);
  SimulationData visitSimulationData(FireWorldMap worldMap);
}
