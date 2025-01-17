// SimulationPresenter.java
package agh.ics.oop.presenter;

import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationApp;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.BaseAnimal;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.model.worldmap.abstracts.WorldMap;
import agh.ics.oop.model.worldmap.util.Boundary;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Set;

public class SimulationPresenter implements MapChangeListener {
  @FXML private TextField movesTextField;
  @FXML private Label moveDescriptionLabel;
  @FXML private GridPane gridPane;

  private Vector2d gridPaneSize = new Vector2d(8, 8);
  private Vector2d gridPaneOffset = new Vector2d(0, 0);
  private WorldMap worldMap;
  private SimulationDataCollector dataCollector;

  private void setGridWidthAndHeight(WorldMap worldMapGeneric) {
    Boundary mapBounds = worldMapGeneric.getBoundaries();
    int mapWidth = mapBounds.upperBoundary().getX() - mapBounds.lowerBoundary().getX() + 1;
    int mapHeight = mapBounds.upperBoundary().getY() - mapBounds.lowerBoundary().getY() + 1;
    int offsetX = mapBounds.lowerBoundary().getX();
    int offsetY = mapBounds.lowerBoundary().getY();
    gridPaneSize = new Vector2d(mapWidth, mapHeight);
    gridPaneOffset = new Vector2d(offsetX, offsetY);
  }

  private void clearGrid() {
    gridPane.getChildren().retainAll(gridPane.getChildren().getFirst());
    gridPane.getColumnConstraints().clear();
    gridPane.getRowConstraints().clear();
  }

  private void setGridCell(int xPosition, int yPosition, String labelText) {
    Label cellLabel = new Label();
    cellLabel.setText(labelText);
    gridPane.add(cellLabel, xPosition, yPosition);
    GridPane.setHalignment(cellLabel, HPos.CENTER);
    GridPane.setValignment(cellLabel, VPos.CENTER);
  }

  private void drawAxes() {
    setGridCell(0, 0, "x/y");
    for (int i = 1; i < gridPaneSize.getY(); i++) {
      setGridCell(0, i, Integer.toString(gridPaneSize.getY() - 1 - i - gridPaneOffset.getY()));
    }
    for (int j = 1; j < gridPaneSize.getX(); j++) {
      setGridCell(j, 0, Integer.toString(j + gridPaneOffset.getX()));
    }
  }

  private void updateGridConstraints() {
    RowConstraints rowConstraints = new RowConstraints();
    rowConstraints.setPercentHeight(100.0 / gridPaneSize.getY());
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setPercentWidth(100.0 / gridPaneSize.getX());

    for (int i = 0; i < gridPaneSize.getY(); i++) {
      gridPane.getRowConstraints().add(rowConstraints);
    }
    for (int j = 0; j < gridPaneSize.getX(); j++) {
      gridPane.getColumnConstraints().add(columnConstraints);
    }
  }

  public void setWorldMap(WorldMap worldMap) {
    this.worldMap = worldMap;
    setGridWidthAndHeight(worldMap);
    updateGridConstraints();
    drawAxes();
  }

  public void drawMap() {
    clearGrid();
    setGridWidthAndHeight(worldMap);
    drawAxes();
    updateGridConstraints();

    for (int i = 1; i < gridPaneSize.getY(); i++) {
      for (int j = 1; j < gridPaneSize.getX(); j++) {
        Set<BaseAnimal> elementAtCoordinates = worldMap.objectsAt(
                new Vector2d(j + gridPaneOffset.getX(), i + gridPaneOffset.getY())
        );
        int xPosition = j;
        int yPosition = gridPaneSize.getY() - i - 1;
        String objectRepresentation = elementAtCoordinates.size() != 0 ? "*" : " ";
        setGridCell(xPosition, yPosition, objectRepresentation);
      }
    }
  }

  @Override
  public void mapChanged(WorldMap worldMap, String message) {
    Platform.runLater(() -> {
      drawMap();
      moveDescriptionLabel.setText(message);
      if (dataCollector != null) {
        System.out.println(dataCollector.getSimulationData());
      } else {
        System.out.println("Simulation data collector is not initialized.");
      }
    });
  }

  @FXML
  private void onSimulationStartClicked(ActionEvent actionEvent) {
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