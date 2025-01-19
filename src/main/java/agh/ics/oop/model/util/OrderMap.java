package agh.ics.oop.model.util;

import agh.ics.oop.model.exception.util.ElementAtKeyDoesNotExistException;

public class OrderMap<K> extends ValueSortedMap<K, Integer> {
  public void decrement(K key) {

    Integer currentValue = get(key);
    if (currentValue == null || currentValue == 0) {
      throw new ElementAtKeyDoesNotExistException(key.toString());
    }
    super.put(key, currentValue - 1);
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
