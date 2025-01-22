package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.simulation.Simulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.scene.control.*;
import agh.ics.oop.model.simulation.SimulationApp;
import javafx.stage.Stage;
import java.io.IOException;

public class ParametersPresenter {
    private static final String SIMULATION_FXML = "simulation.fxml";
    private static final String CONFIGURATION_ERROR = "Configuration Error";
    private static final String SIMULATION = "Simulation";
    private static final String MUST_BE_POSITIVE = " must be positive";
    private static final String MUST_BE_VALID_NUMBER = " must be a valid number";
    private static final String CANNOT_BE_NEGATIVE = " cannot be negative";
    private static final String MUTATIONS_ERROR = "Minimum mutations cannot be greater than maximum mutations.";
    private static final String BREED_ENERGY_ERROR = "Breed energy needed cannot be less than breed energy usage.";
    private static final String DIMENSION_ERROR = "Dimensions must be between 2 and 100.";
    private static final String ENTITY_COUNT_ERROR = "Count cannot exceed map area.";
    private static final String GENES_COUNT_ERROR = "Genes count must be between 2 and 10.";
    private static final String REFRESH_INTERVAL_ERROR = "Map refresh interval must be between 100 and 1000.";
    private static final String ERROR_MAP_WIDTH = "Map width";
    private static final String ERROR_MAP_HEIGHT = "Map height";
    private static final String ERROR_REFRESH_INTERVAL = "Map refresh interval";
    private static final String ERROR_FIRE_OUTBURST = "Fire Outburst Interval";
    private static final String ERROR_FIRE_DURATION = "Fire Duration";
    private static final String ERROR_PLANT_COUNT = "Plant count";
    private static final String ERROR_PLANT_ENERGY = "Plant energy";
    private static final String ERROR_ANIMAL_COUNT = "Animal count";
    private static final String ERROR_ANIMAL_ENERGY = "Animal energy";
    private static final String ERROR_BREED_ENERGY_NEEDED = "Breed energy needed";
    private static final String ERROR_BREED_ENERGY_USAGE = "Breed energy usage";
    private static final String ERROR_MIN_MUTATIONS = "Min mutations";
    private static final String ERROR_MAX_MUTATIONS = "Max mutations";
    private static final String ERROR_GENES_COUNT = "Genes count";
    @FXML private TextField mapWidth;
    @FXML private TextField mapHeight;
    @FXML private TextField plantCount;
    @FXML private TextField plantEnergy;
    @FXML private TextField plantPerDay;
    @FXML private TextField animalCount;
    @FXML private TextField animalEnergy;
    @FXML private TextField breedEnergyNeeded;
    @FXML private TextField breedEnergyUsage;
    @FXML private TextField minMutations;
    @FXML private TextField maxMutations;
    @FXML private TextField genesCount;
    @FXML private CheckBox fireMap;
    @FXML private CheckBox insanity;
    @FXML private CheckBox saveToCsv;
    @FXML private TextField mapRefreshInterval;
    @FXML private TextField fireOutburstInterval;
    @FXML private TextField fireDuration;
    @FXML private void mapWidth() {}
    @FXML private void mapHeight() {}
    @FXML private void plantCount() {}
    @FXML private void plantEnergy() {}
    @FXML private void plantPerDay() {}
    @FXML private void animalCount() {}
    @FXML private void animalEnergy() {}
    @FXML private void breedEnergyNeeded() {}
    @FXML private void breedEnergyUsage() {}
    @FXML private void minMutations() {}
    @FXML private void maxMutations() {}
    @FXML private void genesCount() {}
    @FXML private void mapRefreshInterval() {}
    @FXML private void fireOutburstInterval() {}
    @FXML private void fireDuration() {}
    private ConfigMap mapConfig = new ConfigMap(10, 10, 1000, MapVariant.EQUATORS);
    private ConfigAnimal animalConfig = new ConfigAnimal(5, 100, 6, 5, 2, 2, 3, BehaviorVariant.FULL_PREDESTINATION);
    private ConfigPlant plantConfig  = new ConfigPlant(5, 3, 4);
    private static final int MIN_MAP_WIDTH = 2;
    private static final int MAX_MAP_WIDTH = 100;
    private static final int MIN_MAP_HEIGHT = 2;
    private static final int MAX_MAP_HEIGHT = 100;
    private static final int MIN_REFRESH_INTERVAL = 100;
    private static final int MAX_REFRESH_INTERVAL = 1000;
    private static final int MIN_GENES_COUNT = 2;
    private static final int MAX_GENES_COUNT = 10;
    private static final int DEFAULT_SPREAD_RADIUS = 5;

    @FXML
    private void save() {
        try {
            validateAndCreateConfigs();
            SimulationConfig config = new SimulationConfig(mapConfig, plantConfig, animalConfig);
            SimulationConfigManager.saveConfig(config);
        } catch (IllegalArgumentException e) {
            showError(CONFIGURATION_ERROR, e.getMessage());
        }
    }

    @FXML
    private void load() {
        SimulationConfig config = SimulationConfigManager.loadConfig();
        if (config != null) {
            mapConfig = config.mapConfig();
            plantConfig = config.plantConfig();
            animalConfig = config.animalConfig();
            updateFieldsFromConfigs();
        }
    }

    private void updateFieldsFromConfigs() {
        mapWidth.setText(String.valueOf(mapConfig.width()));
        mapHeight.setText(String.valueOf(mapConfig.height()));
        fireMap.setSelected(mapConfig.mapVariant() == MapVariant.FIRES);
        fireOutburstInterval.setText(String.valueOf(mapConfig.fireOutburstInterval()));
        fireDuration.setText(String.valueOf(mapConfig.fireDuration()));
        plantCount.setText(String.valueOf(plantConfig.initialPlantCount()));
        plantEnergy.setText(String.valueOf(plantConfig.energyPerPlant()));
        animalCount.setText(String.valueOf(animalConfig.initialAnimalCount()));
        animalEnergy.setText(String.valueOf(animalConfig.initialEnergy()));
        breedEnergyNeeded.setText(String.valueOf(animalConfig.energyToReproduce()));
        breedEnergyUsage.setText(String.valueOf(animalConfig.energyConsumedByParents()));
        minMutations.setText(String.valueOf(animalConfig.minMutations()));
        maxMutations.setText(String.valueOf(animalConfig.maxMutations()));
        genesCount.setText(String.valueOf(animalConfig.genomeLength()));
        insanity.setSelected(animalConfig.behaviorVariant() == BehaviorVariant.CRAZINESS);
        mapRefreshInterval.setText(String.valueOf(mapConfig.mapRefreshInterval()));
    }

    @FXML
    private void accept(ActionEvent event) {
        try {
            validateAndCreateConfigs();
            Simulation simulation = new Simulation(mapConfig, animalConfig, plantConfig);
            SimulationApp.addSimulation(simulation);
            StageUtil.openNewStage(SIMULATION_FXML, SIMULATION, loader -> {
                SimulationPresenter presenter = loader.getController();
                presenter.initializeSimulation(simulation);
            });
            closeWindow(event);
        } catch (IllegalArgumentException e) {
            showError(CONFIGURATION_ERROR, e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void validateAndCreateConfigs() {
        int width = validateInRange(mapWidth.getText(), MIN_MAP_WIDTH, MAX_MAP_WIDTH, ERROR_MAP_WIDTH);
        int height = validateInRange(mapHeight.getText(), MIN_MAP_HEIGHT, MAX_MAP_HEIGHT, ERROR_MAP_HEIGHT);
        int refreshInterval = validateInRange(mapRefreshInterval.getText(), MIN_REFRESH_INTERVAL, MAX_REFRESH_INTERVAL, ERROR_REFRESH_INTERVAL);
        int fireInterval = fireMap.isSelected()
                ? validatePositiveInt(fireOutburstInterval.getText(), ERROR_FIRE_OUTBURST)
                : -1;
        int fireDurationValue = fireMap.isSelected()
                ? validatePositiveInt(fireDuration.getText(), ERROR_FIRE_DURATION)
                : -1;

        mapConfig = new ConfigMap(
                width,
                height,
                refreshInterval,
                fireMap.isSelected() ? MapVariant.FIRES : MapVariant.EQUATORS,
                fireInterval,
                fireDurationValue
        );

        int plantCountValue = validateNonNegativeInt(plantCount.getText(), ERROR_PLANT_COUNT);
        int plantEnergyValue = validatePositiveInt(plantEnergy.getText(), ERROR_PLANT_ENERGY);

        validateEntityCount(plantCountValue, width, height, ERROR_PLANT_COUNT);

        plantConfig = new ConfigPlant(
                plantCountValue,
                plantEnergyValue,
                DEFAULT_SPREAD_RADIUS
        );

        validateAndCreateAnimalConfig(width, height);
    }

    private void validateAndCreateAnimalConfig(int mapWidth, int mapHeight) {
        int animalCountValue = validatePositiveInt(animalCount.getText(), ERROR_ANIMAL_COUNT);
        int animalEnergyValue = validatePositiveInt(animalEnergy.getText(), ERROR_ANIMAL_ENERGY);
        int breedEnergyNeededValue = validatePositiveInt(breedEnergyNeeded.getText(), ERROR_BREED_ENERGY_NEEDED);
        int breedEnergyUsageValue = validatePositiveInt(breedEnergyUsage.getText(), ERROR_BREED_ENERGY_USAGE);
        int minMutationsValue = validateNonNegativeInt(minMutations.getText(), ERROR_MIN_MUTATIONS);
        int maxMutationsValue = validatePositiveInt(maxMutations.getText(), ERROR_MAX_MUTATIONS);
        int genesCountValue = validateInRange(genesCount.getText(), MIN_GENES_COUNT, MAX_GENES_COUNT, ERROR_GENES_COUNT);

        if (minMutationsValue > maxMutationsValue) {
            throw new IllegalArgumentException(MUTATIONS_ERROR);
        }

        if (breedEnergyNeededValue < breedEnergyUsageValue) {
            throw new IllegalArgumentException(BREED_ENERGY_ERROR);
        }

        validateEntityCount(animalCountValue, mapWidth, mapHeight, ERROR_ANIMAL_COUNT);

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
    private void validateEntityCount(int count, int width, int height, String fieldName) {
        if (count > width * height) {
            throw new IllegalArgumentException(String.format("%s %s", fieldName, ENTITY_COUNT_ERROR));
        }
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

    private int validateInRange(String value, int min, int max, String fieldName) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < min || parsedValue > max) {
                throw new IllegalArgumentException(String.format("%s must be between %d and %d.", fieldName, min, max));
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

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
