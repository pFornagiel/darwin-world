package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class MapRenderer {
    private final GridManager gridManager;
    private final ElementRenderer elementRenderer;
    private final AnimalRenderer animalRenderer;
    private final BorderRenderer borderRenderer;
    private final ImageLoader imageLoader;
    private final int MAX_MAP_SIZE_FOR_IMAGES;
    private ConfigAnimal animalConfig;
    public MapRenderer(GridManager gridManager, GridPane gridPane,
                       SimulationDataCollector dataCollector,
                       SimulationPresenter simulationPresenter, BorderRenderer borderRenderer, ImageLoader imageLoader, int MAX_MAP_SIZE_FOR_IMAGES, ConfigAnimal animalConfig) {
        this.MAX_MAP_SIZE_FOR_IMAGES = MAX_MAP_SIZE_FOR_IMAGES;
        this.gridManager = gridManager;
        this.borderRenderer = borderRenderer;
        this.imageLoader = imageLoader;
        this.elementRenderer = new ElementRenderer(gridManager, gridPane);
        this.animalRenderer = new AnimalRenderer(gridManager, gridPane, dataCollector, simulationPresenter, imageLoader,MAX_MAP_SIZE_FOR_IMAGES, animalConfig.initialEnergy());
        this.animalConfig = animalConfig;
    }

    public void drawMap(SimulationData simulationData) {
        gridManager.clearGrid();
        Vector2d offset = gridManager.getGridPaneOffset();
        Vector2d mapSize = gridManager.getGridPaneSize();
        int mapArea = mapSize.getX() * mapSize.getY();

        if (mapArea > MAX_MAP_SIZE_FOR_IMAGES) {
            elementRenderer.drawColoredElements(simulationData.verdantFieldPositionSet(), Color.GREEN, offset);
            elementRenderer.drawColoredElements(simulationData.plantPositionSet(), Color.DARKGREEN, offset);
            animalRenderer.drawColoredAnimalElements(simulationData.animalPositionSet(), offset);
            elementRenderer.drawColoredElements(simulationData.firePositionSet(), Color.RED, offset);
        } else {
            Image plantImage = imageLoader.getPlantImage();
            Image fireImage = imageLoader.getFireImage();
            elementRenderer.drawElements(simulationData.plantPositionSet(), plantImage, offset);
            animalRenderer.drawAnimalElements(simulationData.animalPositionSet(), offset);
            elementRenderer.drawElements(simulationData.firePositionSet(), fireImage, offset);
        }
        borderRenderer.render(offset, mapSize);
    }
}