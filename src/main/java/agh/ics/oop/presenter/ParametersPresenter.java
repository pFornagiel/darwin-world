// ParametersPresenter.java
package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import agh.ics.oop.model.simulation.SimulationApp;

public class ParametersPresenter {
    @FXML private TextField mapWidth;
    @FXML private TextField mapHeight;
    @FXML private TextField plantCount;
    @FXML private TextField plantEnergy;
    @FXML private TextField animalCount;
    @FXML private TextField animalEnergy;
    @FXML private TextField breedEnergyNeeded;
    @FXML private TextField breedEnergyUsage;
    @FXML private TextField minMutations;
    @FXML private TextField maxMutations;
    @FXML private TextField genesCount;
    @FXML private CheckBox fireMap;
    @FXML private CheckBox insanity;

    private ConfigMap mapConfig;
    private ConfigAnimal animalConfig;
    private ConfigPlant plantConfig;

    @FXML
    private void initialize() {
        mapConfig = new ConfigMap(10, 10, 1000, MapVariant.EQUATORS);
        animalConfig = new ConfigAnimal(5, 100, 6, 5, 2, 2, 3, BehaviorVariant.FULL_PREDESTINATION);
        plantConfig = new ConfigPlant(5, 3, 4);
        System.out.println("ParametersPresenter initialized with default configurations.");
    }

    @FXML
    private void save() {
        SimulationConfig config = new SimulationConfig(
                mapWidth.getText(),
                mapHeight.getText(),
                plantCount.getText(),
                plantEnergy.getText(),
                animalCount.getText(),
                animalEnergy.getText(),
                breedEnergyNeeded.getText(),
                breedEnergyUsage.getText(),
                minMutations.getText(),
                maxMutations.getText(),
                genesCount.getText(),
                fireMap.isSelected(),
                insanity.isSelected()
        );
        SimulationConfigManager.saveConfig(config);
    }

    @FXML
    private void load() {
        SimulationConfig config = SimulationConfigManager.loadConfig();
        if (config != null) {
            mapWidth.setText(config.mapWidth);
            mapHeight.setText(config.mapHeight);
            plantCount.setText(config.plantCount);
            plantEnergy.setText(config.plantEnergy);
            animalCount.setText(config.animalCount);
            animalEnergy.setText(config.animalEnergy);
            breedEnergyNeeded.setText(config.breedEnergyNeeded);
            breedEnergyUsage.setText(config.breedEnergyUsage);
            minMutations.setText(config.minMutations);
            maxMutations.setText(config.maxMutations);
            genesCount.setText(config.genesCount);
            fireMap.setSelected(config.fireMap);
            insanity.setSelected(config.insanity);
            System.out.println("Configuration loaded successfully.");
        }
    }

    @FXML
    private void accept() {
        try {
            validateAndCreateConfigs();
            SimulationApp.setConfigurations(mapConfig, animalConfig, plantConfig);
            SimulationApp.switchScene("simulation.fxml");
        } catch (IllegalArgumentException e) {
            showError("Configuration Error", e.getMessage());
        }
    }

    private void validateAndCreateConfigs() {
        int width = validatePositiveInt(mapWidth.getText(), "Map width");
        int height = validatePositiveInt(mapHeight.getText(), "Map height");
        mapConfig = new ConfigMap(
                width,
                height,
                1000,
                fireMap.isSelected() ? MapVariant.FIRES : MapVariant.EQUATORS
        );

        int plantCountValue = validateNonNegativeInt(plantCount.getText(), "Plant count");
        int plantEnergyValue = validatePositiveInt(plantEnergy.getText(), "Plant energy");
        plantConfig = new ConfigPlant(
                plantCountValue,
                plantEnergyValue,
                5
        );

        int animalCountValue = validatePositiveInt(animalCount.getText(), "Animal count");
        int animalEnergyValue = validatePositiveInt(animalEnergy.getText(), "Animal energy");
        int breedEnergyNeededValue = validatePositiveInt(breedEnergyNeeded.getText(), "Breed energy needed");
        int breedEnergyUsageValue = validatePositiveInt(breedEnergyUsage.getText(), "Breed energy usage");
        int minMutationsValue = validateNonNegativeInt(minMutations.getText(), "Min mutations");
        int maxMutationsValue = validatePositiveInt(maxMutations.getText(), "Max mutations");
        int genesCountValue = validatePositiveInt(genesCount.getText(), "Genes count");

        if (minMutationsValue > maxMutationsValue) {
            throw new IllegalArgumentException("Minimum mutations cannot be greater than maximum mutations.");
        }

        animalConfig = new ConfigAnimal(
                animalCountValue,
                animalEnergyValue,
                breedEnergyNeededValue,
                breedEnergyUsageValue,
                minMutationsValue,
                maxMutationsValue,
                genesCountValue,
                insanity.isSelected() ? BehaviorVariant.CRAZINESS : BehaviorVariant.FULL_PREDESTINATION
        );
    }

    private int validatePositiveInt(String value, String fieldName) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue <= 0) {
                throw new IllegalArgumentException(fieldName + " must be positive");
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    private int validateNonNegativeInt(String value, String fieldName) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative");
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Event handlers for UI elements
    @FXML private void mapWidth(ActionEvent event) {
        System.out.println("Map Width: " + mapWidth.getText());
    }

    @FXML private void mapHeight(ActionEvent event) {
        System.out.println("Map Height: " + mapHeight.getText());
    }

    @FXML private void plantCount(ActionEvent event) {
        System.out.println("Plant Count: " + plantCount.getText());
    }

    @FXML private void plantEnergy(ActionEvent event) {
        System.out.println("Plant Energy: " + plantEnergy.getText());
    }

    @FXML private void animalCount(ActionEvent event) {
        System.out.println("Animal Count: " + animalCount.getText());
    }

    @FXML private void animalEnergy(ActionEvent event) {
        System.out.println("Animal Energy: " + animalEnergy.getText());
    }

    @FXML private void breedEnergyNeeded(ActionEvent event) {
        System.out.println("Breed Energy Needed: " + breedEnergyNeeded.getText());
    }

    @FXML private void breedEnergyUsage(ActionEvent event) {
        System.out.println("Breed Energy Usage: " + breedEnergyUsage.getText());
    }

    @FXML private void minMutations(ActionEvent event) {
        System.out.println("Min Mutations: " + minMutations.getText());
    }

    @FXML private void maxMutations(ActionEvent event) {
        System.out.println("Max Mutations: " + maxMutations.getText());
    }

    @FXML private void genesCount(ActionEvent event) {
        System.out.println("Genes Count: " + genesCount.getText());
    }

    @FXML private void fireMap(ActionEvent event) {
        System.out.println("Fire Map checkbox toggled.");
    }

    @FXML private void insanity(ActionEvent event) {
        System.out.println("Insanity checkbox toggled.");
    }
}