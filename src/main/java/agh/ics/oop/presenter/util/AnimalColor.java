package agh.ics.oop.presenter.util;

import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldelement.util.Genotype;
import javafx.scene.paint.Color;

import java.util.List;
public class AnimalColor {
    public static Color getAnimalColor(Animal animal, SimulationStatistics stats, int animalEnergy){
        double ONE = 1.0;
        double HALF = 0.5;
        double QUARTER = 0.25;
        List<Genotype> mostPopularGenotypes = stats.mostPopularGenotypes();
        double energyRatio = Math.min((double) animal.getEnergy() / animalEnergy, ONE);
        double r = 0.0, g = 0.0, b = 0.0;
        if (!mostPopularGenotypes.isEmpty() && animal.getGenotype().equals(mostPopularGenotypes.get(0))) {
            r = energyRatio * HALF + HALF;
            b = energyRatio * HALF + HALF;
            g = energyRatio * QUARTER;
        } else {
            b = energyRatio;
        }
        return new Color(r, g, b, ONE);
    }
}