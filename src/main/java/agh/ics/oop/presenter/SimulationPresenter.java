package agh.ics.oop.presenter;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigMap;
import agh.ics.oop.model.configuration.ConfigPlant;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.datacollectors.SimulationStatisticsCSVSaver;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.renderer.BackgroundRenderer;
import agh.ics.oop.presenter.renderer.GridRenderer;
import agh.ics.oop.presenter.renderer.BorderRenderer;
import agh.ics.oop.presenter.util.ImageLoader;
import agh.ics.oop.presenter.renderer.MapRenderer;
import agh.ics.oop.presenter.statistics.StatisticsChartManager;
import agh.ics.oop.presenter.statistics.StatisticsUpdater;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.Random;

public class SimulationPresenter implements MapChangeListener {

  private static final String SIMULATION_ERROR_TITLE = "Simulation Error";
  private static final String SIMULATION_ERROR_MESSAGE = "Failed to start simulation: ";
  private static final String RESUME = "Resume";
  private static final String PAUSE = "Pause";
  private static final String PARAMETERS = "parameters.fxml";
  private static final String SIMULATION_PARAMETERS = "Simulation Parameters";

  private boolean simulationStarted = false;
  private Simulation simulation;
  private SimulationData simulationData;
  private SimulationStatistics simulationStatistics;

  @FXML
  private GridPane gridPane;
  private SimulationDataCollector dataCollector;
  private GridManager gridManager;
  private GridRenderer gridRenderer;
  @FXML
  private LineChart<Number, Number> statisticsChart;
  private StatisticsChartManager chartManager;
  private boolean isPaused = false;
  @FXML
  private Button pauseButton;
  @FXML
  private Button startButton;
  private Animal chosenAnimal;
  @FXML
  private Label freeFields;
  @FXML
  private Label genotype1;
  @FXML
  private Label genotype2;
  @FXML
  private Label genotype3;
  @FXML
  private Label averageEnergy;
  @FXML
  private Label averageLifespan;
  @FXML
  private Label averageChildren;
  @FXML
  private GridPane grassGridPane;
  private Image[] grassImages;
  private SimulationStatisticsCSVSaver statisticsCSVSaver;
  @FXML
  private Label dayCount;
  @FXML
  private Label plantCount;
  @FXML
  private Label energy;
  @FXML
  private Label children;
  @FXML
  private Label descendants;
  @FXML
  private Label genome;
  @FXML
  private Label activeGene;
  @FXML
  private Label dayOfDeath;
  @FXML
  private Label animalTitle;
  private BackgroundRenderer backgroundRenderer;

  private Image borderImage;
  private StatisticsUpdater statisticsUpdater;

  private Label[] ANIMAL_STATS_LABELS = new Label[8];
  private final static int amountOfGrassImages = 7;
  private Image[] verdantImages;
  private final static int amountOfVerdantImages = 5;
  private boolean isGrassGridInitialized = false;
  private AnimalStatisticsUpdater animalStatisticsUpdater;
  private BorderRenderer borderRenderer;
  private MapRenderer mapRenderer;
  ImageLoader imageLoader;

  @FXML
  public void initialize() {
    gridManager = new GridManager(gridPane, grassGridPane);
    gridRenderer = new GridRenderer(gridPane, gridManager);
    chartManager = new StatisticsChartManager(statisticsChart);
    ANIMAL_STATS_LABELS = new Label[] { dayCount, plantCount, energy, children, descendants, genome, activeGene, dayOfDeath };
    imageLoader =  new ImageLoader();
    grassImages = new Image[amountOfGrassImages];
    grassImages = imageLoader.getGrassImages();
    verdantImages = imageLoader.getVerdantImages();
    backgroundRenderer = new BackgroundRenderer(grassGridPane, gridManager, grassImages, verdantImages);
    isGrassGridInitialized = false;
    animalStatisticsUpdater = new AnimalStatisticsUpdater(ANIMAL_STATS_LABELS, animalTitle, dataCollector);
    borderImage = imageLoader.getBorderImage();
    borderRenderer = new BorderRenderer(gridManager, borderImage, grassGridPane);
    statisticsUpdater = new StatisticsUpdater(freeFields, genotype1, genotype2, genotype3,
            averageEnergy, averageLifespan, averageChildren);
  }

  public void onAnimalClicked(Vector2d position, List<Animal> animals) {
    chosenAnimal = getChosenAnimal(position, animals);
    animalStatisticsUpdater.updateAnimalStatistics(chosenAnimal);
  }

  public void setSimulation(Simulation simulation) {
    this.simulation = simulation;
  }

  @FXML
  private void onNewSimulationButtonClicked() {
    try {
      StageUtil.openNewStage(PARAMETERS, SIMULATION_PARAMETERS, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void initializeSimulation(Simulation simulation,
                                   ConfigMap mapConfig,
                                   ConfigAnimal animalConfig,
                                   ConfigPlant plantConfig) {
    this.simulation = simulation;
  }

  private Animal getChosenAnimal(Vector2d position, List<Animal> animalList) {
    if (animalList.isEmpty()) {
      return null;
    }
    return animalList.getFirst();
  }

  @Override
  public void mapChanged(CountDownLatch latch) {
    Platform.runLater(() -> {
      try {
        if (dataCollector != null && simulationStarted && simulation != null) {
          simulationData = dataCollector.getSimulationData();
          simulationStatistics = dataCollector.getSimulationStatistics();
          chartManager.updateChart(simulationStatistics);

          if (!isGrassGridInitialized) {
            backgroundRenderer.initializeGrassGrid(simulationData,gridManager);
            mapRenderer = new MapRenderer(gridManager, gridPane, dataCollector, this,borderRenderer,imageLoader);
            isGrassGridInitialized = true;
          }

          if (simulationStatistics != null) {
            animalStatisticsUpdater = new AnimalStatisticsUpdater(ANIMAL_STATS_LABELS, animalTitle, dataCollector);
            statisticsUpdater.updateStatistics(simulationStatistics); // Use StatisticsUpdater
            animalStatisticsUpdater.updateAnimalStatistics(chosenAnimal);
            mapRenderer.drawMap(simulationData);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        latch.countDown();
      }
    });
  }

  public void onSimulationStartClicked() {
    try {
      if (simulationStarted)
        return;
      simulation.addObserver(this);
      dataCollector = new SimulationDataCollector(simulation);
      SimulationEngine simulationEngine = new SimulationEngine(simulation);
      statisticsCSVSaver = new SimulationStatisticsCSVSaver();
      simulationEngine.runAsync();
      chosenAnimal = null;
      animalStatisticsUpdater.updateAnimalStatistics(null);
      startButton.setDisable(true);
      simulationStarted = true;
      gridManager.setGridDimensions(dataCollector.getWorldMap());
    } catch (Exception e) {
      showError(SIMULATION_ERROR_TITLE, SIMULATION_ERROR_MESSAGE + e.getMessage());
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

  private void showError(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}