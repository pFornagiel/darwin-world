package agh.ics.oop.presenter.grid;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridRenderer {

    public static void addToGrid(GridPane gridPane, Pane pane, int x, int y) {
        gridPane.add(pane, x, y);
        GridPane.setHalignment(pane, HPos.CENTER);
        GridPane.setValignment(pane, VPos.CENTER);
    }

    public static StackPane createCell(double cellSize, Image image) {
        StackPane stackPane = new StackPane();
        stackPane.setSnapToPixel(true);

        Rectangle cell = createRectangle(cellSize);
        if (image != null) {
            ImageView imageView = createImageView(image, cellSize);
            stackPane.getChildren().addAll(cell, imageView);
        } else {
            cell.setFill(Color.GRAY);
            stackPane.getChildren().add(cell);
        }

        return stackPane;
    }

    public static Rectangle createRectangle(double cellSize) {
        Rectangle cell = new Rectangle(cellSize, cellSize);
        cell.setFill(new Color(0, 0, 0, 0));
        return cell;
    }

    public static ImageView createImageView(Image image, double cellSize) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(cellSize);
        imageView.setFitHeight(cellSize);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        return imageView;
    }

    public static Pane createColoredCell(double cellSize, Color color) {
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