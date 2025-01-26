package agh.ics.oop.presenter.statistics;

import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.worldelement.util.Genotype;
import javafx.scene.control.Label;

import java.util.List;

import static agh.ics.oop.presenter.util.Rounder.roundToTwoDecimal;

public class StatisticsUpdater {

    private final Label freeFields;
    private final Label genotype1;
    private final Label genotype2;
    private final Label genotype3;
    private final Label averageEnergy;
    private final Label averageLifespan;
    private final Label averageChildren;

    public StatisticsUpdater(Label freeFields, Label genotype1, Label genotype2, Label genotype3,
                             Label averageEnergy, Label averageLifespan, Label averageChildren) {
        this.freeFields = freeFields;
        this.genotype1 = genotype1;
        this.genotype2 = genotype2;
        this.genotype3 = genotype3;
        this.averageEnergy = averageEnergy;
        this.averageLifespan = averageLifespan;
        this.averageChildren = averageChildren;
    }

    public void updateStatistics(SimulationStatistics statistics) {
        if (statistics == null) {
            return;
        }

        freeFields.setText(String.valueOf(statistics.amountOfFreeFields()));

        List<Genotype> mostPopularGenotypes = statistics.mostPopularGenotypes();
        Label[] genotypeLabels = {genotype1, genotype2, genotype3};
        for (int i = 0; i < genotypeLabels.length; i++) {
            if (i < mostPopularGenotypes.size()) {
                genotypeLabels[i].setText(mostPopularGenotypes.get(i).toString());
            } else {
                genotypeLabels[i].setText("-");
            }
        }

        averageEnergy.setText(String.format("%.2f", statistics.averageEnergy()));
        averageLifespan.setText(String.valueOf(roundToTwoDecimal(statistics.averageLifespan())));
        averageChildren.setText(String.format("%.2f", statistics.averageChildren()));
    }
}