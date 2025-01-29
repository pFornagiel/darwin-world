package agh.ics.oop.model.simulation;

import java.util.concurrent.CountDownLatch;

public interface MapChangeListener {
  void mapChanged(CountDownLatch latch);
  void mapPaused(CountDownLatch latch);
}
