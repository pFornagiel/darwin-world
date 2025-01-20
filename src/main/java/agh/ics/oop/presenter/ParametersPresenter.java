package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import agh.ics.oop.model.simulation.SimulationApp;

public class ParametersPresenter {
    private static final String SIMULATION_FXML = "simulation.fxml";
    private static final String CONFIGURATION_ERROR = "Configuration Error";

    private static final String MUST_BE_POSITIVE = " must be positive";
    private static final String MUST_BE_VALID_NUMBER = " must be a valid number";
    private static final String CANNOT_BE_NEGATIVE = " cannot be negative";
    private static final String MUTATIONS_ERROR = "Minimum mutations cannot be greater than maximum mutations.";

    private static final String MAP_WIDTH = "Map width";
    private static final String MAP_HEIGHT = "Map height";
    private static final String PLANT_COUNT = "Plant count";
    private static final String PLANT_ENERGY = "Plant energy";
    private static final String ANIMAL_COUNT = "Animal count";
    private static final String ANIMAL_ENERGY = "Animal energy";
    private static final String BREED_ENERGY_NEEDED = "Breed energy needed";
    private static final String BREED_ENERGY_USAGE = "Breed energy usage";
    private static final String MIN_MUTATIONS = "Min mutations";
    private static final String MAX_MUTATIONS = "Max mutations";
    private static final String GENES_COUNT = "Genes count";

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

    @FXML private void mapWidth() {}
    @FXML private void mapHeight() {}
    @FXML private void plantCount() {}
    @FXML private void plantEnergy() {}
    @FXML private void fireMap() {}
    @FXML private void insanity() {}

    @FXML
    private void initialize() {
        mapConfig = new ConfigMap(10, 10, 1000, MapVariant.EQUATORS);
        animalConfig = new ConfigAnimal(5, 100, 6, 5, 2, 2, 3, BehaviorVariant.FULL_PREDESTINATION);
        plantConfig = new ConfigPlant(5, 3, 4);
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
            updateFieldsFromConfig(config);
        }
    }

    private void updateFieldsFromConfig(SimulationConfig config) {
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
    }

    @FXML
    private void accept() {
        try {
            validateAndCreateConfigs();
            SimulationApp.setConfigurations(mapConfig, animalConfig, plantConfig);
            SimulationApp.switchScene(SIMULATION_FXML);
        } catch (IllegalArgumentException e) {
            showError(CONFIGURATION_ERROR, e.getMessage());
        }
    }

    private void validateAndCreateConfigs() {
        int width = validatePositiveInt(mapWidth.getText(), MAP_WIDTH);
        int height = validatePositiveInt(mapHeight.getText(), MAP_HEIGHT);
        mapConfig = new ConfigMap(
                width,
                height,
                100,
                fireMap.isSelected() ? MapVariant.FIRES : MapVariant.EQUATORS
        );

        int plantCountValue = validateNonNegativeInt(plantCount.getText(), PLANT_COUNT);
        int plantEnergyValue = validatePositiveInt(plantEnergy.getText(), PLANT_ENERGY);
        plantConfig = new ConfigPlant(
                plantCountValue,
                plantEnergyValue,
                5
        );

        validateAndCreateAnimalConfig();
    }

    private void validateAndCreateAnimalConfig() {
        int animalCountValue = validatePositiveInt(animalCount.getText(), ANIMAL_COUNT);
        int animalEnergyValue = validatePositiveInt(animalEnergy.getText(), ANIMAL_ENERGY);
        int breedEnergyNeededValue = validatePositiveInt(breedEnergyNeeded.getText(), BREED_ENERGY_NEEDED);
        int breedEnergyUsageValue = validatePositiveInt(breedEnergyUsage.getText(), BREED_ENERGY_USAGE);
        int minMutationsValue = validateNonNegativeInt(minMutations.getText(), MIN_MUTATIONS);
        int maxMutationsValue = validatePositiveInt(maxMutations.getText(), MAX_MUTATIONS);
        int genesCountValue = validatePositiveInt(genesCount.getText(), GENES_COUNT);

        if (minMutationsValue > maxMutationsValue) {
            throw new IllegalArgumentException(MUTATIONS_ERROR);
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
                throw new IllegalArgumentException(fieldName + MUST_BE_POSITIVE);
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + MUST_BE_VALID_NUMBER);
        }
    }

    private int validateNonNegativeInt(String value, String fieldName) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 0) {
                throw new IllegalArgumentException(fieldName + CANNOT_BE_NEGATIVE);
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + MUST_BE_VALID_NUMBER);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}