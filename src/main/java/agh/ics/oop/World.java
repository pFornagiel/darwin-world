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
    BaseWorldMap map = new BaseWorldMap(20,20, 10);
    System.out.println(map);
    for(int i = 0; i< 20; i++){
      map.growPlantsAtRandomPositions(10);
    }
  }
}