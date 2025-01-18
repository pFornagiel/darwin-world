package agh.ics.oop.presenter;

import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationApp;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.model.worldmap.abstracts.AbstractWorldMap;
import agh.ics.oop.model.worldmap.abstracts.WorldMap;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.renderer.GridRenderer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SimulationPresenter implements MapChangeListener {
  @FXML
  private TextField movesTextField;
  @FXML
  private Label moveDescriptionLabel;
  @FXML
  private GridPane gridPane;

  private WorldMap worldMap;
  private SimulationDataCollector dataCollector;
  private GridManager gridManager;
  private GridRenderer gridRenderer;

  @FXML
  public void initialize() {
    gridManager = new GridManager(gridPane);
    gridRenderer = new GridRenderer(gridPane, gridManager);
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

    // Draw base terrain
    for (int x = 1; x < size.getX(); x++) {
      for (int y = 1; y < size.getY(); y++) {
        gridRenderer.setGridCell(x, y, Color.LIGHTGRAY);
      }
    }

    // Draw simulation elements
    drawElements(simulationData.verdantFieldPositionSet(), Color.GRAY, offset, size);
    drawElements(simulationData.plantPositionSet(), Color.GREEN, offset, size);
    drawElements(simulationData.animalPositionSet(), Color.BLUE, offset, size);
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

  @Override
  public void mapChanged(WorldMap worldMap, String message) {
    Platform.runLater(() -> {
      moveDescriptionLabel.setText(message);
      if (dataCollector != null) {
        drawMap();
      } else {
        System.out.println("Simulation data collector is not initialized.");
      }
    });
  }

  @FXML
  private void onSimulationStartClicked() {
    try {
      Simulation simulation = SimulationApp.createSimulation();
      dataCollector = new SimulationDataCollector(simulation);
      SimulationEngine simulationEngine = new SimulationEngine(simulation);
      simulationEngine.runAsync();
      System.out.println("Simulation started.");
    } catch (Exception e) {
      showError("Simulation Error", "Failed to start simulation: " + e.getMessage());
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
