package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;

public class WorldMapVisitor implements DataVisitor {
  @Override
  public SimulationData visitWorldMapData(FireWorldMap worldMap) {
      return new SimulationData(
          worldMap.getElementPositionSet(),
          worldMap.getPlantPositionSet(),
          worldMap.getVerdantFieldPositionSet(),
          worldMap.getFirePositionSet()
    );
  }

  @Override
  public SimulationData visitWorldMapData(BaseWorldMap worldMap) {
    return new SimulationData(
        worldMap.getElementPositionSet(),
        worldMap.getPlantPositionSet(),
        worldMap.getVerdantFieldPositionSet()
    );
  }
}
