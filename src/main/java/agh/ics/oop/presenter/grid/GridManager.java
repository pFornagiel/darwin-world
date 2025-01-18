package agh.ics.oop.presenter.grid;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldmap.abstracts.WorldMap;
import agh.ics.oop.model.worldmap.util.Boundary;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GridManager {
    private static final int MIN_CELL_SIZE = 1;
    private Vector2d gridPaneSize;
    private Vector2d gridPaneOffset;
    private final GridPane gridPane;

    public GridManager(GridPane gridPane) {
        this.gridPane = gridPane;
        this.gridPaneSize = new Vector2d(8, 8);
        this.gridPaneOffset = new Vector2d(0, 0);
    }

    public void updateGridDimensions(WorldMap worldMap) {
        Boundary mapBounds = worldMap.getBoundaries();
        int mapWidth = mapBounds.upperBoundary().getX() - mapBounds.lowerBoundary().getX() + 1;
        int mapHeight = mapBounds.upperBoundary().getY() - mapBounds.lowerBoundary().getY() + 1;
        gridPaneSize = new Vector2d(mapWidth + 1, mapHeight + 1);
        gridPaneOffset = new Vector2d(
                mapBounds.lowerBoundary().getX(),
                mapBounds.lowerBoundary().getY()
        );
    }

    public double calculateCellSize() {
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

    public void updateGridConstraints() {
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        double cellSize = calculateCellSize();

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

    public void clearGrid() {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
    }

    public Vector2d getGridPaneSize() {
        return gridPaneSize;
    }

    public Vector2d getGridPaneOffset() {
        return gridPaneOffset;
    }
}