package agh.ics.oop.presenter.util;

import agh.ics.oop.model.datacollectors.AnimalData;
import agh.ics.oop.model.datacollectors.SimulationStatistics;
import agh.ics.oop.model.worldelement.util.Genotype;
import javafx.scene.paint.Color;

import java.util.List;
public class AnimalColor {
    public static Color getAnimalColor(AnimalData animalData, SimulationStatistics stats, int animalEnergy){
        if(animalData == null) return Color.GRAY;
        double ONE = 1.0;
        double r = 0.0, g = 0.0, b = Math.min((double) animalData.energy() / animalEnergy, ONE);
        return new Color(r, g, b, ONE);
    }
}