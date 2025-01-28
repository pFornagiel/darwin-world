package agh.ics.oop.model.worldmap.abstracts;

import java.util.concurrent.CountDownLatch;

public interface MapChangeListener {
  void mapChanged(CountDownLatch latch);
}
