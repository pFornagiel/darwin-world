package agh.ics.oop.model.simulation;

import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;

/**
 * Interface for visitors that perform simulations on different types of world maps.
 * Implementations of this interface define how a simulation should be performed
 * on BaseWorldMap and FireWorldMap.
 */
public interface SimulationVisitor {

  /**
   * Performs a simulation on the given BaseWorldMap.
   *
   * @param worldMap the {@link BaseWorldMap} on which the simulation will be performed.
   */
  void visit(BaseWorldMap worldMap);

  /**
   * Performs a simulation on the given FireWorldMap.
   *
   * @param worldMap the {@link FireWorldMap} on which the simulation will be performed.
   */
  void visit(FireWorldMap worldMap);
}
