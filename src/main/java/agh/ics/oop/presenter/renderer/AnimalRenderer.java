package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;

import java.util.List;

public class AnimalRenderer {

    private final GridManager gridManager;
    private final Image snailBack;
    private final Image snailFront;
    private final Image snailSide;
    private final SimulationDataCollector dataCollector;

    public AnimalRenderer(GridManager gridManager, Image snailBack, Image snailFront, Image snailSide, SimulationDataCollector dataCollector) {
        this.gridManager = gridManager;
        this.snailBack = snailBack;
        this.snailFront = snailFront;
        this.snailSide = snailSide;
        this.dataCollector = dataCollector;
    }

    public void drawAnimalElements(Iterable<Vector2d> positions, Vector2d offset, Vector2d size, GridPane gridPane) {
        if (snailBack.isError() || snailFront.isError() || snailSide.isError()) {
            System.err.println("Failed to load snail images.");
            return;
        }
        double cellSize = gridManager.calculateCellSize();

        for (Vector2d position : positions) {
            int x = position.getX() - offset.getX() + 1;
            int y = (position.getY() - offset.getY()) + 1;

            List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
            Animal currentAnimal = getChosenAnimal(position, animals);

            if (currentAnimal == null) {
                continue;
            }

            StackPane stackPane = new StackPane();
            stackPane.setSnapToPixel(true);
            Rectangle cell = new Rectangle();
            cell.setWidth(cellSize);
            cell.setHeight(cellSize);
            cell.setFill(new Color(0, 0, 0, 0));

            String orientation = String.valueOf(currentAnimal.getOrientation());
            ImageView snailImageView = new ImageView();
            snailImageView.setFitWidth(cellSize);
            snailImageView.setFitHeight(cellSize);
            snailImageView.setPreserveRatio(true);
            snailImageView.setSmooth(false);

            double rotation = 0;

            switch (orientation) {
                case "North":
                    snailImageView.setImage(snailBack);
                    break;
                case "South":
                    snailImageView.setImage(snailFront);
                    break;
                case "East":
                    snailImageView.setImage(snailSide);
                    break;
                case "West":
                    snailImageView.setImage(snailSide);
                    snailImageView.setScaleX(-1);
                    break;
                case "North-East":
                    snailImageView.setImage(snailBack);
                    rotation = 45;
                    break;
                case "North-West":
                    snailImageView.setImage(snailBack);
                    rotation = -45;
                    break;
                case "South-East":
                    snailImageView.setImage(snailFront);
                    rotation = 45;
                    break;
                case "South-West":
                    snailImageView.setImage(snailFront);
                    rotation = -45;
                    break;
                default:
                    snailImageView.setImage(snailFront);
                    break;
            }

            snailImageView.setRotate(rotation);

            stackPane.getChildren().addAll(cell, snailImageView);

            final Vector2d finalPosition = position;
            stackPane.setOnMouseClicked(event -> {
                Animal chosenAnimal = getChosenAnimal(finalPosition, dataCollector.getAnimalsAtPosition(finalPosition));
                // You can pass a callback or use an event bus to notify the presenter about the chosen animal
            });
            gridPane.setSnapToPixel(true);
            gridPane.add(stackPane, x, y);
        }
    }

    private Animal getChosenAnimal(Vector2d position, List<Animal> animalList) {
        if (animalList.isEmpty()) {
            return null;
        }
        return animalList.getFirst();
    }
}