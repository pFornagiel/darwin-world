package agh.ics.oop.model.simulation;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.datacollectors.*;
import agh.ics.oop.model.util.OrderMap;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.random.RandomRepeatingPositionGenerator;
import agh.ics.oop.model.worldelement.*;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;
import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;
import agh.ics.oop.model.worldmap.abstracts.SimulatableMap;

import java.util.*;

public class Simulation implements Runnable, SimulationVisitor {
  private static final SimulationDataCollector dataCollector = new SimulationDataCollector();
  private final SimulatableMap<Animal> worldMap;
  private final AnimalFactory animalFactory;

  private final OrderMap<Genotype> orderedAmountOfGenotypes = new OrderMap<>();

  private int dayCount = 0;

  private final ConfigMap configMap;
  private final ConfigPlant configPlant;
  private final ConfigAnimal configAnimal;

  private boolean isRunning = true;

  private static final String INTERRUPT_ERROR_MESSAGE = "Engine: Interrupted. Restoring interrupted status. Reason: %s%n";

  public Simulation(ConfigMap configMap, ConfigAnimal configAnimal, ConfigPlant configPlant) {
    this.configMap = configMap;
    this.configAnimal = configAnimal;
    this.configPlant = configPlant;
    this.animalFactory = new AnimalFactory(
        configAnimal.genomeLength(),
        configAnimal.maxMutations(),
        configAnimal.minMutations(),
        configAnimal.energyToReproduce(),
        configAnimal.energyConsumedByParents(),
        configPlant.energyPerPlant(),
        configAnimal.initialEnergy(),
        configAnimal.behaviorVariant()
    );
    this.worldMap = initialiseWorldMap();
  }

  private SimulatableMap<Animal> initialiseWorldMap(){
    SimulatableMap<Animal> worldMap = switch (configMap.mapVariant()){
      case MapVariant.FIRES ->
          new FireWorldMap(configMap.width(), configMap.height(), animalFactory, configMap.fireDuration());
      case MapVariant.EQUATORS ->
          new BaseWorldMap(configMap.width(), configMap.height(), animalFactory);
    };

    RandomRepeatingPositionGenerator animalPositionGenerator = new RandomRepeatingPositionGenerator(
        configMap.width(),
        configMap.height(),
        configAnimal.initialAnimalCount()
    );

    for(Vector2d animalPosition: animalPositionGenerator) {
      Animal animal = animalFactory.createAnimal(animalPosition);
      worldMap.placeElement(animal);
    }

    worldMap.randomPlantGrowth(configPlant.initialPlantCount());

    return worldMap;
  }

  public void toggle(){
    this.isRunning = !isRunning;
  }

  private void sleep(){
    try {
      Thread.sleep(this.configMap.mapRefreshInterval());
    }catch(InterruptedException e){
      System.out.println(e.getMessage());
    }
  }

  private void baseSimulationSteps(SimulatableMap<Animal> worldMap){
      Set<Animal> animalSet = new HashSet<>(worldMap.getElements());
      dayCount++;
      System.out.println(worldMap);
      for (Animal animal : animalSet) {
        animal.updateLifespan();
        worldMap.killDyingCreature(animal);
        if (animal.isAlive()) {
          worldMap.moveCreature(animal);
          worldMap.rotateCreature(animal);
        }
      }

      Set<Vector2d> occupiedPositionSet = new HashSet<>(worldMap.getElementPositionSet());
      for (Vector2d position : occupiedPositionSet) {
        worldMap.consumePlant(position);
        worldMap.breedCreatures(position);
      }
      worldMap.growPlants(configPlant.dailyPlantGrowth());
  }

  @Override
  public void visit(BaseWorldMap worldMap) {
    while(isRunning && !worldMap.getElements().isEmpty()) {
      baseSimulationSteps(worldMap);
      sleep();
    }

  }

  @Override
  public void visit(FireWorldMap worldMap) {
    while(isRunning && !worldMap.getElements().isEmpty()) {
      baseSimulationSteps(worldMap);
      if(dayCount % configMap.fireOutburstInterval() == 0){
        worldMap.randomFireOutburst();
      }
      worldMap.spreadFire();
      worldMap.updateFireDuration();
      sleep();
    }
  }

//  Statistics API
  public AnimalStatistics getAnimalStatistics(Animal animal) {
    return dataCollector.getAnimalStatistics(animal);
  }

  public SimulationStatistics getSimulationStatistics(){
    return dataCollector.getSimulationStatistics(worldMap,dayCount);
  }

  public SimulationData getSimulationData(){
    return worldMap.acceptData(dataCollector);
  }


  @Override
  public void run(){
    worldMap.accept(this);
  }
}
