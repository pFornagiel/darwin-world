package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.worldmap.abstracts.WorldMap;

public interface MapChangeListener {
  void mapChanged(WorldMap worldMap, String message);
}
