package agh.ics.oop.presenter.statistics;

import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.util.Genotype;
import javafx.scene.control.Label;

import java.util.List;

public class AnimalStatisticsUpdater {

    private static final String ANIMAL_TITLE_FORMAT = "Animal at (%d, %d)";
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
            var stats = dataCollector.getAnimalStatistics(animal);
            if (stats != null && stats.coordinates() != null) {
                animalTitle.setText(String.format(ANIMAL_TITLE_FORMAT,
                        stats.coordinates().getX(),
                        stats.coordinates().getY()));
            }

            assert stats != null;
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
            for (int i = 0; i < animalStatsLabels.length; i++) {
                animalStatsLabels[i].setText(values[i]);
            }
        } else if (selectedAnimal == null) {
            animalTitle.setText(NO_ANIMAL_SELECTED);
            for (Label label : animalStatsLabels) {
                label.setText(DEFAULT_LABEL_TEXT);
            }
        }
    }

    public void updateAnimalStatistics(Animal animal) {
        if (dataCollector != null) {
            selectedAnimal = selectAnimal(animal);
            updateAnimalStatisticsDisplay(selectedAnimal);
        }
    }

    private Animal selectAnimal(Animal animal) {
        if (animal != null) {
            return animal;
        }

        if (dataCollector.getSimulationStatistics() == null) {
            return null;
        }

        List<Genotype> dominantGenotypes = dataCollector.getSimulationStatistics().mostPopularGenotypes();
        if (dominantGenotypes.isEmpty()) {
            return null;
        }

        for (Vector2d position : dataCollector.getSimulationData().animalPositionSet()) {
            List<Animal> animals = dataCollector.getAnimalsAtPosition(position);
            if (animals.isEmpty()) continue;
            for (Animal a : animals) {
                if (a.getGenotype().equals(dominantGenotypes.getFirst())) {
                    return a;
                }
            }
        }
        return selectedAnimal;
    }
}