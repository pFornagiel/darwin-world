package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.worldmap.BaseWorldMap;
import agh.ics.oop.model.worldmap.FireWorldMap;

/**
 * An interface for collecting data from BaseWorldMap and FireWorldMap.
 * This interface defines the methods for visiting various world map types and gathering
 * simulation data associated with them.
 */
public interface DataVisitor {

  /**
   * Visits a {@link BaseWorldMap} instance and collects simulation data.
   *
   * @param worldMap The {@link BaseWorldMap} instance to collect data from.
   * @return The collected simulation data for the provided world map.
   */
  SimulationData visitWorldMapData(BaseWorldMap worldMap);

  /**
   * Visits a {@link FireWorldMap} instance and collects simulation data.
   *
   * @param worldMap The {@link FireWorldMap} instance to collect data from.
   * @return The collected simulation data for the provided world map.
   */
  SimulationData visitWorldMapData(FireWorldMap worldMap);
}
