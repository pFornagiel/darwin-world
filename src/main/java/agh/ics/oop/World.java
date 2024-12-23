package agh.ics.oop;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.OptionsParser;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldmap.AbstractWorldMap;
import agh.ics.oop.model.worldmap.ConsoleMapDisplay;
import agh.ics.oop.model.worldmap.GrassField;
import agh.ics.oop.model.worldmap.RectangularMap;

import java.util.ArrayList;
import java.util.Collections;

public class World {
  public static void main(String[] args) {

    ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();

    ArrayList<AbstractWorldMap> maps = new ArrayList<>();
    int amountOfMaps = 100;
    for(int i = 0; i< amountOfMaps; i++){
      AbstractWorldMap newMap;
      if(i%2 == 0){
        newMap = new RectangularMap(10 + i /10, 10 + i/10);
      } else {
        newMap = new GrassField(5 + i/10);
      }
      newMap.addToListeners(consoleMapDisplay);
      maps.add(newMap);
    }

    String[] arguments = {"l", "r", "b", "f","f","f","r","r","b","f"};

    ArrayList<Vector2d> positionList = new ArrayList<>();
    Collections.addAll(
        positionList,
        new Vector2d(2,2),
        new Vector2d(1,1),
        new Vector2d(10,10)
    );
    ArrayList<MoveDirection> directionList = OptionsParser.parse(arguments);

    ArrayList<Simulation> simulationList = new ArrayList<>();
    for(AbstractWorldMap map: maps){
      simulationList.add(new Simulation(positionList,directionList,map));
    }

    SimulationEngine simulationEngine = new SimulationEngine(simulationList);
    try{
      simulationEngine.runAsyncInThreadPool();
      simulationEngine.awaitSimulationEnd();
    } catch (InterruptedException exception){
      System.out.printf("%s%n", exception.getMessage());
    }
    System.out.println("System has finished");


}

  private static void run(ArrayList<MoveDirection> args){
    for (MoveDirection arg : args) {
      printMessage(arg);
    }
  }

  private static void printMessage(MoveDirection arg) {
    switch (arg) {
      case MoveDirection.FORWARD -> System.out.println("Tequila Sunset goes forward");
      case MoveDirection.BACKWARD -> System.out.println("Tequila Sunset goes backward");
      case MoveDirection.RIGHT -> System.out.println("Tequila Sunset turns right");
      case MoveDirection.LEFT -> System.out.println("Tequila Sunset turns left");
    }
  }
}