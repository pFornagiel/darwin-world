package agh.ics.oop.model;

import agh.ics.oop.model.configuration.*;

public class ConfigTestSingleton {

  private static final ConfigMap mapConfig = new ConfigMap(
      20,
      20,
      500,
      MapVariant.EQUATORS,
      2,
      4,
      false
  );
  private static final ConfigAnimal animalConfig = new ConfigAnimal(
      30,
      100,
      6,
      5,
      2,
      2,
      3,
      BehaviorVariant.CRAZINESS
  );
  private static final ConfigPlant plantConfig = new ConfigPlant(
      5,
      3,
      4
  );

  public static ConfigMap getMapConfig() {
    return ConfigTestSingleton.mapConfig;
  }
  public static ConfigAnimal getAnimalConfig() {
    return ConfigTestSingleton.animalConfig;
  }
  public static ConfigPlant getPlantConfig() {
    return ConfigTestSingleton.plantConfig;
  }
}
