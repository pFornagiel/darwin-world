package agh.ics.oop.presenter;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationApp;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Animal;
import agh.ics.oop.model.worldelement.Direction;
import agh.ics.oop.model.worldmap.Boundary;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.model.worldmap.WorldMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class SimulationPresenter implements MapChangeListener {
  @FXML private TextField mapWidth;
  @FXML private TextField mapHeight;
  @FXML private TextField plantCount;
  @FXML private TextField plantEnergy;
  @FXML private TextField animalCount;
  @FXML private TextField animalEnergy;
  @FXML private TextField breedEnergyNeeded;
  @FXML private TextField breedEnergyUsage;
  @FXML private TextField minMutations;
  @FXML private TextField maxMutations;
  @FXML private TextField genesCount;
  @FXML private CheckBox fireMap;
  @FXML private CheckBox insanity;
  @FXML private TextField movesTextField;
  @FXML private Label moveDescriptionLabel;
  @FXML private GridPane gridPane;

  private Vector2d gridPaneSize = new Vector2d(8, 8);
  private Vector2d gridPaneOffset = new Vector2d(0, 0);
  private WorldMap worldMap;
  private ArrayList<Vector2d> positionList = new ArrayList<>();

  public SimulationPresenter() {
    Collections.addAll(
            positionList,
            new Vector2d(2, 2),
            new Vector2d(1, 1),
            new Vector2d(10, 10)
    );
  }

  @FXML
  private void initialize() {
    System.out.println("SimulationPresenter initialized");
  }

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
        Set<Animal> elementAtCoordinates = worldMap.objectsAt(new Vector2d(j + gridPaneOffset.getX(), i + gridPaneOffset.getY()));
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
    });
  }
  @FXML
  private void save() {
    SimulationConfig config = new SimulationConfig(
            mapWidth.getText(),
            mapHeight.getText(),
            plantCount.getText(),
            plantEnergy.getText(),
            animalCount.getText(),
            animalEnergy.getText(),
            breedEnergyNeeded.getText(),
            breedEnergyUsage.getText(),
            minMutations.getText(),
            maxMutations.getText(),
            genesCount.getText(),
            fireMap.isSelected(),
            insanity.isSelected()
    );

    SimulationConfigManager.saveConfig(config);
  }

  @FXML
  private void load() {
    SimulationConfig config = SimulationConfigManager.loadConfig();
    if (config != null) {
      mapWidth.setText(config.mapWidth);
      mapHeight.setText(config.mapHeight);
      plantCount.setText(config.plantCount);
      plantEnergy.setText(config.plantEnergy);
      animalCount.setText(config.animalCount);
      animalEnergy.setText(config.animalEnergy);
      breedEnergyNeeded.setText(config.breedEnergyNeeded);
      breedEnergyUsage.setText(config.breedEnergyUsage);
      minMutations.setText(config.minMutations);
      maxMutations.setText(config.maxMutations);
      genesCount.setText(config.genesCount);
      fireMap.setSelected(config.fireMap);
      insanity.setSelected(config.insanity);

      System.out.println("Configuration loaded successfully.");
    }
  }
  @FXML
  private void accept() {
    SimulationApp.switchScene("simulation.fxml");
    System.out.println("Accept button clicked.");
    System.out.println("Map Width: " + mapWidth.getText());
    System.out.println("Map Height: " + mapHeight.getText());
    System.out.println("Plant Count: " + plantCount.getText());
    System.out.println("Plant Energy: " + plantEnergy.getText());
    System.out.println("Animal Count: " + animalCount.getText());
    System.out.println("Animal Energy: " + animalEnergy.getText());
    System.out.println("Breed Energy Needed: " + breedEnergyNeeded.getText());
    System.out.println("Breed Energy Usage: " + breedEnergyUsage.getText());
    System.out.println("Min Mutations: " + minMutations.getText());
    System.out.println("Max Mutations: " + maxMutations.getText());
    System.out.println("Genes Count: " + genesCount.getText());
    System.out.println("Fire Map: " + (fireMap.isSelected() ? "ENABLED" : "DISABLED"));
    System.out.println("Insanity: " + (insanity.isSelected() ? "ENABLED" : "DISABLED"));
    SimulationApp.initMap();
  }

  @FXML
  private void mapWidth(ActionEvent event) {
    System.out.println("Map Width: " + mapWidth.getText());
  }

  @FXML
  private void mapHeight(ActionEvent event) {
    System.out.println("Map Height: " + mapHeight.getText());
  }

  @FXML
  private void plantCount(ActionEvent event) {
    System.out.println("Plant Count: " + plantCount.getText());
  }

  @FXML
  private void plantEnergy(ActionEvent event) {
    System.out.println("Plant Energy: " + plantEnergy.getText());
  }

  @FXML
  private void animalCount(ActionEvent event) {
    System.out.println("Animal Count: " + animalCount.getText());
  }

  @FXML
  private void animalEnergy(ActionEvent event) {
    System.out.println("Animal Energy: " + animalEnergy.getText());
  }

  @FXML
  private void breedEnergyNeeded(ActionEvent event) {
    System.out.println("Breed Energy Needed: " + breedEnergyNeeded.getText());
  }

  @FXML
  private void breedEnergyUsage(ActionEvent event) {
    System.out.println("Breed Energy Usage: " + breedEnergyUsage.getText());
  }

  @FXML
  private void minMutations(ActionEvent event) {
    System.out.println("Min Mutations: " + minMutations.getText());
  }

  @FXML
  private void maxMutations(ActionEvent event) {
    System.out.println("Max Mutations: " + maxMutations.getText());
  }

  @FXML
  private void genesCount(ActionEvent event) {
    System.out.println("Genes Count: " + genesCount.getText());
  }

  @FXML
  private void fireMap(ActionEvent event) {
    System.out.println("Fire Map checkbox toggled.");
  }

  @FXML
  private void insanity(ActionEvent event) {
    System.out.println("Insanity checkbox toggled.");
  }


  @FXML
  private void onSimulationStartClicked(ActionEvent actionEvent) {
    String[] arguments = movesTextField.getText().split(" ");

    ArrayList<Direction> directionList = null;
//    try {
//      directionList = OptionsParser.parse(arguments);
//    } catch (IllegalMoveArgumentException e) {
//      moveDescriptionLabel.setText("Invalid moves provided!");
//      return;
//    }

    moveDescriptionLabel.setText("");
    SimulationEngine simulationEngine = new SimulationEngine(new Simulation(positionList, directionList, worldMap));
    simulationEngine.runAsync();
  }
}
