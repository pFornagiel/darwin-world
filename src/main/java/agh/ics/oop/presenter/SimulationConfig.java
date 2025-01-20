package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.ConfigMap;
import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigPlant;

public record SimulationConfig(
        ConfigMap mapConfig,
        ConfigPlant plantConfig,
        ConfigAnimal animalConfig
) {}