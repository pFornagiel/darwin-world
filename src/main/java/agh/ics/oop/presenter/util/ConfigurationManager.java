package agh.ics.oop.presenter.util;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class ConfigurationManager {

    private static final String CONFIGURATION_ERROR = "Configuration Error";
    private static final String CONFIG_FOLDER = "configs";
    private static final String JSON_EXTENSION = ".json";
    private static final String DEFAULT_FILE_NAME = "simulation_config";
    private static final String SAVE_ERROR_TITLE = "Save Error";
    private static final String SAVE_ERROR_HEADER = "Failed to create directory";
    private static final String SAVE_ERROR_MESSAGE = "Could not create directory: ";
    private static final String JSON_FILTER_DESCRIPTION = "JSON Files";

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

    public void saveConfig(SimulationConfig config, Stage ownerStage) {
        ensureConfigFolderExists();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(getConfigFolderPath().toFile());
        fileChooser.setInitialFileName(DEFAULT_FILE_NAME + JSON_EXTENSION);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(JSON_FILTER_DESCRIPTION, "*" + JSON_EXTENSION)
        );

        File selectedFile = fileChooser.showSaveDialog(ownerStage);
        if (selectedFile == null) {
            return;
        }

        Path configFilePath = selectedFile.toPath();
        String fileName = configFilePath.getFileName().toString();
        if (!fileName.toLowerCase().endsWith(JSON_EXTENSION)) {
            configFilePath = configFilePath.resolveSibling(fileName + JSON_EXTENSION);
        }

        Path parentDir = configFilePath.getParent();
        if (parentDir != null) {
            try {
                Files.createDirectories(parentDir);
            } catch (IOException e) {
                showErrorDialog(SAVE_ERROR_TITLE, SAVE_ERROR_HEADER, SAVE_ERROR_MESSAGE + parentDir + ": " + e.getMessage());
                return;
            }
        }

        SimulationConfigManager.saveConfig(config, configFilePath);
    }

    private void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
