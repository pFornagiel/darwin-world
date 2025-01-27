package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.abstracts.MapChangeListener;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.grid.GridRenderer;
import agh.ics.oop.presenter.grid.GridStaticRenderer;
import agh.ics.oop.presenter.renderer.*;
import agh.ics.oop.presenter.statistics.AnimalStatisticsUpdater;
import agh.ics.oop.presenter.statistics.StatisticsChartManager;
import agh.ics.oop.presenter.statistics.StatisticsUpdater;
import agh.ics.oop.presenter.util.ImageLoader;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SimulationPresenter implements MapChangeListener {

  private static final String SIMULATION_ERROR_TITLE = "Simulation Error";
  private static final String SIMULATION_ERROR_MESSAGE = "Failed to start simulation: ";
  private static final String RESUME = "Resume";
  private static final String PAUSE = "Pause";
  private static final String PARAMETERS = "parameters.fxml";
  private static final String SIMULATION_PARAMETERS = "Simulation Parameters";
  private static final int MAX_MAP_SIZE_FOR_IMAGES = 400;


  @FXML
  private Canvas staticCanvas;
  @FXML
  private Canvas simulationCanvas;

  @FXML
  private LineChart<Number, Number> statisticsChart;
  @FXML
  private Button pauseButton;
  @FXML
  private Button startButton;
  @FXML
  private Label freeFields, genotype1, genotype2, genotype3, averageEnergy, averageLifespan, averageChildren;
  @FXML
  private Label dayCount, plantCount, energy, children, descendants, genome, activeGene, dayOfDeath, animalTitle;

  private boolean isPaused = false;
  private Simulation simulation;
  private SimulationData simulationData;
  private SimulationStatistics simulationStatistics;
  private Animal chosenAnimal;

  private StatisticsChartManager chartManager;
  private SimulationDataCollector dataCollector;
  private MapRenderer mapRenderer;
  private StatisticsUpdater statisticsUpdater;
  private AnimalStatisticsUpdater animalStatisticsUpdater;
  private ImageLoader imageLoader;
  private ConfigAnimal animalConfig;

  @FXML
  public void initialize() {
    initializeUIComponents();
    initializeHelpersAndManagers();
  }

  private void initializeUIComponents() {
    chartManager = new StatisticsChartManager(statisticsChart);
  }

  private void initializeHelpersAndManagers() {
    imageLoader = new ImageLoader();
    statisticsUpdater = new StatisticsUpdater(freeFields, genotype1, genotype2, genotype3, averageEnergy, averageLifespan, averageChildren);
  }

  @FXML
  private void onNewSimulationButtonClicked() {
    try {
      StageUtil.openNewStage(PARAMETERS, SIMULATION_PARAMETERS, simulation, null);
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
      simulationData = dataCollector.getSimulationData();

      Label[] labels = new Label[] {dayCount, plantCount, energy, children, descendants, genome, activeGene, dayOfDeath};
      animalStatisticsUpdater = new AnimalStatisticsUpdater(labels, animalTitle, dataCollector);

      GridManager gridManager = new GridManager(simulationCanvas, dataCollector.getWorldMapSize());
      gridManager.setOnClickEventHandling(this::onAnimalClicked);

      GridStaticRenderer gridStaticRenderer = new GridStaticRenderer(staticCanvas, gridManager, imageLoader, MAX_MAP_SIZE_FOR_IMAGES);
      gridStaticRenderer.drawBackground(simulationData);
      gridStaticRenderer.drawBorder();

      GridRenderer gridRenderer = new GridRenderer(simulationCanvas, gridManager);

      mapRenderer = new MapRenderer(gridManager, gridRenderer, dataCollector, imageLoader, MAX_MAP_SIZE_FOR_IMAGES, animalConfig);

      startButton.setDisable(true);
      pauseButton.setDisable(false);

      SimulationEngine simulationEngine = new SimulationEngine(simulation);
      simulationEngine.runAsync();

    } catch (Exception e) {
      showError(SIMULATION_ERROR_MESSAGE + e.getMessage());
    }
  }

  private void onAnimalClicked(Vector2d position) {
    chosenAnimal =
        simulationData.animalPositionSet().contains(position)
            ? dataCollector.getAnimalsAtPosition(position).getFirst()
            : null;
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

  public void initializeSimulation(Simulation simulation, ConfigAnimal animalConfig) {
    this.simulation = simulation;
    this.animalConfig = animalConfig;
  }

}