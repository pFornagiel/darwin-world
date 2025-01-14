package agh.ics.oop.model.simulation;

import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;

public interface SimulationVisitor {
  public void visit(BaseWorldMap worldMap);
  public void visit(FireWorldMap worldMap);
}
