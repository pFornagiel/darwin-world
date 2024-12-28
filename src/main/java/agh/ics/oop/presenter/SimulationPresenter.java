package agh.ics.oop.presenter;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationApp;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.OptionsParser;
import agh.ics.oop.model.exception.IllegalMoveArgumentException;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.WorldElement;
import agh.ics.oop.model.worldmap.Boundary;
import agh.ics.oop.model.worldmap.MapChangeListener;
import agh.ics.oop.model.worldmap.WorldMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.Collections;

import static agh.ics.oop.model.simulation.SimulationApp.initMap;

public class SimulationPresenter implements MapChangeListener {
  private static final String EMPTY_CELL_REPRESENTATION = " ";
  private static final String INVALID_MOVE_PROVIDED = "Invalid moves provided. Valid moves are [f,b,l,r]";


  @FXML
  private TextField mapWidth;

  @FXML
  private TextField mapHeight;

  @FXML
  private Button accept;

  @FXML
  public TextField movesTextField;
  @FXML
  public Label moveDescriptionLabel;
  @FXML
  public Button startButton;
  @FXML
  public GridPane gridPane;
  private Vector2d gridPaneSize = new Vector2d(8,8);
  private Vector2d gridPaneOffset = new Vector2d(0,0);

  private WorldMap worldMap;
  ArrayList<Vector2d> positionList = new ArrayList<>();

  public SimulationPresenter(){
    Collections.addAll(
        positionList,
        new Vector2d(2,2),
        new Vector2d(1,1),
        new Vector2d(10,10)
    );
  }

  private void setGridWidthAndHeight(WorldMap worldMap){
    Boundary mapBounds = worldMap.getCurrentBounds();

    int mapWidth = mapBounds.upperBoundary().getX() - mapBounds.lowerBoundary().getX() + 1;
    int mapHeight = mapBounds.upperBoundary().getY() - mapBounds.lowerBoundary().getY() + 1;
    int offsetX = mapBounds.lowerBoundary().getX();
    int offsetY = mapBounds.lowerBoundary().getY();
    gridPaneSize = new Vector2d(mapWidth,mapHeight);
    gridPaneOffset = new Vector2d(offsetX,offsetY);
  }

  private void clearGrid() {
    gridPane.getChildren().retainAll(gridPane.getChildren().getFirst()); // hack to retain visible grid lines
    gridPane.getColumnConstraints().clear();
    gridPane.getRowConstraints().clear();
  }

  private void setGridCell(int xPosition, int yPosition, String labelText){
    Label cellLabel = new Label();
    cellLabel.setText(labelText);
    gridPane.add(cellLabel, xPosition, yPosition);
    GridPane.setHalignment(cellLabel, HPos.CENTER);
    GridPane.setValignment(cellLabel, VPos.CENTER);
  }

  private void drawAxes(){
    setGridCell(0,0,"x/y");
    for(int i = 1; i< gridPaneSize.getY(); i++){
      setGridCell(0,i,Integer.toString(gridPaneSize.getY() - 1- i  -gridPaneOffset.getY()));

    }
    for(int j = 1; j< gridPaneSize.getX(); j++){
      setGridCell(j,0,Integer.toString(j+gridPaneOffset.getX()));
    }
  }

  private void updateGridConstraints(){
    RowConstraints rowConstraints = new RowConstraints();
    rowConstraints.setPercentHeight(100.0/gridPaneSize.getY());
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setPercentWidth(100.0/gridPaneSize.getX());
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
  public void drawMap(){
    clearGrid();
    setGridWidthAndHeight(worldMap);
    drawAxes();
//    Set size of each cell using row and column constraints
    updateGridConstraints();

    for(int i = 1; i< gridPaneSize.getY(); i++){
      for(int j = 1; j< gridPaneSize.getX(); j++){
        WorldElement elementAtCoordinates = worldMap.objectAt(new Vector2d(j + gridPaneOffset.getX() ,i + gridPaneOffset.getY()));
        int xPosition = j;
        int yPosition = gridPaneSize.getY() - i-1;
        String objectRepresentation = elementAtCoordinates != null ? elementAtCoordinates.toString() : EMPTY_CELL_REPRESENTATION;
        setGridCell(xPosition,yPosition,objectRepresentation);
      }
    }
  }

  @Override
  public void mapChanged(WorldMap worldMap, String message) {
    Platform.runLater(()-> {
      drawMap();
      moveDescriptionLabel.setText(message);
    });
  }

  public void onSimulationStartClicked(ActionEvent actionEvent) {
    String[] arguments = movesTextField.getText().split(" ");

    ArrayList<MoveDirection> directionList = null;
    try{
      directionList = OptionsParser.parse(arguments);
    } catch (IllegalMoveArgumentException e) {
      moveDescriptionLabel.setText(INVALID_MOVE_PROVIDED);
      return;
    }

    worldMap.removeMainElementsFromWorld();
    moveDescriptionLabel.setText("");
    SimulationEngine simulationEngine = new SimulationEngine(new Simulation(positionList,directionList,worldMap));
    simulationEngine.runAsync();
  }


  @FXML
  private void mapWidth() {
  }

  @FXML
  private void mapHeight() {
  }

  @FXML
  private void accept() {
    SimulationApp.switchScene("simulation.fxml");
    SimulationApp.initMap();
  }

  @FXML
  private void initialize() {
  }
}
