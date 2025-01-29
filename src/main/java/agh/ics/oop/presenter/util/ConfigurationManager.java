package agh.ics.oop.presenter.util;

import agh.ics.oop.presenter.SimulationConfig;
import agh.ics.oop.presenter.SimulationConfigManager;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfigurationManager {

    private static final String CONFIGURATION_ERROR = "Configuration Error";
    private static final String CONFIG_FOLDER = "configs";

    private Path getConfigFolderPath() {
        return Paths.get(System.getProperty("user.dir"), CONFIG_FOLDER);
    }

    private void ensureConfigFolderExists() {
        Path configFolderPath = getConfigFolderPath();
        if (!Files.exists(configFolderPath)) {
            try {
                Files.createDirectories(configFolderPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config folder: " + e.getMessage());
            }
        }
    }

    public SimulationConfig loadConfig(Path path) {
        ensureConfigFolderExists();
        SimulationConfig config = SimulationConfigManager.loadConfig(path);
        if (config == null) {
            throw new IllegalArgumentException(CONFIGURATION_ERROR);
        }
        return config;
    }

    public void saveConfig(SimulationConfig config) {
        ensureConfigFolderExists();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String currentDateTime = LocalDateTime.now().format(dateTimeFormatter);
        String fileName = String.format("simulation_config_%s.json", currentDateTime);
        Path configFilePath = getConfigFolderPath().resolve(fileName);
        SimulationConfigManager.saveConfig(config, configFilePath);
    }
}