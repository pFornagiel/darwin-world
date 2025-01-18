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
import agh.ics.oop.model.worldmap.util.Boundary;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SimulationPresenter implements MapChangeListener {
  @FXML
  private TextField movesTextField;
  @FXML
  private Label moveDescriptionLabel;
  @FXML
  private GridPane gridPane;

  private Vector2d gridPaneSize = new Vector2d(8, 8);
  private Vector2d gridPaneOffset = new Vector2d(0, 0);
  private WorldMap worldMap;
  private SimulationDataCollector dataCollector;
  private static final int MIN_CELL_SIZE = 1;

  private double calculateDynamicSize() {
    double availableWidth = gridPane.getWidth();
    double availableHeight = gridPane.getHeight();
    if (availableWidth <= 0 || availableHeight <= 0) {
      availableWidth = 800;
      availableHeight = 600;
    }
    int maxDimension = Math.max(gridPaneSize.getX(), gridPaneSize.getY());
    double cellSizeByWidth = (availableWidth * 0.9) / maxDimension;
    double cellSizeByHeight = (availableHeight * 0.9) / maxDimension;
    return Math.max(MIN_CELL_SIZE, Math.min(cellSizeByWidth, cellSizeByHeight));
  }

  private void setGridWidthAndHeight(WorldMap worldMapGeneric) {
    Boundary mapBounds = worldMapGeneric.getBoundaries();
    int mapWidth = mapBounds.upperBoundary().getX() - mapBounds.lowerBoundary().getX() + 1;
    int mapHeight = mapBounds.upperBoundary().getY() - mapBounds.lowerBoundary().getY() + 1;
    int offsetX = mapBounds.lowerBoundary().getX();
    int offsetY = mapBounds.lowerBoundary().getY();
    gridPaneSize = new Vector2d(mapWidth + 1, mapHeight + 1);
    gridPaneOffset = new Vector2d(offsetX, offsetY);
  }

  private void setGridCell(int xPosition, int yPosition, Color color) {
    double cellSize = calculateDynamicSize();
    Pane cell = new Pane();
    cell.setMinSize(cellSize, cellSize);
    cell.setPrefSize(cellSize, cellSize);
    cell.setMaxSize(cellSize, cellSize);
    BackgroundFill backgroundFill = new BackgroundFill(color, null, null);
    Background background = new Background(backgroundFill);
    cell.setBackground(background);
    BorderStroke borderStroke = new BorderStroke(
            Color.GRAY,
            BorderStrokeStyle.SOLID,
            null,
            new BorderWidths(0.5)
    );
    Border border = new Border(borderStroke);
    cell.setBorder(border);

    gridPane.add(cell, xPosition, yPosition);
    GridPane.setHalignment(cell, HPos.CENTER);
    GridPane.setValignment(cell, VPos.CENTER);
  }

  private void setAxisLabel(int xPosition, int yPosition, String text) {
    Label label = new Label(text);
    double cellSize = calculateDynamicSize();
    double fontSize = Math.max(8, Math.min(cellSize / 2, 12));
    label.setStyle(String.format("-fx-font-weight: bold; -fx-font-size: %.1fpx;", fontSize));

    label.setMaxWidth(cellSize);
    label.setMinWidth(cellSize);
    label.setWrapText(true);

    gridPane.add(label, xPosition, yPosition);
    GridPane.setHalignment(label, HPos.CENTER);
    GridPane.setValignment(label, VPos.CENTER);
  }

  private void drawAxes() {
    setAxisLabel(0, 0, "y/x");
    for (int i = 1; i < gridPaneSize.getY(); i++) {
      setAxisLabel(0, i, String.valueOf(gridPaneSize.getY() - 1 - i + gridPaneOffset.getY()));
    }
    for (int i = 1; i < gridPaneSize.getX(); i++) {
      setAxisLabel(i, 0, String.valueOf(i - 1 + gridPaneOffset.getX()));
    }
  }

  private void clearGrid() {
    gridPane.getChildren().clear();
    gridPane.getColumnConstraints().clear();
    gridPane.getRowConstraints().clear();
  }

  private void updateGridConstraints() {
    double cellSize = calculateDynamicSize();
    for (int i = 0; i < gridPaneSize.getY(); i++) {
      RowConstraints rowConstraints = new RowConstraints();
      rowConstraints.setMinHeight(cellSize);
      rowConstraints.setPrefHeight(cellSize);
      rowConstraints.setMaxHeight(cellSize);
      gridPane.getRowConstraints().add(rowConstraints);
    }

    for (int j = 0; j < gridPaneSize.getX(); j++) {
      ColumnConstraints columnConstraints = new ColumnConstraints();
      columnConstraints.setMinWidth(cellSize);
      columnConstraints.setPrefWidth(cellSize);
      columnConstraints.setMaxWidth(cellSize);
      gridPane.getColumnConstraints().add(columnConstraints);
    }
  }
  public void setWorldMap(AbstractWorldMap worldMap) {
    this.worldMap = worldMap;
    worldMap.addObserver(this);
  }
  public void drawMap() {
    clearGrid();
    setGridWidthAndHeight(worldMap);
    updateGridConstraints();
    drawAxes();
    if (dataCollector != null) {
      SimulationData simulationData = dataCollector.getSimulationData();
      for (int x = 1; x < gridPaneSize.getX(); x++) {
        for (int y = 1; y < gridPaneSize.getY(); y++) {
          setGridCell(x, y, Color.LIGHTGRAY);
        }
      }
      for (Vector2d position : simulationData.verdantFieldPositionSet()) {
        setGridCell(
                position.getX() - gridPaneOffset.getX() + 1,
                gridPaneSize.getY() - 1 - (position.getY() - gridPaneOffset.getY()),
                Color.GRAY
        );
      }
      for (Vector2d position : simulationData.plantPositionSet()) {
        setGridCell(
                position.getX() - gridPaneOffset.getX() + 1,
                gridPaneSize.getY() - 1 - (position.getY() - gridPaneOffset.getY()),
                Color.GREEN
        );
      }
      for (Vector2d position : simulationData.animalPositionSet()) {
        setGridCell(
                position.getX() - gridPaneOffset.getX() + 1,
                gridPaneSize.getY() - 1 - (position.getY() - gridPaneOffset.getY()),
                Color.BLUE
        );
      }
      for (Vector2d position : simulationData.firePositionSet()) {
        setGridCell(
                position.getX() - gridPaneOffset.getX() + 1,
                gridPaneSize.getY() - 1 - (position.getY() - gridPaneOffset.getY()),
                Color.RED
        );
      }
    } else {
      System.out.println("Simulation data collector is not initialized.");
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