package agh.ics.oop.model.worldmap.util;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigMap;
import agh.ics.oop.model.configuration.ConfigPlant;

public class ConfigContainer {
    private final ConfigMap mapConfig;
    private final ConfigAnimal animalConfig;
    private final ConfigPlant plantConfig;

    public ConfigContainer(ConfigMap mapConfig, ConfigAnimal animalConfig, ConfigPlant plantConfig) {
        this.mapConfig = mapConfig;
        this.animalConfig = animalConfig;
        this.plantConfig = plantConfig;
    }

    public ConfigMap getMapConfig() {
        return mapConfig;
    }

    public ConfigAnimal getAnimalConfig() {
        return animalConfig;
    }

    public ConfigPlant getPlantConfig() {
        return plantConfig;
    }
}
