package agh.ics.oop.model.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
  private final List<Simulation> simulationList;
  private final ArrayList<Thread> simulationThreadList = new ArrayList<>();
  private final ExecutorService simulationExecutor = Executors.newFixedThreadPool(4);


  public SimulationEngine(List<Simulation> simulationList) {
    this.simulationList = simulationList;
  }
  public SimulationEngine(Simulation simulation) {
    this.simulationList = new ArrayList<>();
    this.simulationList.add(simulation);
  }

  public void runSync(){
    for(Simulation simulation : simulationList){
      simulation.run();
    }
  }

  public void runAsync(){
    for(Simulation simulation : simulationList){
      Thread simulationThread = new Thread(simulation);
      simulationThreadList.add(simulationThread);
      simulationThread.start();
    }

  }

  public void awaitSimulationEnd() throws InterruptedException {
    for(Thread simulationThread : simulationThreadList){
      simulationThread.join();
    }
    simulationExecutor.shutdown();
    boolean hasExecutorTerminated = simulationExecutor.awaitTermination(100, TimeUnit.SECONDS);
    if(!hasExecutorTerminated){
      simulationExecutor.shutdownNow();
      throw new InterruptedException("Executor shutdown before termination");
    }
  }

  public void runAsyncInThreadPool() {
    for(Simulation simulation : simulationList){
      simulationExecutor.submit(simulation);
    }
  }
}
