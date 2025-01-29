package agh.ics.oop.model.simulation;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.datacollectors.*;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.random.RandomRepeatingPositionGenerator;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;
import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;
import agh.ics.oop.model.worldmap.abstracts.SimulatableMap;

import java.util.*;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation implements Runnable, SimulationVisitor {
  private final SimulatableMap<Animal> worldMap;
  private final AnimalFactory animalFactory;

  private int dayCount = 0;

  private final ConfigMap configMap;
  private final ConfigPlant configPlant;
  private final ConfigAnimal configAnimal;

  private boolean paused = false;
  private final ReentrantLock pauseLock = new ReentrantLock();
  private final Condition unpausedCondition = pauseLock.newCondition();
  private CountDownLatch pauseLatch;

  private final List<MapChangeListener> observers = new ArrayList<>();

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

  private SimulatableMap<Animal> initialiseWorldMap() {
    SimulatableMap<Animal> worldMap = switch (configMap.mapVariant()) {
      case MapVariant.FIRES ->
          new FireWorldMap(configMap.width(), configMap.height(), animalFactory, configMap.fireDuration());
      case MapVariant.EQUATORS -> new BaseWorldMap(configMap.width(), configMap.height(), animalFactory);
    };

    RandomRepeatingPositionGenerator animalPositionGenerator = new RandomRepeatingPositionGenerator(
        configMap.width(),
        configMap.height(),
        configAnimal.initialAnimalCount()
    );

    for(Vector2d animalPosition: animalPositionGenerator) {
      worldMap.createNewCreatureOnMap(animalPosition);
    }

    worldMap.randomPlantGrowth(configPlant.initialPlantCount());
    return worldMap;
  }

//  Getters
  public int getDayCount() {
    return dayCount;
  }

  public boolean isPuased() {
    return paused;
  }

//  DataCollector
  public void acceptDataCollector(SimulationDataCollector dataCollector) {
    dataCollector.setWorldMap(worldMap);
  }

  //  Observers
  public void addObserver(MapChangeListener observer) {
    observers.add(observer);
  }

  private void notifyChangeObservers(CountDownLatch latch) {
    for (MapChangeListener observer : observers) {
      observer.mapChanged(latch);
    }
  }

  private void notifyPauseObservers(CountDownLatch latch) {
    for (MapChangeListener observer : observers) {
      observer.mapPaused(latch);
    }
  }

//  Pause logic
  public synchronized void togglePause() {
    paused = !paused;
    if (!paused) {
      pauseLock.lock();
      try {
        unpausedCondition.signalAll();
      } finally {
        pauseLock.unlock();
      }
    } else {
      pauseLatch = new CountDownLatch(observers.size());
      notifyPauseObservers(pauseLatch);
    }
  }

  private void checkPaused() {
    pauseLock.lock();
    try {
      while (paused) {
        try {
          pauseLatch.await();
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

  private void sleep() {
    try {
      Thread.sleep(this.configMap.mapRefreshInterval());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

//  Simulation logic
  private void baseSimulationSteps(SimulatableMap<Animal> worldMap) {
    Set<Animal> animalSet = new HashSet<>(worldMap.getElements());
    dayCount++;
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

  private void fireSimulationSteps(FireWorldMap worldMap) {
    if (dayCount % configMap.fireOutburstInterval() == 0) {
      worldMap.randomFireOutburst();
    }
    worldMap.spreadFire();
    worldMap.updateFireDuration();
  }

//  Visitors
  @Override
  public void visit(BaseWorldMap worldMap) {
    while (!worldMap.getElements().isEmpty()) {
      checkPaused(); // Check and wait if paused
      CountDownLatch latch = new CountDownLatch(observers.size());
      baseSimulationSteps(worldMap);

      notifyChangeObservers(latch);

      try {
        sleep();
        latch.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  @Override
  public void visit(FireWorldMap worldMap) {
    while (!worldMap.getElements().isEmpty()) {
      checkPaused(); // Check and wait if paused
      CountDownLatch latch = new CountDownLatch(observers.size());
      baseSimulationSteps(worldMap);
      fireSimulationSteps(worldMap);

      notifyChangeObservers(latch);

      try {
        sleep();
        latch.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      checkPaused();
      worldMap.accept(this);
    }
  }
}
