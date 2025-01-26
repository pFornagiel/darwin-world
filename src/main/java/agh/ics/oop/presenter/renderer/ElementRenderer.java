package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
            int x = calculatePosition(position.getX(), offset.getX());
            int y = calculatePosition(position.getY(), offset.getY());
            StackPane stackPane = createCell(image);
            addToGrid(stackPane, x, y);
        }
    }

    public void drawColoredElements(Iterable<Vector2d> positions, Color color, Vector2d offset) {
        for (Vector2d position : positions) {
            int x = calculatePosition(position.getX(), offset.getX());
            int y = calculatePosition(position.getY(), offset.getY());
            Pane cell = createColoredCell(color);
            addToGrid(cell, x, y);
        }
    }

    private int calculatePosition(int position, int offset) {
        return position - offset + 1;
    }

    private void addToGrid(Pane pane, int x, int y) {
        gridPane.add(pane, x, y);
        GridPane.setHalignment(pane, HPos.CENTER);
        GridPane.setValignment(pane, VPos.CENTER);
    }

    private StackPane createCell(Image image) {
        StackPane stackPane = new StackPane();
        stackPane.setSnapToPixel(true);

        Rectangle cell = createRectangle();
        if (image != null) {
            ImageView imageView = createImageView(image);
            stackPane.getChildren().addAll(cell, imageView);
        } else {
            cell.setFill(Color.GRAY);
            stackPane.getChildren().add(cell);
        }

        return stackPane;
    }

    private Rectangle createRectangle() {
        double cellSize = gridManager.calculateCellSize();
        Rectangle cell = new Rectangle(cellSize, cellSize);
        cell.setFill(new Color(0, 0, 0, 0));
        return cell;
    }

    private ImageView createImageView(Image image) {
        double cellSize = gridManager.calculateCellSize();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(cellSize);
        imageView.setFitHeight(cellSize);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        return imageView;
    }

    private Pane createColoredCell(Color color) {
        double cellSize = gridManager.calculateCellSize();
        Pane cell = new Pane();
        cell.setMinSize(cellSize, cellSize);
        cell.setPrefSize(cellSize, cellSize);
        cell.setMaxSize(cellSize, cellSize);

        BackgroundFill backgroundFill = new BackgroundFill(color, null, null);
        Background background = new Background(backgroundFill);
        cell.setBackground(background);

        return cell;
    }
}