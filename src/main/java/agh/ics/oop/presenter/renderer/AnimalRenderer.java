package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.AnimalData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.util.AnimalColor;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;

public class AnimalRenderer {
    private final GridManager gridManager;
    private final GridPane gridPane;
    private final SimulationDataCollector dataCollector;
    private final SimulationPresenter simulationPresenter;
    private final ImageLoader imageLoader;
    private final int MAX_MAP_SIZE_FOR_IMAGES;

    private static final String NORTH = "North";
    private static final String SOUTH = "South";
    private static final String EAST = "East";
    private static final String WEST = "West";
    private static final String NORTH_EAST = "North-East";
    private static final String NORTH_WEST = "North-West";
    private static final String SOUTH_EAST = "South-East";
    private static final String SOUTH_WEST = "South-West";
    private final int initialEnergy;
    public AnimalRenderer(GridManager gridManager, GridPane gridPane,
                          SimulationDataCollector dataCollector,
                          SimulationPresenter simulationPresenter, ImageLoader imageLoader, int MAX_MAP_SIZE_FOR_IMAGES, int initialEnergy) {
        this.gridManager = gridManager;
        this.gridPane = gridPane;
        this.dataCollector = dataCollector;
        this.simulationPresenter = simulationPresenter;
        this.imageLoader = imageLoader;
        this.MAX_MAP_SIZE_FOR_IMAGES = MAX_MAP_SIZE_FOR_IMAGES;
        this.initialEnergy = initialEnergy;
    }

    public void drawAnimalElements(Iterable<Vector2d> positions, Vector2d offset) {
        Vector2d mapSize = gridManager.getGridPaneSize();
        int mapArea = mapSize.getX() * mapSize.getY();

        if (mapArea > MAX_MAP_SIZE_FOR_IMAGES) {
            drawColoredAnimalElements(positions, offset);
        } else {
            drawAnimalImages(positions, offset);
        }
    }

    private void drawAnimalImages(Iterable<Vector2d> positions, Vector2d offset) {
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
                case NORTH:
                    snailImageView.setImage(imageLoader.getSnailBack());
                    break;
                case SOUTH:
                    snailImageView.setImage(imageLoader.getSnailFront());
                    break;
                case EAST:
                    snailImageView.setImage(imageLoader.getSnailSide());
                    break;
                case WEST:
                    snailImageView.setImage(imageLoader.getSnailSide());
                    snailImageView.setScaleX(-1);
                    break;
                case NORTH_EAST:
                    snailImageView.setImage(imageLoader.getSnailBack());
                    rotation = 45;
                    break;
                case NORTH_WEST:
                    snailImageView.setImage(imageLoader.getSnailBack());
                    rotation = -45;
                    break;
                case SOUTH_EAST:
                    snailImageView.setImage(imageLoader.getSnailFront());
                    rotation = 45;
                    break;
                case SOUTH_WEST:
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

    public void drawColoredAnimalElements(Iterable<Vector2d> positions, Vector2d offset) {
        for (Vector2d position : positions) {
            int x = position.getX() - offset.getX() + 1;
            int y = (position.getY() - offset.getY()) + 1;

            List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
            Animal currentAnimal = getChosenAnimal(animals);

            if (currentAnimal == null) {
                continue;
            }

            AnimalData animalData = dataCollector.getAnimalData(currentAnimal);
            Color animalColor = AnimalColor.getAnimalColor(animalData, dataCollector.getSimulationStatistics(), initialEnergy);
            StackPane stackPane = createColoredCellWithText(animalColor, currentAnimal.toString());
            stackPane.setOnMouseClicked(e -> simulationPresenter.onAnimalClicked(animals));
            gridPane.add(stackPane, x, y);
        }
    }

    private StackPane createColoredCellWithText(Color color, String textContent) {
        double cellSize = gridManager.calculateCellSize();
        Pane cell = new Pane();
        cell.setMinSize(cellSize, cellSize);
        cell.setPrefSize(cellSize, cellSize);
        cell.setMaxSize(cellSize, cellSize);
        BackgroundFill backgroundFill = new BackgroundFill(color, null, null);
        Background background = new Background(backgroundFill);
        cell.setBackground(background);
        Text text = new Text(textContent);
        text.setFill(Color.WHITE);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(cell, text);
        StackPane.setAlignment(text, Pos.CENTER);

        return stackPane;
    }

    private Animal getChosenAnimal(List<Animal> animals) {
        if (animals.isEmpty()) {
            return null;
        }
        return animals.getFirst();
    }
}