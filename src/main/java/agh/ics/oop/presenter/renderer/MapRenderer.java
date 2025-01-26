package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class MapRenderer {
    private final GridManager gridManager;
    private final GridPane gridPane;
    private final ElementRenderer elementRenderer;
    private final AnimalRenderer animalRenderer;
    private final BorderRenderer borderRenderer;
    private final ImageLoader imageLoader; // Add ImageLoader as a field

    public MapRenderer(GridManager gridManager, GridPane gridPane,
                       SimulationDataCollector dataCollector,
                       SimulationPresenter simulationPresenter, BorderRenderer borderRenderer) {
        this.gridManager = gridManager;
        this.gridPane = gridPane;
        this.borderRenderer = borderRenderer;
        this.imageLoader = new ImageLoader();
        this.elementRenderer = new ElementRenderer(gridManager, gridPane);
        this.animalRenderer = new AnimalRenderer(gridManager, gridPane, dataCollector, simulationPresenter, imageLoader);
    }

    public void drawMap(SimulationData simulationData) {
        gridManager.clearGrid();
        Vector2d offset = gridManager.getGridPaneOffset();
        Image plantImage = imageLoader.getPlantImage();
        Image fireImage = imageLoader.getFireImage();
        elementRenderer.drawElements(simulationData.plantPositionSet(), plantImage, offset);
        elementRenderer.drawElements(simulationData.firePositionSet(), fireImage, offset);
        animalRenderer.drawAnimalElements(simulationData.animalPositionSet(), offset);
        borderRenderer.render(offset, gridManager.getGridPaneSize());
    }
}