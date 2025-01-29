package agh.ics.oop.presenter.grid;

import agh.ics.oop.model.util.DoubleVector2d;
import agh.ics.oop.model.util.Vector2d;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class GridManager {
  private final static double MAX_CANVAS_WIDTH = 800;
  private final static double MAX_CANVAS_HEIGHT = 500;
  private static final int MIN_CELL_SIZE = 1;
  private final static Vector2d GRID_OFFSET = new Vector2d(1, 1);

  private final Vector2d mapSize;
  private final Vector2d mapSizeWithOffset;
  private final int gridArea;
  private final Canvas simulationCanvas;
  private final Canvas staticCanvas;
  private final double cellSize;

  public GridManager(Canvas simulationCanvas, Canvas staticCanvas, Vector2d mapSize) {
    this.simulationCanvas = simulationCanvas;
    this.staticCanvas = staticCanvas;
    this.mapSize = mapSize;
    this.mapSizeWithOffset = mapSize.add(GRID_OFFSET.multiply(2));
    this.gridArea = mapSize.getX() * mapSize.getY();
    this.cellSize = calculateCellSize();

    initialiseCanvasDimensions();
  }

  public void setOnClickEventHandling(Consumer<Vector2d> onAnimalClicked) {
    this.simulationCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      int clickedX = (int) (event.getX() / cellSize) - GRID_OFFSET.getX();
      int clickedY = (int) (event.getY() / cellSize) - GRID_OFFSET.getY();
      Vector2d clickedPosition = new Vector2d(clickedX, clickedY);
      onAnimalClicked.accept(clickedPosition);
    });
  }

  private double calculateCellSize() {
    double ratio = (double) mapSizeWithOffset.getX() / mapSizeWithOffset.getY();
    double availableWidth = MAX_CANVAS_WIDTH;
    double availableHeight = availableWidth / ratio;
    if (availableHeight > MAX_CANVAS_HEIGHT) {
      availableHeight = MAX_CANVAS_HEIGHT;
      availableWidth = ratio * availableHeight;
    }
    int maxDimension = Math.max(mapSizeWithOffset.getX(), mapSizeWithOffset.getY());
    double cellSizeByWidth = availableWidth / maxDimension;
    double cellSizeByHeight = availableHeight / maxDimension;
    return Math.max(MIN_CELL_SIZE, Math.max(cellSizeByWidth, cellSizeByHeight));
  }

  private void initialiseCanvasDimensions() {
    double canvasWidth = cellSize * mapSizeWithOffset.getX();
    double canvasHeight = cellSize * mapSizeWithOffset.getY();

    double windowWidth = simulationCanvas.getScene().getWindow().getWidth();

    double translateX = (windowWidth - canvasWidth) / 2;

    simulationCanvas.setWidth(canvasWidth);
    simulationCanvas.setHeight(canvasHeight);
    staticCanvas.setWidth(canvasWidth);
    staticCanvas.setHeight(canvasHeight);

    simulationCanvas.setTranslateX(translateX);
    staticCanvas.setTranslateX(translateX);
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