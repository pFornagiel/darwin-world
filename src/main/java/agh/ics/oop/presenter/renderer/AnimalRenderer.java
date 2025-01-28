package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.AnimalData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.presenter.grid.GridManager;
import agh.ics.oop.presenter.grid.GridRenderer;
import agh.ics.oop.presenter.util.AnimalColor;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;

public class AnimalRenderer {
  private final GridManager gridManager;
  private final GridRenderer gridRenderer;
  private final SimulationDataCollector dataCollector;
  private final ImageLoader imageLoader;
  private final int maxMapSizeForImages;

  private final int initialEnergy;

  public AnimalRenderer(
      GridManager gridManager,
      GridRenderer gridRenderer,
      SimulationDataCollector dataCollector,
      ImageLoader imageLoader,
      int maxMapSizeForImages,
      int initialEnergy
  ) {
    this.gridManager = gridManager;
    this.gridRenderer = gridRenderer;
    this.dataCollector = dataCollector;
    this.imageLoader = imageLoader;
    this.maxMapSizeForImages = maxMapSizeForImages;
    this.initialEnergy = initialEnergy;
  }

  public void drawAnimalElements(Iterable<Vector2d> positions) {
    int mapArea = gridManager.getGridArea();

    if (mapArea > maxMapSizeForImages) {
      drawColoredAnimalElements(positions);
    } else {
      drawAnimalImages(positions);
    }
  }

  private void drawAnimalImages(Iterable<Vector2d> positions) {
    for (Vector2d position : positions) {
      List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
      Animal currentAnimal = animals.getFirst();
      Direction orientation = (currentAnimal.getOrientation());

      ImageView snailImageView = new ImageView();
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
          rotation = -45;
          break;
        case SOUTH_WEST:
          snailImageView.setImage(imageLoader.getSnailFront());
          rotation = 45;
          break;
        default:
          snailImageView.setImage(imageLoader.getSnailFront());
          break;
      }


      double scaleSize = rotation == 0 ? 1 : Math.sqrt(2);
      snailImageView.setRotate(rotation);

      SnapshotParameters snapshotParameters = new SnapshotParameters();
      snapshotParameters.setFill(Color.TRANSPARENT);
      Image newImage = snailImageView.snapshot(snapshotParameters, null);

      gridRenderer.drawElement(newImage, position, scaleSize);
    }
  }

  public void drawColoredAnimalElements(Iterable<Vector2d> positions) {
    for (Vector2d position : positions) {
      List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
      Animal currentAnimal = animals.getFirst();
      AnimalData animalData = dataCollector.getAnimalData(currentAnimal);
      Color animalColor = AnimalColor.getAnimalColor(animalData, dataCollector.getSimulationStatistics(), initialEnergy);

      gridRenderer.drawColor(animalColor, position);
    }
  }

}