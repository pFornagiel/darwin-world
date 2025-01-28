package agh.ics.oop.presenter.statistics;

import agh.ics.oop.model.datacollectors.AnimalStatistics;
import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.util.Genotype;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class AnimalStatisticsUpdater {

  private static final String ANIMAL_TITLE_FORMAT = "Animal at (%s, %s)";
  private static final String ANIMAL_DEAD_TITLE = "Animal is dead";
  private static final String NO_ANIMAL_SELECTED = "No Animal Selected";
  private static final String DEFAULT_LABEL_TEXT = "-";
  private static final String ALIVE_STATUS = "Alive";

  private final Label[] animalStatsLabels;
  private final Label animalTitle;
  private final SimulationDataCollector dataCollector;

  private Animal selectedAnimal;

  public AnimalStatisticsUpdater(Label[] animalStatsLabels, Label animalTitle, SimulationDataCollector dataCollector) {
    this.animalStatsLabels = animalStatsLabels;
    this.animalTitle = animalTitle;
    this.dataCollector = dataCollector;
  }

  public void updateAnimalStatisticsDisplay(Animal animal) {
    if (animal != null) {
      AnimalStatistics stats = dataCollector.getAnimalStatistics(animal);

      animalTitle.setText(
          Optional.ofNullable(stats.coordinates())
              .map(c -> ANIMAL_TITLE_FORMAT.formatted(c.getX(), c.getY()))
              .orElse(ANIMAL_DEAD_TITLE)
      );

      String[] values = {
          String.valueOf(stats.lifespan()),
          String.valueOf(stats.eatenPlantsCount()),
          String.valueOf(stats.energy()),
          String.valueOf(stats.childrenCount()),
          String.valueOf(stats.descendantCount()),
          stats.genotype().toString(),
          String.valueOf(stats.currentGene()),
          stats.dayOfDeath() == -1 ? ALIVE_STATUS : String.valueOf(stats.dayOfDeath())
      };

      IntStream.range(0, animalStatsLabels.length)
          .forEach(i -> animalStatsLabels[i].setText(values[i]));

    } else {
      animalTitle.setText(NO_ANIMAL_SELECTED);
      for (Label label : animalStatsLabels) {
        label.setText(DEFAULT_LABEL_TEXT);
      }
    }
  }

  public void updateAnimalStatistics(Animal animal) {
    updateAnimalStatisticsDisplay(animal);
  }
}