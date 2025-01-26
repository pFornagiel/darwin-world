package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ElementRenderer {

    private final GridManager gridManager;
    private final GridPane gridPane;

    public ElementRenderer(GridManager gridManager, GridPane gridPane) {
        this.gridManager = gridManager;
        this.gridPane = gridPane;
    }

    public void drawElements(Iterable<Vector2d> positions, Image image, Vector2d offset) {
        double cellSize = gridManager.calculateCellSize();
        for (Vector2d position : positions) {
            int x = calculatePosition(position.getX(), offset.getX());
            int y = calculatePosition(position.getY(), offset.getY());
            StackPane stackPane = GridRenderer.createCell(cellSize, image);
            GridRenderer.addToGrid(gridPane, stackPane, x, y);
        }
    }

    public void drawColoredElements(Iterable<Vector2d> positions, Color color, Vector2d offset) {
        double cellSize = gridManager.calculateCellSize();
        for (Vector2d position : positions) {
            int x = calculatePosition(position.getX(), offset.getX());
            int y = calculatePosition(position.getY(), offset.getY());
            Pane cell = GridRenderer.createColoredCell(cellSize, color);
            GridRenderer.addToGrid(gridPane, cell, x, y);
        }
    }

    private int calculatePosition(int position, int offset) {
        return position - offset + 1;
    }
}