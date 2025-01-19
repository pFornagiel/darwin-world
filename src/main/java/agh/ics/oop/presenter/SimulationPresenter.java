package agh.ics.oop.presenter;

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

public class SimulationPresenter implements MapChangeListener {
  @FXML
  private GridPane gridPane;

  private AbstractWorldMap worldMap;
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
  double ONE = 1.0;
  double HALF = 0.5;
  double QUARTER = 0.25;
  private SimulationStatisticsCSVSaver statisticsCSVSaver;

  @FXML
  public void initialize() {
    gridManager = new GridManager(gridPane);
    gridRenderer = new GridRenderer(gridPane, gridManager);
    chartManager = new StatisticsChartManager(statisticsChart);
  }

  public void setWorldMap(AbstractWorldMap worldMap) {
    this.worldMap = worldMap;
    worldMap.addObserver(this);
  }

  public void drawMap() {
    gridManager.clearGrid();
    gridManager.updateGridDimensions(worldMap);
    gridManager.updateGridConstraints();
    gridRenderer.drawAxes();

    if (dataCollector == null) {
      System.out.println("Simulation data collector is not initialized.");
      return;
    }

    SimulationData simulationData = dataCollector.getSimulationData();
    Vector2d offset = gridManager.getGridPaneOffset();
    Vector2d size = gridManager.getGridPaneSize();

    for (int x = 1; x < size.getX(); x++) {
      for (int y = 1; y < size.getY(); y++) {
        gridRenderer.setGridCell(x, y, Color.LIGHTGRAY);
      }
    }
    drawElements(simulationData.verdantFieldPositionSet(), Color.GRAY, offset, size);
    drawElements(simulationData.plantPositionSet(), Color.GREEN, offset, size);
    drawAnimalElements(simulationData.animalPositionSet(), Color.BLUE, offset, size);
    drawElements(simulationData.firePositionSet(), Color.RED, offset, size);
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
  private Color getAnimalColor(Animal animal) {
    SimulationStatistics stats = dataCollector.getSimulationStatistics();
    List<Genotype> mostPopularGenotypes = stats.mostPopularGenotypes();
    double energyRatio = Math.min((double) animal.getEnergy() / simulation.getAnimalEnergy(), ONE);
    double r = 0.0, g = 0.0, b = 0.0;
    if (!mostPopularGenotypes.isEmpty() && animal.getGenotype().equals(mostPopularGenotypes.get(0))) {
      r = energyRatio * HALF + HALF;
      b = energyRatio * HALF + HALF;
      g = energyRatio * QUARTER;
    } else {
      b = energyRatio;
    }
    return new Color(r, g, b, ONE);
  }

  private void drawAnimalElements(Iterable<Vector2d> positions, Color color, Vector2d offset, Vector2d size) {
    for (Vector2d position : positions) {
      int x = position.getX() - offset.getX() + 1;
      int y = size.getY() - 1 - (position.getY() - offset.getY());
      color = getAnimalColor(getChosenAnimal(position));
      Rectangle cell = new Rectangle();
      cell.setWidth(gridManager.calculateCellSize());
      cell.setHeight(gridManager.calculateCellSize());
      cell.setFill(color);
      cell.setOnMouseClicked(event -> {
        chosenAnimal = getChosenAnimal(position);
        System.out.println(dataCollector.getAnimalStatistics(chosenAnimal));
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
  public void mapChanged(WorldMap worldMap, String message) {
    Platform.runLater(() -> {
      if (dataCollector != null) {
        chartManager.updateChart(dataCollector.getSimulationStatistics());
        drawMap();
        SimulationStatistics stats = dataCollector.getSimulationStatistics();
        if (stats != null) {
          updateSimulationStatistics(stats);
        } else {
          System.out.println("Simulation statistics are unavailable.");
        }
      } else {
        System.out.println("Simulation data collector is not initialized.");
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
        genotypeLabels[i].setText("-");
      }
    }
    averageEnergy.setText(String.format("%.2f", statistics.averageEnergy()));
    averageLifespan.setText(String.valueOf(roundToTwoDecimal(statistics.averageLifespan())));
    averageChildren.setText(String.format("%.2f", statistics.averageChildren()));
    if (statisticsCSVSaver != null) {
      statisticsCSVSaver.saveStatistics(statistics);
    }
  }

  private double roundToTwoDecimal(double number){
    return Math.round(number / 100 ) * 100;
  }

  @FXML
  private void onSimulationStartClicked() {
    try {
      simulation = SimulationApp.createSimulation();
      dataCollector = new SimulationDataCollector(simulation);
      SimulationEngine simulationEngine = new SimulationEngine(simulation);
      statisticsCSVSaver = new SimulationStatisticsCSVSaver();
      simulationEngine.runAsync();
      System.out.println("Simulation started.");
    } catch (Exception e) {
      showError("Simulation Error", "Failed to start simulation: " + e.getMessage());
    }
  }

  @FXML
  private void onPauseButtonClicked() {
    if (simulation != null) {
      isPaused = !isPaused;
      simulation.togglePause();
      pauseButton.setText(isPaused ? "Resume" : "Pause");
      System.out.println(isPaused ? "Simulation paused." : "Simulation resumed.");
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
