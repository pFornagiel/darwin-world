package agh.ics.oop.model.simulation;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.datacollectors.*;
import agh.ics.oop.model.util.OrderMap;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.random.RandomRepeatingPositionGenerator;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;
import agh.ics.oop.model.worldmap.abstracts.AbstractWorldMap;
import agh.ics.oop.model.worldmap.abstracts.SimulatableMap;
import agh.ics.oop.presenter.SimulationPresenter;

import java.util.*;

import static agh.ics.oop.model.simulation.SimulationApp.getPresenter;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation implements Runnable, SimulationVisitor {
  private final SimulatableMap<Animal> worldMap;
  private final AnimalFactory animalFactory;

  private final OrderMap<Genotype> orderedAmountOfGenotypes = new OrderMap<>();
  private int dayCount = 0;

  private final ConfigMap configMap;
  private final ConfigPlant configPlant;
  private final ConfigAnimal configAnimal;

  private boolean isRunning = true;
  private boolean paused = false;

  private final ReentrantLock pauseLock = new ReentrantLock();
  private final Condition unpausedCondition = pauseLock.newCondition();

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
    SimulationPresenter presenter = getPresenter();
    this.worldMap = initialiseWorldMap();
    presenter.setWorldMap((AbstractWorldMap) this.worldMap);
  }
  public int getAnimalEnergy(){
    return configAnimal.initialEnergy();
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
      worldMap.incrementGenotypeCount(animal.getGenotype());
    }

    worldMap.randomPlantGrowth(configPlant.initialPlantCount());
    return worldMap;
  }


  public synchronized void togglePause() {
    paused = !paused;
    if (!paused) {
      pauseLock.lock();
      try {
        unpausedCondition.signalAll();
      } finally {
        pauseLock.unlock();
      }
    }
  }

  private void checkPaused() {
    pauseLock.lock();
    try {
      while (paused) {
        try {
          unpausedCondition.await(); // Wait until notified by `resume()`
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt(); // Restore interrupted status
          System.out.printf(INTERRUPT_ERROR_MESSAGE, e.getMessage());
        }
      }
    } finally {
      pauseLock.unlock();
    }
  }

  public void pause() {
    pauseLock.lock();
    try {
      paused = true;
    } finally {
      pauseLock.unlock();
    }
  }

  public void resume() {
    pauseLock.lock();
    try {
      paused = false;
      unpausedCondition.signalAll(); // Notify all waiting threads
    } finally {
      pauseLock.unlock();
    }
  }

  private void sleep() {
    try {
      Thread.sleep(this.configMap.mapRefreshInterval());
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }
  }

  private void baseSimulationSteps(SimulatableMap<Animal> worldMap) {
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
    while (isRunning && !worldMap.getElements().isEmpty()) {
      checkPaused(); // Check and wait if paused
      baseSimulationSteps(worldMap);
      sleep();
    }
  }

  @Override
  public void visit(FireWorldMap worldMap) {
    while (isRunning && !worldMap.getElements().isEmpty()) {
      checkPaused(); // Check and wait if paused
      baseSimulationSteps(worldMap);
      if (dayCount % configMap.fireOutburstInterval() == 0) {
        worldMap.randomFireOutburst();
      }
      worldMap.spreadFire();
      worldMap.updateFireDuration();
      sleep();
    }
  }

  public int getDayCount() {
    return dayCount;
  }

  public void acceptDataCollector(SimulationDataCollector dataCollector) {
    dataCollector.setWorldMap(worldMap);
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      checkPaused();
      worldMap.accept(this);
    }
  }
}
