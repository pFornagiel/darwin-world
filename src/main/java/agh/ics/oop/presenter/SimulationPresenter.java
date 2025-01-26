package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigMap;
import agh.ics.oop.model.configuration.ConfigPlant;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.renderer.*;
import agh.ics.oop.presenter.statistics.AnimalStatisticsUpdater;
import agh.ics.oop.presenter.statistics.StatisticsChartManager;
import agh.ics.oop.presenter.statistics.StatisticsUpdater;
import agh.ics.oop.presenter.util.ImageLoader;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SimulationPresenter implements MapChangeListener {

  private static final String SIMULATION_ERROR_TITLE = "Simulation Error";
  private static final String SIMULATION_ERROR_MESSAGE = "Failed to start simulation: ";
  private static final String RESUME = "Resume";
  private static final String PAUSE = "Pause";
  private static final String PARAMETERS = "parameters.fxml";
  private static final String SIMULATION_PARAMETERS = "Simulation Parameters";

  @FXML private GridPane gridPane;
  @FXML private GridPane grassGridPane;
  @FXML private LineChart<Number, Number> statisticsChart;
  @FXML private Button pauseButton;
  @FXML private Button startButton;
  @FXML private Label freeFields, genotype1, genotype2, genotype3, averageEnergy, averageLifespan, averageChildren;
  @FXML private Label dayCount, plantCount, energy, children, descendants, genome, activeGene, dayOfDeath, animalTitle;

  private boolean isPaused = false;
  private Simulation simulation;
  private SimulationData simulationData;
  private SimulationStatistics simulationStatistics;
  private Animal chosenAnimal;

  private GridManager gridManager;
  private StatisticsChartManager chartManager;
  private SimulationDataCollector dataCollector;
  private BackgroundRenderer backgroundRenderer;
  private BorderRenderer borderRenderer;
  private MapRenderer mapRenderer;
  private StatisticsUpdater statisticsUpdater;
  private AnimalStatisticsUpdater animalStatisticsUpdater;
  private ImageLoader imageLoader;
  private static final int MAX_MAP_SIZE_FOR_IMAGES = 400;


  @FXML
  public void initialize() {
    initializeUIComponents();
    initializeHelpersAndManagers();
  }

  private void initializeUIComponents() {
    gridManager = new GridManager(gridPane, grassGridPane);
    chartManager = new StatisticsChartManager(statisticsChart);
  }

  private void initializeHelpersAndManagers() {
    imageLoader = new ImageLoader();
    backgroundRenderer = new BackgroundRenderer(grassGridPane, imageLoader.getGrassImages(), imageLoader.getVerdantImages());
    borderRenderer = new BorderRenderer(gridManager, imageLoader.getBorderImage(), grassGridPane, MAX_MAP_SIZE_FOR_IMAGES);
    statisticsUpdater = new StatisticsUpdater(freeFields, genotype1, genotype2, genotype3, averageEnergy, averageLifespan, averageChildren);
  }

  @FXML
  private void onNewSimulationButtonClicked() {
    try {
      StageUtil.openNewStage(PARAMETERS, SIMULATION_PARAMETERS, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onPauseButtonClicked() {
    if (simulation != null) {
      isPaused = !isPaused;
      simulation.togglePause();
      pauseButton.setText(isPaused ? RESUME : PAUSE);
    }
  }

  public void onSimulationStartClicked() {
    try {
      simulation.addObserver(this);
      dataCollector = new SimulationDataCollector(simulation);
      SimulationEngine simulationEngine = new SimulationEngine(simulation);
      simulationEngine.runAsync();

      chosenAnimal = null;
      animalStatisticsUpdater = new AnimalStatisticsUpdater(new Label[]{dayCount, plantCount, energy, children, descendants, genome, activeGene, dayOfDeath}, animalTitle, dataCollector);
      animalStatisticsUpdater.updateAnimalStatistics(null);

      gridManager.setGridDimensions(dataCollector.getWorldMap());
      backgroundRenderer.initializeGrassGrid(simulationData, gridManager);
      mapRenderer = new MapRenderer(gridManager, gridPane, dataCollector, this, borderRenderer, imageLoader, MAX_MAP_SIZE_FOR_IMAGES);

      startButton.setDisable(true);
      pauseButton.setDisable(false);

    } catch (Exception e) {
      showError(SIMULATION_ERROR_MESSAGE + e.getMessage());
    }
  }

  public void onAnimalClicked(List<Animal> animals) {
    chosenAnimal = animals.isEmpty() ? null : animals.getFirst();
    animalStatisticsUpdater.updateAnimalStatistics(chosenAnimal);
  }

  @Override
  public void mapChanged(CountDownLatch latch) {
    Platform.runLater(() -> {
      try {
        if (dataCollector == null) return;

        simulationData = dataCollector.getSimulationData();
        simulationStatistics = dataCollector.getSimulationStatistics();
        animalStatisticsUpdater.updateAnimalStatistics(chosenAnimal);
        chartManager.updateChart(simulationStatistics);
        statisticsUpdater.updateStatistics(simulationStatistics);

        mapRenderer.drawMap(simulationData);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        latch.countDown();
      }
    });
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(SimulationPresenter.SIMULATION_ERROR_TITLE);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void setSimulation(Simulation simulation) {
    this.simulation = simulation;
  }

  public void initializeSimulation(Simulation simulation) {
    this.simulation = simulation;
  }
}