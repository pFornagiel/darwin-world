package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.grid.GridRenderer;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MapRenderer {
    private final static Color COLOR_PLANT = Color.DARKGREEN;
    private final static Color COLOR_FIRE = Color.RED;

    private final ElementRenderer elementRenderer;
    private final AnimalRenderer animalRenderer;
    private final GridRenderer gridRenderer;
    private final ImageLoader imageLoader;
    private final int maxMapSizeForImages;
    private final int mapArea;

    public MapRenderer(
       GridManager gridManager,
       GridRenderer gridRenderer,
       ImageLoader imageLoader,
       int maxMapSizeForImages,
       AnimalRenderer animalRenderer
    ) {
        this.maxMapSizeForImages = maxMapSizeForImages;
        this.imageLoader = imageLoader;
        this.elementRenderer = new ElementRenderer(gridRenderer);
        this.animalRenderer = animalRenderer;
        this.gridRenderer = gridRenderer;
        this.mapArea = gridManager.getGridArea();
    }

    public void drawMap(SimulationData simulationData) {
        if (mapArea > maxMapSizeForImages) {
            elementRenderer.drawColoredElements(simulationData.plantPositionSet(), COLOR_PLANT);
            animalRenderer.drawColoredAnimalElements(simulationData.animalPositionSet());
            elementRenderer.drawColoredElements(simulationData.firePositionSet(), COLOR_FIRE);
        } else {
            Image plantImage = imageLoader.getPlantImage();
            Image fireImage = imageLoader.getFireImage();
            elementRenderer.drawElements(simulationData.plantPositionSet(), plantImage);
            animalRenderer.drawAnimalElements(simulationData.animalPositionSet());
            elementRenderer.drawElements(simulationData.firePositionSet(), fireImage);
        }

    }
}