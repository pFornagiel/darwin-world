package agh.ics.oop.presenter.grid;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.abstracts.SimulatableMap;
import agh.ics.oop.model.worldmap.util.Boundary;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GridManager {
    private static final int MIN_CELL_SIZE = 1;
    private Vector2d gridPaneSize;
    private Vector2d staticGridPaneSize;
    private Vector2d gridPaneOffset;
    private final GridPane dynamicGridPane;
    private final GridPane staticGridPane;

    public GridManager(GridPane dynamicGridPane, GridPane staticGridPane) {
        this.dynamicGridPane = dynamicGridPane;
        this.staticGridPane = staticGridPane;
        this.gridPaneSize = new Vector2d(8, 8);
        this.staticGridPaneSize = new Vector2d(10, 10);
        this.gridPaneOffset = new Vector2d(0, 0);
    }

    public void setGridDimensions(SimulatableMap<Animal> worldMap) {
        Boundary mapBounds = worldMap.getBoundaries();
        int mapWidth = mapBounds.upperBoundary().getX() - mapBounds.lowerBoundary().getX() + 1;
        int mapHeight = mapBounds.upperBoundary().getY() - mapBounds.lowerBoundary().getY() + 1;

        gridPaneSize = new Vector2d(mapWidth, mapHeight);
        staticGridPaneSize = new Vector2d(mapWidth + 2, mapHeight + 2);

        gridPaneOffset = new Vector2d(
                mapBounds.lowerBoundary().getX(),
                mapBounds.lowerBoundary().getY()
        );

        setGridConstraints();
        alignGrids();
    }

    public double calculateCellSize() {
        double availableWidth = dynamicGridPane.getWidth();
        double availableHeight = dynamicGridPane.getHeight();
        if (availableWidth <= 0 || availableHeight <= 0) {
            availableWidth = 800;
            availableHeight = 600;
        }
        int maxDimension = Math.max(gridPaneSize.getX(), gridPaneSize.getY());
        double cellSizeByWidth = (availableWidth) / maxDimension;
        double cellSizeByHeight = (availableHeight) / maxDimension;
        return Math.max(MIN_CELL_SIZE, Math.min(cellSizeByWidth, cellSizeByHeight));
    }

    public void setGridConstraints() {
        double cellSize = calculateCellSize();
        dynamicGridPane.getColumnConstraints().clear();
        dynamicGridPane.getRowConstraints().clear();
        for (int i = 0; i < gridPaneSize.getY(); i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(cellSize);
            rowConstraints.setPrefHeight(cellSize);
            rowConstraints.setMaxHeight(cellSize);
            dynamicGridPane.getRowConstraints().add(rowConstraints);
        }
        for (int j = 0; j < gridPaneSize.getX(); j++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(cellSize);
            columnConstraints.setPrefWidth(cellSize);
            columnConstraints.setMaxWidth(cellSize);
            dynamicGridPane.getColumnConstraints().add(columnConstraints);
        }

        staticGridPane.getColumnConstraints().clear();
        staticGridPane.getRowConstraints().clear();
        for (int i = 0; i < staticGridPaneSize.getY(); i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(cellSize);
            rowConstraints.setPrefHeight(cellSize);
            rowConstraints.setMaxHeight(cellSize);
            staticGridPane.getRowConstraints().add(rowConstraints);
        }
        for (int j = 0; j < staticGridPaneSize.getX(); j++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(cellSize);
            columnConstraints.setPrefWidth(cellSize);
            columnConstraints.setMaxWidth(cellSize);
            staticGridPane.getColumnConstraints().add(columnConstraints);
        }
    }

    public void alignGrids() {
        GridPane.setRowIndex(staticGridPane, 0);
        GridPane.setColumnIndex(staticGridPane, 0);
        GridPane.setRowIndex(dynamicGridPane, 0);
        GridPane.setColumnIndex(dynamicGridPane, 0);

        GridPane.setFillWidth(staticGridPane, true);
        GridPane.setFillHeight(staticGridPane, true);
        GridPane.setFillWidth(dynamicGridPane, true);
        GridPane.setFillHeight(dynamicGridPane, true);
    }

    public void clearGrid() {
        dynamicGridPane.getChildren().clear();
    }

    public Vector2d getGridPaneSize() {
        return gridPaneSize;
    }

    public Vector2d getStaticGridPaneSize() {
        return staticGridPaneSize;
    }
    public GridPane getGridPane() {
        return dynamicGridPane;
    }

    public Vector2d getGridPaneOffset() {
        return gridPaneOffset;
    }
}