package agh.ics.oop.presenter.grid;

import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.util.ColorProvider;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Random;

public class GridStaticRenderer extends GridRenderer {


  private static final Random random = new Random();

  private final int maxMapSizeForImages;
  private final ImageLoader imageLoader;
  private final int mapWidth;
  private final int mapHeight;

  public GridStaticRenderer(Canvas simulationCanvas, GridManager gridManager, ImageLoader imageLoader, int maxMapSizeForImages) {
    super(simulationCanvas, gridManager);
    this.maxMapSizeForImages = maxMapSizeForImages;
    this.imageLoader = imageLoader;
    this.mapWidth = gridManager.getGridSize().getX();
    this.mapHeight = gridManager.getGridSize().getY();
  }

  public void drawBackground(SimulationData simulationData) {
    boolean isMapTooLarge = mapWidth * mapHeight > maxMapSizeForImages;

    if (isMapTooLarge) {
      gc.setFill(ColorProvider.BACKGROUND_COLOR);
      gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
      for (Vector2d position : simulationData.verdantFieldPositionSet()) {
        drawColor(ColorProvider.VERDANT_COLOR, position);
      }
    } else {
      for (int i = 0; i < mapWidth; i++) {
        for (int j = 0; j < mapHeight; j++) {
          Vector2d position = new Vector2d(i, j);
          Image image = getImageForPosition(simulationData, position);
          drawElement(image, position);
        }
      }
    }
  }

  private void drawBorderCell(Vector2d upperPosition, Vector2d lowerPosition) {
    boolean isMapTooLarge = mapWidth * mapHeight > maxMapSizeForImages;
    if (isMapTooLarge) {
      drawColor(ColorProvider.MAP_BORDER_COLOR, upperPosition);
      drawColor(ColorProvider.MAP_BORDER_COLOR, lowerPosition);
    } else {
      drawElement(imageLoader.getBorderImage(), upperPosition);
      drawElement(imageLoader.getBorderImage(), lowerPosition);
    }
  }

  public void drawBorder() {

    for (int x = -gridOffset.getX(); x < mapWidth + gridOffset.getX(); x++) {
      Vector2d upperPosition = new Vector2d(x, -gridOffset.getY());
      Vector2d lowerPosition = new Vector2d(x, mapHeight - 1 + gridOffset.getY());
      drawBorderCell(upperPosition, lowerPosition);
    }

    for (int y = -gridOffset.getY(); y < mapHeight + 2 * gridOffset.getY(); y++) {
      Vector2d upperPosition = new Vector2d(-gridOffset.getX(), y);
      Vector2d lowerPosition = new Vector2d(mapWidth - 1 + gridOffset.getX(), y);
      drawBorderCell(upperPosition, lowerPosition);
    }
  }

  private Image getImageForPosition(SimulationData simulationData, Vector2d position) {
    Image[] verdantImages = imageLoader.getVerdantImages();
    Image[] grassImages = imageLoader.getGrassImages();
    if (simulationData != null && simulationData.verdantFieldPositionSet().contains(position)) {
      return verdantImages[random.nextInt(verdantImages.length)];
    } else {
      return grassImages[random.nextInt(grassImages.length)];
    }
  }
}
