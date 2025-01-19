package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GridRenderer {
    private final GridPane gridPane;
    private final GridManager gridManager;
    private final static int MIN_FONT_SIZE = 1;

    public GridRenderer(GridPane gridPane, GridManager gridManager) {
        this.gridPane = gridPane;
        this.gridManager = gridManager;
    }

    public void setGridCell(int xPosition, int yPosition, Color color) {
        double cellSize = gridManager.calculateCellSize();
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

    public void drawAxes() {
        Vector2d gridPaneSize = gridManager.getGridPaneSize();
        Vector2d gridPaneOffset = gridManager.getGridPaneOffset();

        setAxisLabel(0, 0, "y/x");
        for (int i = 1; i < gridPaneSize.getY(); i++) {
            setAxisLabel(0, i, String.valueOf(gridPaneSize.getY() - 1 - i + gridPaneOffset.getY()));
        }
        for (int i = 1; i < gridPaneSize.getX(); i++) {
            setAxisLabel(i, 0, String.valueOf(i - 1 + gridPaneOffset.getX()));
        }
    }

    private void setAxisLabel(int xPosition, int yPosition, String text) {
        Label label = new Label(text);
        double cellSize = gridManager.calculateCellSize();
        double fontSize = Math.max(cellSize / 2, MIN_FONT_SIZE);
        label.setStyle(String.format("-fx-font-weight: bold; -fx-font-size: %.1fpx;", fontSize));

        label.setMaxWidth(cellSize);
        label.setMinWidth(cellSize);
        label.setWrapText(true);

        gridPane.add(label, xPosition, yPosition);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
    }
}