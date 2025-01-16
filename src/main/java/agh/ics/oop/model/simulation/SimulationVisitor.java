package agh.ics.oop.model.simulation;

import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;

public interface SimulationVisitor {
  void visit(BaseWorldMap worldMap);
  void visit(FireWorldMap worldMap);
}
