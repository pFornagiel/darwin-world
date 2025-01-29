package agh.ics.oop.model.util;

import agh.ics.oop.model.exception.util.ElementAtKeyDoesNotExistException;


/**
 * A specialized extension of {@link ValueSortedMap} that maintains integer values
 * and provides methods for incrementing and decrementing values associated with keys.
 *
 * <p>The map ensures that decrementing a value to zero results in its removal.
 * If a key does not exist or has a value of zero, decrementing will throw an exception.
 *
 * <p>This class is useful for tracking counts or priority-based ordering where
 * values dynamically change over time.
 *
 * @param <K> the type of keys maintained by this map
 */
public class OrderMap<K> extends ValueSortedMap<K, Integer> {

  public void decrement(K key) {
    Integer currentValue = get(key);
    if (currentValue == null || currentValue == 0) {
      throw new ElementAtKeyDoesNotExistException(key.toString());
    }
    if(currentValue-1 == 0) {
      super.remove(key);
    } else {
      super.put(key, currentValue - 1);
    }
  }

  public void increment(K key) {
    Integer currentValue = get(key);
    if (currentValue == null) {
      super.put(key, 1); // Add new key with value 1
    } else {
      super.put(key, currentValue + 1); // Increment existing value
    }
  }
}
