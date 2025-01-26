package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class ElementRenderer {
    private final GridManager gridManager;
    private final GridPane gridPane;

    public ElementRenderer(GridManager gridManager, GridPane gridPane) {
        this.gridManager = gridManager;
        this.gridPane = gridPane;
    }

    public void drawElements(Iterable<Vector2d> positions, Image image, Vector2d offset) {
        for (Vector2d position : positions) {
            int x = position.getX() - offset.getX() + 1;
            int y = position.getY() - offset.getY() + 1;

            StackPane stackPane = new StackPane();
            stackPane.setSnapToPixel(true);

            Rectangle cell = new Rectangle();
            cell.setWidth(gridManager.calculateCellSize());
            cell.setHeight(gridManager.calculateCellSize());
            cell.setFill(new Color(0, 0, 0, 0));

            if (image != null) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(gridManager.calculateCellSize());
                imageView.setFitHeight(gridManager.calculateCellSize());
                imageView.setPreserveRatio(true);
                imageView.setSmooth(false);
                stackPane.getChildren().addAll(cell, imageView);
            } else {
                cell.setFill(Color.GRAY);
                stackPane.getChildren().add(cell);
            }

            gridPane.add(stackPane, x, y);
        }
    }

    public void drawColoredElements(Iterable<Vector2d> positions, Color color, Vector2d offset) {
        for (Vector2d position : positions) {
            int x = position.getX() - offset.getX() + 1;
            int y = position.getY() - offset.getY() + 1;

            Rectangle cell = new Rectangle();
            cell.setWidth(gridManager.calculateCellSize());
            cell.setHeight(gridManager.calculateCellSize());
            cell.setFill(color);

            gridPane.add(cell, x, y);
        }
    }
}