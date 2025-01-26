package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

import java.util.List;

public class MapRenderer {

    private final GridManager gridManager;
    private final GridPane gridPane;
    private Image fireImage = new Image(getClass().getResourceAsStream("/fire.png"));
    private Image plantImage = new Image(getClass().getResourceAsStream("/plant.png"));
    private Image snailBack = new Image(getClass().getResourceAsStream("/snail_back.png"));
    private Image snailFront = new Image(getClass().getResourceAsStream("/snail_front.png"));
    private Image snailSide = new Image(getClass().getResourceAsStream("/snail_side.png"));
    private final SimulationDataCollector dataCollector;
    private final SimulationPresenter simulationPresenter; // Add reference to SimulationPresenter
    private BorderRenderer borderRenderer;
    public MapRenderer(GridManager gridManager, GridPane gridPane,
                        SimulationDataCollector dataCollector,
                       SimulationPresenter simulationPresenter, BorderRenderer borderRenderer) { // Add SimulationPresenter parameter
        this.gridManager = gridManager;
        this.gridPane = gridPane;
        this.dataCollector = dataCollector;
        this.simulationPresenter = simulationPresenter;
        this.borderRenderer = borderRenderer;
    }

    public void drawMap(SimulationData simulationData) {
        gridManager.clearGrid();
        Vector2d offset = gridManager.getGridPaneOffset();
        Vector2d size = gridManager.getGridPaneSize();
        drawElements(simulationData.plantPositionSet(), plantImage, offset, size);
        drawElements(simulationData.firePositionSet(), fireImage, offset, size);
        drawAnimalElements(simulationData.animalPositionSet(), offset, size);
        borderRenderer.render(offset, size);
    }

    private void drawElements(Iterable<Vector2d> positions, Image image, Vector2d offset, Vector2d size) {
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

    private void drawAnimalElements(Iterable<Vector2d> positions, Vector2d offset, Vector2d size) {
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
                // Directly call the method in SimulationPresenter
                simulationPresenter.onAnimalClicked(finalPosition, animals);
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