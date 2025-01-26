package agh.ics.oop.presenter.util;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.presenter.SimulationConfig;
import agh.ics.oop.presenter.SimulationConfigManager;

public class ConfigurationManager {
    private static final String CONFIGURATION_ERROR = "Configuration Error";

    public SimulationConfig loadConfig() {
        SimulationConfig config = SimulationConfigManager.loadConfig();
        if (config == null) {
            throw new IllegalArgumentException(CONFIGURATION_ERROR);
        }
        return config;
    }

    public void saveConfig(SimulationConfig config) {
        SimulationConfigManager.saveConfig(config);
    }
}