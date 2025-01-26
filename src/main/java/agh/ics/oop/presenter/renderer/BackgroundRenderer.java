package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class BackgroundRenderer {

    private final GridPane grassGridPane;
    private final GridManager gridManager;
    private final Image[] grassImages;
    private final Image[] verdantImages;
    private final Random random;

    public BackgroundRenderer(GridPane grassGridPane, GridManager gridManager, Image[] grassImages, Image[] verdantImages) {
        this.grassGridPane = grassGridPane;
        this.gridManager = gridManager;
        this.grassImages = grassImages;
        this.verdantImages = verdantImages;
        this.random = new Random();
    }

    public void initializeGrassGrid(SimulationData simulationData, GridManager gridManager) {
        grassGridPane.getChildren().clear();
        int rows = gridManager.getGridPaneSize().getY();
        int cols = gridManager.getGridPaneSize().getX();
        for (int i = 2; i < rows + 1; i++) {
            for (int j = 2; j < cols + 1; j++) {
                ImageView imageView = new ImageView();
                double cellSize = gridManager.calculateCellSize(); // Use GridManager to calculate cell size

                Vector2d position = new Vector2d(j - 1, i - 1);
                if (simulationData != null && simulationData.verdantFieldPositionSet().contains(position)) {
                    imageView.setImage(verdantImages[random.nextInt(verdantImages.length)]);
                } else {
                    imageView.setImage(grassImages[random.nextInt(grassImages.length)]);
                }

                imageView.setFitWidth(cellSize);
                imageView.setFitHeight(cellSize);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(false);

                grassGridPane.add(imageView, j, i);
            }
        }
    }
}