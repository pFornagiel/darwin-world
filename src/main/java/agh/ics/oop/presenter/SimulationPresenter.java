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
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.model.worldmap.abstracts.WorldMap;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.renderer.GridRenderer;
import agh.ics.oop.presenter.renderer.BorderRenderer;
import agh.ics.oop.presenter.statistics.StatisticsChartManager;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.Random;
import agh.ics.oop.model.exception.util.DatacollectorNotInitialized;

import static agh.ics.oop.presenter.util.AnimalColor.getAnimalColor;
import static agh.ics.oop.presenter.util.Rounder.roundToTwoDecimal;

public class SimulationPresenter implements MapChangeListener {

  private static final String NO_ANIMAL_SELECTED = "No Animal Selected";
  private static final String ALIVE = "Alive";
  private static final String ANIMAL_AT = "Animal at (%d, %d)";
  private static final String SIMULATION_ERROR_TITLE = "Simulation Error";
  private static final String SIMULATION_ERROR_MESSAGE = "Failed to start simulation: ";
  private static final String RESUME = "Resume";
  private static final String PAUSE = "Pause";
  private static final String DASH = "-";
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
  @FXML private Label dayCount;
  @FXML private Label plantCount;
  @FXML private Label energy;
  @FXML private Label children;
  @FXML private Label descendants;
  @FXML private Label genome;
  @FXML private Label activeGene;
  @FXML private Label dayOfDeath;
  @FXML private Label animalTitle;
  private Image fireImage = new Image(getClass().getResourceAsStream("/fire.png"));
  private Image plantImage =  new Image(getClass().getResourceAsStream("/plant.png"));
  private Image borderImage;

  private ConfigMap mapConfig;
  private ConfigPlant plantConfig;
  private ConfigAnimal animalConfig;
  Image snailBack = new Image(getClass().getResourceAsStream("/snail_back.png"));
  Image snailFront = new Image(getClass().getResourceAsStream("/snail_front.png"));
  Image snailSide = new Image(getClass().getResourceAsStream("/snail_side.png"));
  private final Label[] ANIMAL_STATS_LABELS = new Label[8];
  private final static int amountOfGrassImages = 7;
  private Image[] verdantImages;
  private final static int amountOfVerdantImages = 5;
  private boolean isGrassGridInitialized = false;
  private AnimalStatisticsUpdater animalStatisticsUpdater;

  private BorderRenderer borderRenderer;

  @FXML
  public void initialize() {
    gridManager = new GridManager(gridPane, grassGridPane);
    gridRenderer = new GridRenderer(gridPane, gridManager);
    chartManager = new StatisticsChartManager(statisticsChart);
    ANIMAL_STATS_LABELS[0] = dayCount;
    ANIMAL_STATS_LABELS[1] = plantCount;
    ANIMAL_STATS_LABELS[2] = energy;
    ANIMAL_STATS_LABELS[3] = children;
    ANIMAL_STATS_LABELS[4] = descendants;
    ANIMAL_STATS_LABELS[5] = genome;
    ANIMAL_STATS_LABELS[6] = activeGene;
    ANIMAL_STATS_LABELS[7] = dayOfDeath;
    grassImages = new Image[amountOfGrassImages];
    for (int i = 0; i < amountOfGrassImages; i++) {
      grassImages[i] = new Image(getClass().getResourceAsStream("/grasses/grass" + (i + 1) + ".png"));
    }
    verdantImages = new Image[amountOfVerdantImages];
    for (int i = 0; i < amountOfVerdantImages; i++) {
      verdantImages[i] = new Image(getClass().getResourceAsStream("/verdant/verdant" + (i + 1) + ".png"));
    }
    isGrassGridInitialized = false;

    // Load border image and initialize renderer
    borderImage = new Image(getClass().getResourceAsStream("/border.png"));
    borderRenderer = new BorderRenderer(gridManager, borderImage, grassGridPane);
    animalStatisticsUpdater = new AnimalStatisticsUpdater(ANIMAL_STATS_LABELS, animalTitle, dataCollector);
  }

  private void initializeGrassGrid() {
    Random random = new Random();
    int rows = gridManager.getGridPaneSize().getY();
    int cols = gridManager.getGridPaneSize().getX();

    grassGridPane.getChildren().clear();

    for (int i = 2; i < rows+1; i++) {
      for (int j = 2; j < cols+1; j++) {
        ImageView imageView = new ImageView();
        double cellSize = gridManager.calculateCellSize();

        Vector2d position = new Vector2d(j - 1, i - 1);
        if (simulationData != null && simulationData.verdantFieldPositionSet().contains(position)) {
          imageView.setImage(verdantImages[random.nextInt(amountOfVerdantImages)]);
        } else {
          imageView.setImage(grassImages[random.nextInt(amountOfGrassImages)]);
        }

        imageView.setFitWidth(cellSize);
        imageView.setFitHeight(cellSize);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);

        grassGridPane.add(imageView, j, i);
      }
    }
  }

  public void setSimulation(Simulation simulation) {
    this.simulation = simulation;
  }

  private void drawMap() {
    gridManager.clearGrid();
    if (simulationData == null) {
      throw new DatacollectorNotInitialized();
    }
    Vector2d offset = gridManager.getGridPaneOffset();
    Vector2d size = gridManager.getGridPaneSize();

    // Draw border using dedicated renderer
    borderRenderer.render(offset, size);

    // Draw plants and fire using images
    drawElements(simulationData.plantPositionSet(), plantImage, offset, size);
    drawElements(simulationData.firePositionSet(), fireImage, offset, size);

    // Draw animals
    drawAnimalElements(simulationData.animalPositionSet(), offset, size);
  }

  private Animal selectAnimal(Animal animal) {
    if (animal != null) {
      return animal;
    }

    if (simulationStatistics == null) {
      return null;
    }

    List<Genotype> dominantGenotypes = simulationStatistics.mostPopularGenotypes();
    if (dominantGenotypes.isEmpty()) {
      return null;
    }

    for (Vector2d position : simulationData.animalPositionSet()) {
      List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
      if (animals.isEmpty()) continue;
      for (Animal a : animals) {
        if (a.getGenotype().equals(dominantGenotypes.get(0))) {
          return a;
        }
      }
    }
    return null;
  }

  @FXML
  private void onNewSimulationButtonClicked() {
    try {
      StageUtil.openNewStage(PARAMETERS, SIMULATION_PARAMETERS, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void updateAnimalStatisticsDisplay(Animal animal) {
    animalStatisticsUpdater.updateAnimalStatisticsDisplay(animal);
  }

  private void updateAnimalStatistics(Animal animal) {
    if (dataCollector != null && simulation != null) {
      Animal selectedAnimal = selectAnimal(animal);
      updateAnimalStatisticsDisplay(selectedAnimal);
    }
  }

  private void drawElements(Iterable<Vector2d> positions, Image image, Vector2d offset, Vector2d size) {
    for (Vector2d position : positions) {
      int x = position.getX() - offset.getX() + 1;
      int y = position.getY() - offset.getY() + 1;

      StackPane stackPane = new StackPane();
      stackPane.setSnapToPixel(true);

      Rectangle cell = new Rectangle();
      cell.setWidth(gridManager.calculateCellSize());
      cell.setHeight(gridManager.calculateCellSize());
      cell.setFill(new Color(0, 0, 0, 0));

      if (image != null) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(gridManager.calculateCellSize());
        imageView.setFitHeight(gridManager.calculateCellSize());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        stackPane.getChildren().addAll(cell, imageView);
      } else {
        cell.setFill(Color.GRAY);
        stackPane.getChildren().add(cell);
      }

      gridPane.add(stackPane, x, y);
    }
  }

  public void initializeSimulation(Simulation simulation,
                                   ConfigMap mapConfig,
                                   ConfigAnimal animalConfig,
                                   ConfigPlant plantConfig) {
    this.mapConfig = mapConfig;
    this.animalConfig = animalConfig;
    this.plantConfig = plantConfig;
    this.simulation = simulation;
  }

  private void drawAnimalElements(Iterable<Vector2d> positions, Vector2d offset, Vector2d size) {
    if (snailBack.isError() || snailFront.isError() || snailSide.isError()) {
      System.err.println("Failed to load snail images.");
      return;
    }
    double cellSize = gridManager.calculateCellSize();

    for (Vector2d position : positions) {
      int x = position.getX() - offset.getX() + 1;
      int y = (position.getY() - offset.getY()) + 1;

      List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
      Animal currentAnimal = getChosenAnimal(position, animals);

      if (currentAnimal == null) {
        continue;
      }

      StackPane stackPane = new StackPane();
      stackPane.setSnapToPixel(true);
      Rectangle cell = new Rectangle();
      cell.setWidth(cellSize);
      cell.setHeight(cellSize);
      cell.setFill(new Color(0, 0, 0, 0));

      String orientation = String.valueOf(currentAnimal.getOrientation());
      ImageView snailImageView = new ImageView();
      snailImageView.setFitWidth(cellSize);
      snailImageView.setFitHeight(cellSize);
      snailImageView.setPreserveRatio(true);
      snailImageView.setSmooth(false);

      double rotation = 0;

      switch (orientation) {
        case "North":
          snailImageView.setImage(snailBack);
          break;
        case "South":
          snailImageView.setImage(snailFront);
          break;
        case "East":
          snailImageView.setImage(snailSide);
          break;
        case "West":
          snailImageView.setImage(snailSide);
          snailImageView.setScaleX(-1);
          break;
        case "North-East":
          snailImageView.setImage(snailBack);
          rotation = 45;
          break;
        case "North-West":
          snailImageView.setImage(snailBack);
          rotation = -45;
          break;
        case "South-East":
          snailImageView.setImage(snailFront);
          rotation = 45;
          break;
        case "South-West":
          snailImageView.setImage(snailFront);
          rotation = -45;
          break;
        default:
          snailImageView.setImage(snailFront);
          break;
      }

      snailImageView.setRotate(rotation);

      stackPane.getChildren().addAll(cell, snailImageView);

      final Vector2d finalPosition = position;
      stackPane.setOnMouseClicked(event -> {
        chosenAnimal = getChosenAnimal(finalPosition, dataCollector.getAnimalsAtPosition(finalPosition));
        updateAnimalStatistics(chosenAnimal);
      });
      gridPane.setSnapToPixel(true);
      gridPane.add(stackPane, x, y);
    }
  }

  private Animal getChosenAnimal(Vector2d position, List<Animal> animalList) {
    if (animalList.isEmpty()) {
      return null;
    }
    // TO DO
    // Collections.sort(animalList);
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
          drawMap();
          if (!isGrassGridInitialized) {
            initializeGrassGrid();
            isGrassGridInitialized = true;
          }

          if (simulationStatistics != null) {
            updateSimulationStatistics(simulationStatistics);
            updateAnimalStatistics(chosenAnimal);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        latch.countDown();
      }
    });
  }

  private void updateSimulationStatistics(SimulationStatistics statistics) {
    freeFields.setText(String.valueOf(statistics.amountOfFreeFields()));
    List<Genotype> mostPopularGenotypes = statistics.mostPopularGenotypes();
    Label[] genotypeLabels = {genotype1, genotype2, genotype3};
    for (int i = 0; i < genotypeLabels.length; i++) {
      if (i < mostPopularGenotypes.size()) {
        genotypeLabels[i].setText(mostPopularGenotypes.get(i).toString());
      } else {
        genotypeLabels[i].setText(DASH);
      }
    }
    averageEnergy.setText(String.format("%.2f", statistics.averageEnergy()));
    averageLifespan.setText(String.valueOf(roundToTwoDecimal(statistics.averageLifespan())));
    averageChildren.setText(String.format("%.2f", statistics.averageChildren()));
    if (statisticsCSVSaver != null && mapConfig.saveToCsv()) {
      statisticsCSVSaver.saveStatistics(statistics);
    }
  }

  @FXML
  private void onSimulationStartClicked() {
    try {
      if (simulationStarted)
        return;
      simulation.addObserver(this);
      dataCollector = new SimulationDataCollector(simulation);
      SimulationEngine simulationEngine = new SimulationEngine(simulation);
      statisticsCSVSaver = new SimulationStatisticsCSVSaver();
      simulationEngine.runAsync();
      chosenAnimal = null;
      updateAnimalStatistics(null);
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