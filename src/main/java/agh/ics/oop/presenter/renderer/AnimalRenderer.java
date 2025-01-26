package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class AnimalRenderer {
    private final GridManager gridManager;
    private final GridPane gridPane;
    private final SimulationDataCollector dataCollector;
    private final SimulationPresenter simulationPresenter;
    private final ImageLoader imageLoader;

    public AnimalRenderer(GridManager gridManager, GridPane gridPane,
                          SimulationDataCollector dataCollector,
                          SimulationPresenter simulationPresenter, ImageLoader imageLoader) {
        this.gridManager = gridManager;
        this.gridPane = gridPane;
        this.dataCollector = dataCollector;
        this.simulationPresenter = simulationPresenter;
        this.imageLoader = imageLoader;
    }

    public void drawAnimalElements(Iterable<Vector2d> positions, Vector2d offset) {
        double cellSize = gridManager.calculateCellSize();

        for (Vector2d position : positions) {
            int x = position.getX() - offset.getX() + 1;
            int y = (position.getY() - offset.getY()) + 1;

            List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
            Animal currentAnimal = getChosenAnimal(animals);

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
                    snailImageView.setImage(imageLoader.getSnailBack());
                    break;
                case "South":
                    snailImageView.setImage(imageLoader.getSnailFront());
                    break;
                case "East":
                    snailImageView.setImage(imageLoader.getSnailSide());
                    break;
                case "West":
                    snailImageView.setImage(imageLoader.getSnailSide());
                    snailImageView.setScaleX(-1);
                    break;
                case "North-East":
                    snailImageView.setImage(imageLoader.getSnailBack());
                    rotation = 45;
                    break;
                case "North-West":
                    snailImageView.setImage(imageLoader.getSnailBack());
                    rotation = -45;
                    break;
                case "South-East":
                    snailImageView.setImage(imageLoader.getSnailFront());
                    rotation = 45;
                    break;
                case "South-West":
                    snailImageView.setImage(imageLoader.getSnailFront());
                    rotation = -45;
                    break;
                default:
                    snailImageView.setImage(imageLoader.getSnailFront());
                    break;
            }

            snailImageView.setRotate(rotation);

            stackPane.getChildren().addAll(cell, snailImageView);

            stackPane.setOnMouseClicked(e -> simulationPresenter.onAnimalClicked(animals));

            gridPane.setSnapToPixel(true);
            gridPane.add(stackPane, x, y);
        }
    }

    private Animal getChosenAnimal(List<Animal> animalList) {
        if (animalList.isEmpty()) {
            return null;
        }
        return animalList.getFirst();
    }
}