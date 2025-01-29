package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.presenter.util.ConfigurationManager;
import agh.ics.oop.presenter.util.ParameterValidator;
import agh.ics.oop.presenter.util.SimulationConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.scene.control.*;
import agh.ics.oop.model.simulation.SimulationApp;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParametersPresenter {
    private static final String SIMULATION_FXML = "simulation.fxml";
    private static final String SIMULATION_TITLE = "Simulation";
    private static final String ERROR_TITLE = "Configuration Error";

    private static final String FILE_CHOOSER_TITLE = "Open Configuration File";
    private static final String JSON_FILES = "JSON Files";
    private static final String JSON_EXTENSION = "*.json";
    private static final String CONFIGS_FOLDER = "configs";

    private static final String CONFIG_ERROR = "Failed to load configuration: Invalid configuration file.";
    private static final String CREATE_FOLDER_ERROR = "Failed to create configs folder: %s";
    private static final String LOAD_CONFIG_ERROR = "Failed to load configuration: %s";

    private static final String MAP_WIDTH = "Map width";
    private static final String MAP_HEIGHT = "Map height";
    private static final String MAP_REFRESH_INTERVAL = "Map refresh interval";
    private static final String FIRE_OUTBURST_INTERVAL = "Fire Outburst Interval";
    private static final String FIRE_DURATION = "Fire Duration";
    private static final String PLANT_COUNT = "Plant count";
    private static final String PLANT_ENERGY = "Plant energy";
    private static final String PLANT_PER_DAY = "Plant per day";
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

    private ConfigMap mapConfig = new ConfigMap(10, 10, 1000, MapVariant.EQUATORS);
    private ConfigAnimal animalConfig = new ConfigAnimal(5, 100, 6, 5, 2, 2, 3, BehaviorVariant.FULL_PREDESTINATION);
    private ConfigPlant plantConfig = new ConfigPlant(5, 3, 4);

    private final ParameterValidator validator = new ParameterValidator();
    private final ConfigurationManager configManager = new ConfigurationManager();
    private Simulation simulation;

    @FXML
    private void save() {
        try {
            validateAndCreateConfigs();
            SimulationConfig config = new SimulationConfig(mapConfig, plantConfig, animalConfig);
            Stage stage = (Stage) saveToCsv.getScene().getWindow();
            configManager.saveConfig(config, stage);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void load() {
        Path configsFolderPath = Paths.get(System.getProperty("user.dir"), CONFIGS_FOLDER);

        if (!Files.exists(configsFolderPath)) {
            try {
                Files.createDirectories(configsFolderPath);
            } catch (IOException e) {
                showError(String.format(CREATE_FOLDER_ERROR, e.getMessage()));
                return;
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(FILE_CHOOSER_TITLE);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(JSON_FILES, JSON_EXTENSION)
        );

        fileChooser.setInitialDirectory(configsFolderPath.toFile());

        File selectedFile = fileChooser.showOpenDialog(mapWidth.getScene().getWindow());

        if (selectedFile == null) {
            return;
        }

        try {
            SimulationConfig config = configManager.loadConfig(selectedFile.toPath());
            if (config == null) {
                showError(CONFIG_ERROR);
                return;
            }
            mapConfig = config.mapConfig();
            plantConfig = config.plantConfig();
            animalConfig = config.animalConfig();
            updateFieldsFromConfigs();
        } catch (IllegalArgumentException e) {
            showError(String.format(LOAD_CONFIG_ERROR, e.getMessage()));
        }
    }

    @FXML
    private void accept(ActionEvent event) {
        try {
            validateAndCreateConfigs();
            simulation = new Simulation(mapConfig, animalConfig, plantConfig);
            SimulationApp.addSimulation(simulation);

            StageUtil.openNewStage(SIMULATION_FXML, SIMULATION_TITLE, simulation, loader -> {
                SimulationPresenter presenter = loader.getController();
                presenter.initializeSimulation(simulation, animalConfig, mapConfig);
            });
            closeWindow(event);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateFieldsFromConfigs() {
        mapWidth.setText(String.valueOf(mapConfig.width()));
        mapHeight.setText(String.valueOf(mapConfig.height()));
        fireMap.setSelected(mapConfig.mapVariant() == MapVariant.FIRES);
        fireOutburstInterval.setText(String.valueOf(mapConfig.fireOutburstInterval()));
        fireDuration.setText(String.valueOf(mapConfig.fireDuration()));
        plantCount.setText(String.valueOf(plantConfig.initialPlantCount()));
        plantPerDay.setText(String.valueOf(plantConfig.dailyPlantGrowth()));
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
        saveToCsv.setSelected(mapConfig.saveToCsv());
    }

    private void validateAndCreateConfigs() {
        int width = validator.validateInRange(mapWidth.getText(), 2, 100, MAP_WIDTH);
        int height = validator.validateInRange(mapHeight.getText(), 2, 100, MAP_HEIGHT);
        int refreshInterval = validator.validateInRange(mapRefreshInterval.getText(), 50, 2000, MAP_REFRESH_INTERVAL);

        int fireInterval = fireMap.isSelected()
                ? validator.validateNonNegativeInt(fireOutburstInterval.getText(), FIRE_OUTBURST_INTERVAL)
                : 0;
        int fireDurationValue = fireMap.isSelected()
                ? validator.validateNonNegativeInt(fireDuration.getText(), FIRE_DURATION)
                : 0;

        mapConfig = new ConfigMap(
                width,
                height,
                refreshInterval,
                fireMap.isSelected() ? MapVariant.FIRES : MapVariant.EQUATORS,
                fireInterval,
                fireDurationValue,
                saveToCsv.isSelected()
        );

        int plantCountValue = validator.validateNonNegativeInt(plantCount.getText(), PLANT_COUNT);
        int plantEnergyValue = validator.validateNonNegativeInt(plantEnergy.getText(), PLANT_ENERGY);
        int plantPerDayValue = validator.validateNonNegativeInt(plantPerDay.getText(), PLANT_PER_DAY);

        validator.validateEntityCount(plantCountValue, width, height, PLANT_COUNT);

        plantConfig = new ConfigPlant(
                plantCountValue,
                plantEnergyValue,
                plantPerDayValue
        );

        validateAndCreateAnimalConfig(width, height);
    }

    private void validateAndCreateAnimalConfig(int mapWidth, int mapHeight) {
        int animalCountValue = validator.validatePositiveInt(animalCount.getText(), ANIMAL_COUNT);
        int animalEnergyValue = validator.validatePositiveInt(animalEnergy.getText(), ANIMAL_ENERGY);
        int breedEnergyNeededValue = validator.validatePositiveInt(breedEnergyNeeded.getText(), BREED_ENERGY_NEEDED);
        int breedEnergyUsageValue = validator.validatePositiveInt(breedEnergyUsage.getText(), BREED_ENERGY_USAGE);
        int minMutationsValue = validator.validateNonNegativeInt(minMutations.getText(), MIN_MUTATIONS);
        int maxMutationsValue = validator.validateNonNegativeInt(maxMutations.getText(), MAX_MUTATIONS);
        int genesCountValue = validator.validateInRange(genesCount.getText(), 2, 10, GENES_COUNT);

        validator.validateMutations(minMutationsValue, maxMutationsValue);
        validator.validateBreedEnergy(breedEnergyNeededValue, breedEnergyUsageValue);
        validator.validateEntityCount(animalCountValue, mapWidth, mapHeight, ANIMAL_COUNT);

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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}