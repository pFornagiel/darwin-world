// File: AnimalStatisticsUpdater.java
package agh.ics.oop.presenter;

import agh.ics.oop.model.datacollectors.SimulationDataCollector;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import javafx.scene.control.Label;

public class AnimalStatisticsUpdater {

    private final Label[] animalStatsLabels;
    private final Label animalTitle;
    private final SimulationDataCollector dataCollector;

    public AnimalStatisticsUpdater(Label[] animalStatsLabels, Label animalTitle, SimulationDataCollector dataCollector) {
        this.animalStatsLabels = animalStatsLabels;
        this.animalTitle = animalTitle;
        this.dataCollector = dataCollector;
    }

    public void updateAnimalStatisticsDisplay(Animal animal) {
        if (animal != null) {
            var stats = dataCollector.getAnimalStatistics(animal);
            if (stats != null && stats.coordinates() != null) {
                animalTitle.setText(String.format("Animal at (%d, %d)",
                        stats.coordinates().getX(),
                        stats.coordinates().getY()));
            } else {
                animalTitle.setText("No Animal Selected");
            }

            String[] values = {
                    String.valueOf(stats.lifespan()),
                    String.valueOf(stats.eatenPlantsCount()),
                    String.valueOf(stats.energy()),
                    String.valueOf(stats.childrenCount()),
                    String.valueOf(stats.descendantCount()),
                    stats.genotype().toString(),
                    String.valueOf(stats.currentGene()),
                    stats.dayOfDeath() == -1 ? "Alive" : String.valueOf(stats.dayOfDeath())
            };
            for (int i = 0; i < animalStatsLabels.length; i++) {
                animalStatsLabels[i].setText(values[i]);
            }
        } else {
            animalTitle.setText("No Animal Selected");
            for (Label label : animalStatsLabels) {
                label.setText("-");
            }
        }
    }
}