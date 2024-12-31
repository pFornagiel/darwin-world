package agh.ics.oop;

import agh.ics.oop.model.worldmap.BaseWorldMap;

public class World {
  public static void main(String[] args) {
    BaseWorldMap map = new BaseWorldMap(20,20, 10);
    System.out.println(map);
    for(int i = 0; i< 20; i++){
      map.growPlantsAtRandomPositions(10);
    }
  }
}