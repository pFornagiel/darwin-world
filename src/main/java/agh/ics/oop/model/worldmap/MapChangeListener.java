package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.worldmap.abstracts.WorldMap;

import java.util.concurrent.CountDownLatch;

public interface MapChangeListener {
  void mapChanged(CountDownLatch latch);
}
