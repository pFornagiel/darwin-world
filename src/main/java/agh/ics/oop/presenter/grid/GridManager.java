package agh.ics.oop.presenter.grid;

import agh.ics.oop.model.util.DoubleVector2d;
import agh.ics.oop.model.util.Vector2d;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class GridManager {
  private static final int MIN_CELL_SIZE = 1;
  private final static Vector2d GRID_OFFSET = new Vector2d(1, 1);

  private final Vector2d mapSize;
  private final Vector2d mapSizeWithOffset;
  private final int gridArea;
  private final Canvas canvas;
  private final double cellSize;


  public GridManager(Canvas canvas, Vector2d mapSize) {
    this.canvas = canvas;
    this.mapSize = mapSize;
    this.mapSizeWithOffset = mapSize.add(GRID_OFFSET.multiply(2));
    this.gridArea = mapSize.getX() * mapSize.getY();
    this.cellSize = calculateCellSize();
  }

  public void setOnClickEventHandling(Consumer<Vector2d> onAnimalClicked) {
    this.canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      int clickedX = (int) (event.getX() / cellSize) - GRID_OFFSET.getX();
      int clickedY = (int) (event.getY() / cellSize) - GRID_OFFSET.getY();
      Vector2d clickedPosition = new Vector2d(clickedX, clickedY);
      onAnimalClicked.accept(clickedPosition);
    });
  }

  private double calculateCellSize() {
    double availableWidth = canvas.getWidth();
    double availableHeight = canvas.getHeight();
    if (availableWidth <= 0 || availableHeight <= 0) {
      availableWidth = 800;
      availableHeight = 600;
    }
    int maxDimension = Math.max(mapSizeWithOffset.getX(), mapSizeWithOffset.getY());
    double cellSizeByWidth = availableWidth / maxDimension;
    double cellSizeByHeight = availableHeight / maxDimension;
    return Math.max(MIN_CELL_SIZE, Math.min(cellSizeByWidth, cellSizeByHeight));
  }

  public DoubleVector2d mapToCanvasPosition(Vector2d mapPosition) {
    double canvasX = (mapPosition.getX() + GRID_OFFSET.getX()) * cellSize;
    double canvasY = (mapPosition.getY() + GRID_OFFSET.getY()) * cellSize;
    return new DoubleVector2d(canvasX, canvasY);
  }

  public double getCellSize() {
    return cellSize;
  }

  public Vector2d getGridSize() {
    return mapSize;
  }

  public Vector2d getGridPaneOffset() {
    return GRID_OFFSET;
  }

  public int getGridArea() {
    return gridArea;
  }
}