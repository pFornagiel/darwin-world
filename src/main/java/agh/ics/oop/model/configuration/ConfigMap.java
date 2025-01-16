package agh.ics.oop.model.configuration;

public record ConfigMap(
    int width,
    int height,
    int mapRefreshInterval,
    MapVariant mapVariant,// Enum for map variant
    int fireOutburstInterval, // -1 if not applicable
    int fireDuration // -1 if not applicable
) {
  public ConfigMap(
      int width,
      int height,
      int mapRefreshInterval,
      MapVariant mapVariant
  ) {
    this(width, height, mapRefreshInterval, mapVariant, -1, -1);
  }
}

