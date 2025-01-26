package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
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

    public BorderRenderer(GridManager gridManager, Image borderImage, GridPane grassGridPane) {
        this.gridManager = gridManager;
        this.borderImage = borderImage;
        this.grassGridPane = grassGridPane;
    }

    public void render(Vector2d offset, Vector2d size) {
        if (borderImage == null || borderImage.isError()) return;

        int mapWidth = size.getX();
        int mapHeight = size.getY();

        // Preserve original coordinate calculations
        for (int x = 1; x < mapWidth + 2; x++) {
            drawBorderCell(x, 1);
            drawBorderCell(x, mapHeight + 1);
        }

        for (int y = 2; y <= mapHeight; y++) {
            drawBorderCell(1, y);
            drawBorderCell(mapWidth + 1, y);
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

        ImageView imageView = new ImageView(borderImage);
        imageView.setFitWidth(cellSize);
        imageView.setFitHeight(cellSize);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);

        stackPane.getChildren().addAll(cell, imageView);
        grassGridPane.add(stackPane, x, y);
    }
}