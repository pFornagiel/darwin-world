package agh.ics.oop;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.OptionsParser;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.ConsoleMapDisplay;
import agh.ics.oop.model.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.Collections;

public class World {
  public static void main(String[] args) {

    BaseWorldMap map = new BaseWorldMap(20,20, 20);
    System.out.println(map.toString());

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