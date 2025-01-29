package agh.ics.oop.model.util;

import java.util.*;


/**
 * A custom map implementation that maintains entries sorted by their values in descending order.
 * If two values are equal, the order of insertion determines their relative ordering.
 *
 * <p>This class uses an internal {@code TreeMap} with a custom comparator to ensure the correct ordering.
 * Additionally, an insertion order map is maintained to resolve tie-breaking cases where values are equal.
 *
 * <p>Operations such as insertion, retrieval, and removal are provided, along with a method
 * to retrieve entries as a sorted list.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values, which must be {@link Comparable}
 */
public class ValueSortedMap<K, V extends Comparable<V>> {
  private final Map<K, Integer> insertionOrder = new HashMap<>();
  private int nextOrder = 0;

  private final Map<K, V> map = new HashMap<>();
  private final Comparator<K> valueComparator = (k1, k2) -> {
    if(map.get(k1) == null) return -1;
    if(map.get(k2) == null) return 1;
    int compare = -map.get(k1).compareTo(map.get(k2));
    if (compare == 0) {
      return insertionOrder.get(k1) - insertionOrder.get(k2);
    }
    return compare;

  };

  private final TreeMap<K, V> sortedMap = new TreeMap<>(valueComparator);

  public void put(K key, V value) {
    if (!insertionOrder.containsKey(key)) {
      insertionOrder.put(key, nextOrder++);
    }
    sortedMap.remove(key);
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

