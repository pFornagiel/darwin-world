package agh.ics.oop.model.util;

import java.util.*;

public class ValueSortedMap<K, V extends Comparable<V>> {
  private final Map<K, V> map = new HashMap<>();
  private final Comparator<K> valueComparator = (k1, k2) -> {
    int compare = map.get(k1).compareTo(map.get(k2));
    if (compare == 0) {
      return k1.hashCode() - k2.hashCode(); // Ensure uniqueness of keys
    }
    return compare;
  };
  private final TreeMap<K, V> sortedMap = new TreeMap<>(valueComparator);

  public void put(K key, V value) {
    map.put(key, value);
    sortedMap.put(key, value);
  }

  public V get(K key) {
    return map.get(key);
  }

  public V lastValue(){
    return sortedMap.lastEntry().getValue();
  }

  public V remove(K key) {
    sortedMap.remove(key);
    return map.remove(key);
  }

  public Set<Map.Entry<K, V>> entrySet() {
    return sortedMap.entrySet();
  }

  public List<Map.Entry<K, V>> toSortedList() {
    return new ArrayList<>(sortedMap.entrySet());
  }
}

