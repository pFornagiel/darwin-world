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
    private static final String SIMULATION = "Simulation";
    private static final String FIRE_OUTBURST_INTERVAL = "Fire Outburst Interval";
    private static final String FIRE_DURATION = "Fire Duration";

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
    @FXML private TextField mapRefreshInterval;
    @FXML private TextField fireOutburstInterval;
    @FXML private TextField fireDuration;

    private ConfigMap mapConfig;
    private ConfigAnimal animalConfig;
    private ConfigPlant plantConfig;

    @FXML private void mapWidth() {}
    @FXML private void mapHeight() {}
    @FXML private void plantCount() {}
    @FXML private void plantEnergy() {}
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
    @FXML
    private void initialize() {
        mapConfig = new ConfigMap(10, 10, 1000, MapVariant.EQUATORS);
        animalConfig = new ConfigAnimal(5, 100, 6, 5, 2, 2, 3, BehaviorVariant.FULL_PREDESTINATION);
        plantConfig = new ConfigPlant(5, 3, 4);
    }

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
        int width = validatePositiveInt(mapWidth.getText(), MAP_WIDTH);
        int height = validatePositiveInt(mapHeight.getText(), MAP_HEIGHT);
        int refreshInterval = 100;
        int fireInterval = fireMap.isSelected()
                ? validatePositiveInt(fireOutburstInterval.getText(), FIRE_OUTBURST_INTERVAL)
                : -1;
        int fireDurationValue = fireMap.isSelected()
                ? validatePositiveInt(fireDuration.getText(), FIRE_DURATION )
                : -1;

        mapConfig = new ConfigMap(
                width,
                height,
                refreshInterval,
                fireMap.isSelected() ? MapVariant.FIRES : MapVariant.EQUATORS,
                fireInterval,
                fireDurationValue
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

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
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