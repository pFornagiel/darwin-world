package agh.ics.oop.presenter;

import agh.ics.oop.presenter.util.AnimalColor;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.datacollectors.SimulationStatisticsCSVSaver;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationApp;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.model.worldmap.abstracts.AbstractWorldMap;
import agh.ics.oop.model.worldmap.abstracts.WorldMap;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.renderer.GridRenderer;
import agh.ics.oop.presenter.statistics.StatisticsChartManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
  private Simulation simulation;
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

  private static final Label[] ANIMAL_STATS_LABELS = new Label[8];

  @FXML
  public void initialize() {
    gridManager = new GridManager(gridPane);
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
  }
  public void drawMap() {
    gridManager.clearGrid();
    gridManager.updateGridConstraints();
    gridRenderer.drawAxes();
    if (dataCollector == null) {
      return;
    }
    SimulationData simulationData = dataCollector.getSimulationData();
    Vector2d offset = gridManager.getGridPaneOffset();
    Vector2d size = gridManager.getGridPaneSize();
    drawElements(simulationData.verdantFieldPositionSet(), Color.GRAY, offset, size);
    drawElements(simulationData.plantPositionSet(), Color.GREEN, offset, size);
    drawAnimalElements(simulationData.animalPositionSet(), Color.BLUE, offset, size);
    drawElements(simulationData.firePositionSet(), Color.RED, offset, size);
  }
  private Animal selectAnimal(Animal animal) {
    if (animal == null) {
      SimulationStatistics stats = dataCollector.getSimulationStatistics();
      List<Genotype> dominantGenotypes = stats.mostPopularGenotypes();
      if (!dominantGenotypes.isEmpty()) {
        for (Vector2d position : dataCollector.getSimulationData().animalPositionSet()) {
          if (dataCollector.getAnimalsAtPosition(position).isEmpty()) return chosenAnimal;
          List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
          for (Animal a : animals) {
            if (a.getGenotype().equals(dominantGenotypes.getFirst())) {
              return a;
            }
          }
        }
      }
    }
    return animal;
  }

  private void updateAnimalStatisticsDisplay(Animal animal) {
    if (animal != null) {
      var stats = dataCollector.getAnimalStatistics(animal);
      animalTitle.setText(String.format(ANIMAL_AT,
              stats.coordinates().getX(),
              stats.coordinates().getY()));
      String[] values = {
              String.valueOf(stats.lifespan()),
              String.valueOf(stats.eatenPlantsCount()),
              String.valueOf(stats.energy()),
              String.valueOf(stats.childrenCount()),
              String.valueOf(stats.descendantCount()),
              stats.genotype().toString(),
              String.valueOf(stats.currentGene()),
              stats.dayOfDeath() == -1 ? ALIVE : String.valueOf(stats.dayOfDeath())
      };
      for (int i = 0; i < ANIMAL_STATS_LABELS.length; i++) {
        ANIMAL_STATS_LABELS[i].setText(values[i]);
      }
    } else {
      animalTitle.setText(NO_ANIMAL_SELECTED);
      for (Label label : ANIMAL_STATS_LABELS) {
        label.setText(DASH);
      }
    }
  }

  private void updateAnimalStatistics(Animal animal) {
    animal = selectAnimal(animal);
    updateAnimalStatisticsDisplay(animal);
  }

  private void drawElements(Iterable<Vector2d> positions, Color color, Vector2d offset, Vector2d size) {
    for (Vector2d position : positions) {
      gridRenderer.setGridCell(
              position.getX() - offset.getX() + 1,
              size.getY() - 1 - (position.getY() - offset.getY()),
              color
      );
    }
  }

  private void drawAnimalElements(Iterable<Vector2d> positions, Color color, Vector2d offset, Vector2d size) {
    List<Vector2d> positionsCopy = new ArrayList<>();
    positions.forEach(positionsCopy::add);

    for (Vector2d position : positionsCopy) {
      int x = position.getX() - offset.getX() + 1;
      int y = size.getY() - 1 - (position.getY() - offset.getY());
      Animal currentAnimal = getChosenAnimal(position);

      if (currentAnimal == null) {
        continue;
      }

      color = getAnimalColor(dataCollector.getAnimalStatistics(currentAnimal), dataCollector.getSimulationStatistics(), simulation.getAnimalEnergy());
      Rectangle cell = new Rectangle();
      cell.setWidth(gridManager.calculateCellSize());
      cell.setHeight(gridManager.calculateCellSize());

      cell.setFill(color);
      cell.setOnMouseClicked(event -> {
        chosenAnimal = getChosenAnimal(position);
        updateAnimalStatistics(chosenAnimal);
      });

      gridPane.add(cell, x, y);
    }
  }


  private Animal getChosenAnimal(Vector2d position) {
    List<Animal> animalList = dataCollector.getAnimalsAtPosition(position);
    if (animalList.isEmpty()) {
      return null;
    }
    List<Animal> mutableList = new ArrayList<>(animalList);
    Collections.sort(mutableList);
    return mutableList.getFirst();
  }

  @Override
  public void mapChanged(WorldMap worldMap) {
    Platform.runLater(() -> {
      if (dataCollector != null) {
        chartManager.updateChart(dataCollector.getSimulationStatistics());
        gridManager.setGridDimensions(dataCollector.getWorldMap());
        drawMap();
        SimulationStatistics stats = dataCollector.getSimulationStatistics();
        if (stats != null) {
          updateSimulationStatistics(stats);
          updateAnimalStatistics(chosenAnimal);
        }
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
    if (statisticsCSVSaver != null) {
      statisticsCSVSaver.saveStatistics(statistics);
    }
  }


  @FXML
  private void onSimulationStartClicked() {
    try {
      simulation = SimulationApp.createSimulation();
      simulation.addObserver(this);
      dataCollector = new SimulationDataCollector(simulation);
      SimulationEngine simulationEngine = new SimulationEngine(simulation);
      statisticsCSVSaver = new SimulationStatisticsCSVSaver();
      simulationEngine.runAsync();
      chosenAnimal = null;
      updateAnimalStatistics(null);
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