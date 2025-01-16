package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;

public interface DataVisitor {
  SimulationData visitWorldMapData(BaseWorldMap worldMap);
  SimulationData visitWorldMapData(FireWorldMap worldMap);
}
