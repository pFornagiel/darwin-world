package agh.ics.oop.model.simulation;

import java.util.concurrent.CountDownLatch;

/**
 * Interface for listeners that are notified of changes in the map state during a simulation.
 * Implementations of this interface should define the behavior that should occur when
 * the map is changed or paused.
 */
public interface MapChangeListener {

  /**
   * Notifies that the map has been changed.
   *
   * @param latch a {@link CountDownLatch} that can be used to synchronize the listener's
   *              response to the map change event.
   */
  void mapChanged(CountDownLatch latch);

  /**
   * Notifies that the map has been paused.
   *
   * @param latch a {@link CountDownLatch} that can be used to synchronize the listener's
   *              response to the map pause event.
   */
  void mapPaused(CountDownLatch latch);
}
