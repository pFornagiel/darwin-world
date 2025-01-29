package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.grid.GridRenderer;
import agh.ics.oop.presenter.util.ColorProvider;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MapRenderer {
    private final ElementRenderer elementRenderer;
    private final AnimalRenderer animalRenderer;
    private final ImageLoader imageLoader;
    private final int maxMapSizeForImages;
    private final int mapArea;
    private final GridRenderer gridRenderer;

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
        this.mapArea = gridManager.getGridArea();
        this.gridRenderer = gridRenderer;
    }

    public void drawMap(SimulationData simulationData, Animal chosenAnimal) {
        gridRenderer.clearCanvas();
        if (mapArea > maxMapSizeForImages) {
            elementRenderer.drawColoredElements(simulationData.plantPositionSet(), ColorProvider.COLOR_PLANT);
            animalRenderer.drawColoredAnimalElements(simulationData.animalPositionSet(), ColorProvider.COLOR_ANIMAL);
            elementRenderer.drawColoredElements(simulationData.firePositionSet(), ColorProvider.COLOR_FIRE);
            animalRenderer.drawBorderAroundChosenAnimal(chosenAnimal);
        } else {
            Image plantImage = imageLoader.getPlantImage();
            Image fireImage = imageLoader.getFireImage();
            elementRenderer.drawElements(simulationData.plantPositionSet(), plantImage);
            animalRenderer.drawAnimalImages(simulationData.animalPositionSet());
            elementRenderer.drawElements(simulationData.firePositionSet(), fireImage);
            animalRenderer.drawBorderAroundChosenAnimal(chosenAnimal);
        }

    }
}