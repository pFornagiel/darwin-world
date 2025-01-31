package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.abstracts.SimulatableMap;
import java.util.List;


/**
 * A class that collects data from the simulation, such as information about animals, world map,
 * and simulation statistics.
 */
public class SimulationDataCollector {
  private SimulatableMap<Animal> worldMap;
  private final Simulation simulation;
  private final static DataVisitor worldMapVisitor = new WorldMapVisitor();

  public SimulationDataCollector(Simulation simulation){
    this.simulation = simulation;
    simulation.acceptDataCollector(this);
  }
  public void setWorldMap(SimulatableMap<Animal> worldMap){
    this.worldMap = worldMap;
  }

  public Vector2d getWorldMapSize(){
    return worldMap.getBoundaries().upperBoundary();
  }
  
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
          animal.isAlive() ? -1 : animal.getDayOfDeath()
      );
  }

  public AnimalData getAnimalData(Animal animal) {
      return new AnimalData(
          animal.getEnergy(),
          animal.getPosition(),
          animal.getOrientation(),
          animal.getCurrentGene(),
          animal.getGenotype(),
          animal.isAlive()
      );
  }

//  Consider making it a sorted list if needed
  public List<Animal> getAnimalsAtPosition(Vector2d position){
      return List.copyOf(worldMap.objectsAt(position));
  }

  public SimulationStatistics getSimulationStatistics() {
      return new SimulationStatistics(
          worldMap.getAmountOfElements(),
          worldMap.getAmountOfPlants(),
          worldMap.getAmountOfFreeFields(),
          worldMap.getMostPopularGenotypes(),
          worldMap.getAverageEnergy(),
          worldMap.getAverageLifespan(),
          worldMap.getAverageChildren(),
          simulation.getDayCount()
      );
  }

  public SimulationData getSimulationData(){
      return worldMap.acceptData(worldMapVisitor);
  }

}
