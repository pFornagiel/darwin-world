package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.grid.GridRenderer;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;

public class BorderRenderer {
    private final GridManager gridManager;
    private final Image borderImage;
    private final GridPane grassGridPane;
    private final int MAX_MAP_SIZE_FOR_IMAGES;
    private static final Color BORDER_COLOR = new Color(0.6,0.3,0.1,1);
    public BorderRenderer(GridManager gridManager, Image borderImage, GridPane grassGridPane, int MAX_MAP_SIZE_FOR_IMAGES) {
        this.MAX_MAP_SIZE_FOR_IMAGES = MAX_MAP_SIZE_FOR_IMAGES;
        this.gridManager = gridManager;
        this.borderImage = borderImage;
        this.grassGridPane = grassGridPane;
    }

    public void render() {
        Vector2d size = gridManager.getGridPaneSize();
        int mapWidth = size.getX();
        int mapHeight = size.getY();
        int mapArea = mapWidth * mapHeight;

        if (mapArea <= MAX_MAP_SIZE_FOR_IMAGES)  {
            if (borderImage == null || borderImage.isError()) return;

            for (int x = 1; x < mapWidth + 2; x++) {
                drawBorderCell(x, 1);
                drawBorderCell(x, mapHeight + 1);
            }

            for (int y = 2; y <= mapHeight; y++) {
                drawBorderCell(1, y);
                drawBorderCell(mapWidth + 1, y);
            }
        }
    }

    private void drawBorderCell(int x, int y) {
        StackPane stackPane = new StackPane();
        stackPane.setSnapToPixel(true);

        Rectangle cell = new Rectangle();
        double cellSize = gridManager.calculateCellSize();
        cell.setWidth(cellSize);
        cell.setHeight(cellSize);
        cell.setFill(Color.TRANSPARENT);

        ImageView imageView = GridRenderer.createImageView(borderImage, cellSize);

        stackPane.getChildren().addAll(cell, imageView);
        grassGridPane.add(stackPane, x, y);
    }

    private void renderColoredBorders(int mapWidth, int mapHeight, Color color) {
        for (int x = 1; x < mapWidth + 2; x++) {
            drawColoredBorderCell(x, 1, color);
            drawColoredBorderCell(x, mapHeight + 1, color);
        }

        for (int y = 2; y <= mapHeight; y++) {
            drawColoredBorderCell(1, y, color);
            drawColoredBorderCell(mapWidth + 1, y, color);
        }
    }

    private void drawColoredBorderCell(int x, int y, Color color) {
        Rectangle cell = new Rectangle();
        double cellSize = gridManager.calculateCellSize();
        cell.setWidth(cellSize);
        cell.setHeight(cellSize);
        cell.setFill(color);

        grassGridPane.add(cell, x, y);
    }
}