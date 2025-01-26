package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.presenter.util.ConfigurationManager;
import agh.ics.oop.presenter.util.ParameterValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.scene.control.*;
import agh.ics.oop.model.simulation.SimulationApp;
import javafx.stage.Stage;
import java.io.IOException;

public class ParametersPresenter {

    private static final String SIMULATION_FXML = "simulation.fxml";
    private static final String SIMULATION = "Simulation";
    private static final String ERROR_TITLE = "Configuration Error";

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
    private ConfigPlant plantConfig  = new ConfigPlant(5, 3, 4);

    private final ParameterValidator validator = new ParameterValidator();
    private final ConfigurationManager configManager = new ConfigurationManager();

    @FXML
    private void save() {
        try {
            validateAndCreateConfigs();
            SimulationConfig config = new SimulationConfig(mapConfig, plantConfig, animalConfig);
            configManager.saveConfig(config);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void load() {
        try {
            SimulationConfig config = configManager.loadConfig();
            mapConfig = config.mapConfig();
            plantConfig = config.plantConfig();
            animalConfig = config.animalConfig();
            updateFieldsFromConfigs();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
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

    @FXML
    private void accept(ActionEvent event) {
        try {
            validateAndCreateConfigs();
            Simulation simulation = new Simulation(mapConfig, animalConfig, plantConfig);
            SimulationApp.addSimulation(simulation);
            StageUtil.openNewStage(SIMULATION_FXML, SIMULATION, loader -> {
                SimulationPresenter presenter = loader.getController();
                presenter.initializeSimulation(simulation, mapConfig, animalConfig, plantConfig);
            });
            closeWindow(event);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateAndCreateConfigs() {
        int width = validator.validateInRange(mapWidth.getText(), 2, 100, "Map width");
        int height = validator.validateInRange(mapHeight.getText(), 2, 100, "Map height");
        int refreshInterval = validator.validateInRange(mapRefreshInterval.getText(), 100, 1000, "Map refresh interval");
        int fireInterval = fireMap.isSelected()
                ? validator.validatePositiveInt(fireOutburstInterval.getText(), "Fire Outburst Interval")
                : -1;
        int fireDurationValue = fireMap.isSelected()
                ? validator.validatePositiveInt(fireDuration.getText(), "Fire Duration")
                : -1;

        mapConfig = new ConfigMap(
                width,
                height,
                refreshInterval,
                fireMap.isSelected() ? MapVariant.FIRES : MapVariant.EQUATORS,
                fireInterval,
                fireDurationValue,
                saveToCsv.isSelected()
        );

        int plantCountValue = validator.validateNonNegativeInt(plantCount.getText(), "Plant count");
        int plantEnergyValue = validator.validatePositiveInt(plantEnergy.getText(), "Plant energy");
        int plantPerDayValue = validator.validatePositiveInt(plantPerDay.getText(), "Plant per day");

        validator.validateEntityCount(plantCountValue, width, height, "Plant count");

        plantConfig = new ConfigPlant(
                plantCountValue,
                plantEnergyValue,
                plantPerDayValue
        );

        validateAndCreateAnimalConfig(width, height);
    }

    private void validateAndCreateAnimalConfig(int mapWidth, int mapHeight) {
        int animalCountValue = validator.validatePositiveInt(animalCount.getText(), "Animal count");
        int animalEnergyValue = validator.validatePositiveInt(animalEnergy.getText(), "Animal energy");
        int breedEnergyNeededValue = validator.validatePositiveInt(breedEnergyNeeded.getText(), "Breed energy needed");
        int breedEnergyUsageValue = validator.validatePositiveInt(breedEnergyUsage.getText(), "Breed energy usage");
        int minMutationsValue = validator.validateNonNegativeInt(minMutations.getText(), "Min mutations");
        int maxMutationsValue = validator.validatePositiveInt(maxMutations.getText(), "Max mutations");
        int genesCountValue = validator.validateInRange(genesCount.getText(), 2, 10, "Genes count");

        validator.validateMutations(minMutationsValue, maxMutationsValue);
        validator.validateBreedEnergy(breedEnergyNeededValue, breedEnergyUsageValue);
        validator.validateEntityCount(animalCountValue, mapWidth, mapHeight, "Animal count");

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