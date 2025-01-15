package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;
import agh.ics.oop.model.worldmap.abstracts.SimulatableMap;

public class SimulationDataCollector implements DataVisitor {
  @Override
  public AnimalStatistics getAnimalStatistics(Animal animal) {
    return new AnimalStatistics(
      animal.getPosition(),
      animal.getEnergy(),
      animal.getOrientation(),
      animal.getGenotype(),
      animal.getCurrentGene(),
      animal.getEatenPlants(),
      animal.getEatenPlants(),
      animal.getAmountOfDescendants(),
      animal.getLifespan(),
      animal.getDayOfDeath()
    );
  }

  @Override
  public SimulationStatistics getSimulationStatistics(SimulatableMap<Animal> worldMap, int amountOfDays) {
    return new SimulationStatistics(
        worldMap.getAmountOfElements(),
        worldMap.getAmountOfPlants(),
        worldMap.getAmountOfFreeFields(),
        worldMap.getMostPopularGenotypes(),
        worldMap.getAverageEnergy(),
        worldMap.getAverageLifespan(),
        worldMap.getAverageChildren(),
        amountOfDays
    );
  }

  @Override
  public SimulationData visitSimulationData(BaseWorldMap worldMap) {
    return new SimulationData(
        worldMap.getElementPositionSet(),
        worldMap.getPlantPositionSet(),
        worldMap.getVerdantFieldPositionSet()
    );
  }

  @Override
  public SimulationData visitSimulationData(FireWorldMap worldMap) {
    return new SimulationData(
        worldMap.getElementPositionSet(),
        worldMap.getPlantPositionSet(),
        worldMap.getVerdantFieldPositionSet(),
        worldMap.getFirePositionSet()
    );
  }
}
