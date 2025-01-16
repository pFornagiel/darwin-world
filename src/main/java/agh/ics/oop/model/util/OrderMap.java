package agh.ics.oop.model.util;

public class OrderMap<K> extends ValueSortedMap<K, Integer> {
  public void decrement(K key) {
    // Maybe throw here
    Integer currentValue = get(key);
    if (currentValue == null || currentValue == 0) {
      return;
    }
    super.put(key, currentValue - 1);
  }
  public void increment(K key) {
    // Maybe throw here
    Integer currentValue = get(key);
    if (currentValue == null) {
      super.put(key, 1); // Add new key with value 1
    } else {
      super.put(key, currentValue + 1); // Increment existing value
    }
  }
}
