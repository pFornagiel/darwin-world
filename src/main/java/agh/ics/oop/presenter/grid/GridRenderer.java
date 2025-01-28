package agh.ics.oop.presenter.grid;

import agh.ics.oop.model.util.DoubleVector2d;
import agh.ics.oop.model.util.Vector2d;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GridRenderer {
  protected final GridManager gridManager;
  protected final GraphicsContext gc;
  protected final Vector2d gridOffset;

  public GridRenderer(Canvas simulationCanvas, GridManager gridManager) {
    this.gc = simulationCanvas.getGraphicsContext2D();
    this.gridManager = gridManager;
    this.gridOffset = gridManager.getGridPaneOffset();
  }

  public void clearCanvas() {
    gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
  }

  public void drawElement(Image image, Vector2d position) {
    DoubleVector2d canvasPos = gridManager.mapToCanvasPosition(position);

    gc.drawImage(
        image,
        canvasPos.getX(),
        canvasPos.getY(),
        gridManager.getCellSize(),
        gridManager.getCellSize()
    );
  }

  public void drawElement(Image image, Vector2d position, double scale) {
    DoubleVector2d canvasPos = gridManager.mapToCanvasPosition(position);
    double offset = scale != 1 ? scale * gridManager.getCellSize() / 8 : 0;
    gc.drawImage(
        image,
        canvasPos.getX() - offset,
        canvasPos.getY() - offset,
        gridManager.getCellSize() * scale,
        gridManager.getCellSize() * scale
    );
  }

  public void drawColor(Color color, Vector2d position) {
    DoubleVector2d canvasPos = gridManager.mapToCanvasPosition(position);

    gc.setFill(color);
    gc.fillRect(
        canvasPos.getX(),
        canvasPos.getY(),
        gridManager.getCellSize(),
        gridManager.getCellSize()
    );
  }

  public void drawBorder(Vector2d position) {
    DoubleVector2d canvasPos = gridManager.mapToCanvasPosition(position);
    double cellSize = gridManager.getCellSize();

    Color originalStrokeColor = (Color) gc.getStroke();
    double originalStrokeWidth = gc.getLineWidth();

    gc.setStroke(Color.VIOLET);
    gc.setLineWidth(3);

    gc.strokeRect(
            canvasPos.getX(),
            canvasPos.getY(),
            cellSize,
            cellSize
    );

    gc.setStroke(originalStrokeColor);
    gc.setLineWidth(originalStrokeWidth);
  }

}