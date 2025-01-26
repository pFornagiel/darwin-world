package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Random;

public class BackgroundRenderer {

    private final GridPane grassGridPane;
    private final Image[] grassImages;
    private final Image[] verdantImages;
    private final Random random;
    private final int maxMapSizeForImages;

    // Improved green color definition
    private static final Color GREEN_COLOR = Color.rgb(114, 117, 25); // Dark green color

    public BackgroundRenderer(GridPane grassGridPane, Image[] grassImages, Image[] verdantImages, int maxMapSizeForImages) {
        this.grassGridPane = grassGridPane;
        this.grassImages = grassImages;
        this.verdantImages = verdantImages;
        this.random = new Random();
        this.maxMapSizeForImages = maxMapSizeForImages;
    }

    public void initializeGrassGrid(SimulationData simulationData, GridManager gridManager) {
        grassGridPane.getChildren().clear();
        int rows = gridManager.getGridPaneSize().getY();
        int cols = gridManager.getGridPaneSize().getX();
        double cellSize = gridManager.calculateCellSize();
        boolean isMapTooLarge = (rows * cols) > maxMapSizeForImages;

        for (int i = 2; i < rows + 1; i++) {
            for (int j = 2; j < cols + 1; j++) {
                Vector2d position = new Vector2d(j - 1, i - 1);

                if (isMapTooLarge) {
                    // Use the constant GREEN_COLOR
                    Pane greenCell = GridRenderer.createColoredCell(cellSize, GREEN_COLOR);
                    GridRenderer.addToGrid(grassGridPane, greenCell, j, i);
                } else {
                    Image image = getImageForPosition(simulationData, position);
                    StackPane cell = GridRenderer.createCell(cellSize, image);
                    GridRenderer.addToGrid(grassGridPane, cell, j, i);
                }
            }
        }
    }

    private Image getImageForPosition(SimulationData simulationData, Vector2d position) {
        if (simulationData != null && simulationData.verdantFieldPositionSet().contains(position)) {
            return verdantImages[random.nextInt(verdantImages.length)];
        } else {
            return grassImages[random.nextInt(grassImages.length)];
        }
    }
}