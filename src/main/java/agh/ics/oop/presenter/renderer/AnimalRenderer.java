package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.datacollectors.AnimalData;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.util.Genotype;
import agh.ics.oop.presenter.grid.GridRenderer;
import agh.ics.oop.presenter.util.AnimalColor;
import agh.ics.oop.presenter.util.ColorProvider;
import agh.ics.oop.presenter.util.ImageLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;

public class AnimalRenderer {

  private final GridRenderer gridRenderer;
  private final SimulationDataCollector dataCollector;
  private final ImageLoader imageLoader;

  private final int initialEnergy;

  public AnimalRenderer(
      GridRenderer gridRenderer,
      SimulationDataCollector dataCollector,
      ImageLoader imageLoader,
      int initialEnergy
  ) {
    this.gridRenderer = gridRenderer;
    this.dataCollector = dataCollector;
    this.imageLoader = imageLoader;
    this.initialEnergy = initialEnergy;
  }

  public void drawAnimalImages(Iterable<Vector2d> positions) {
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

  public void drawColoredAnimalElements(Iterable<Vector2d> positions, Color color) {
    for (Vector2d position : positions) {
      List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
      Animal currentAnimal = animals.getFirst();
      AnimalData animalData = dataCollector.getAnimalData(currentAnimal);
      Color animalColor = AnimalColor.getAnimalColor(animalData, initialEnergy, color);
      gridRenderer.drawColor(animalColor, position);
    }
  }
  public void drawDominantAnimalElements(Iterable<Vector2d> positions, SimulationStatistics stats) {
    List<Genotype> mostPopularGenotypes = stats.mostPopularGenotypes();
    if (mostPopularGenotypes.isEmpty()) return;

    List<Genotype> topGenotypes = mostPopularGenotypes.subList(0, Math.min(3, mostPopularGenotypes.size()));

    for (Vector2d position : positions) {
      List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
      for (Animal animal : animals) {
        AnimalData animalData = dataCollector.getAnimalData(animal);
        if (topGenotypes.contains(animalData.genotype())) {
          gridRenderer.drawBorder(position, ColorProvider.BORDER_DOMINANT_COLOR);
        }
      }
    }
  }

  public void drawBorderAroundChosenAnimal(Animal animal) {
      if(animal == null) return;
      AnimalData animalData = dataCollector.getAnimalData(animal);
      if(animal.isAlive()){
        gridRenderer.drawBorder(animalData.position(), ColorProvider.BORDER_CHOSEN_ANIMAL_COLOR);
      }
    }
}