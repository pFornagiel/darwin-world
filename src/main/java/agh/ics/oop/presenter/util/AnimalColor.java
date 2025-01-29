package agh.ics.oop.presenter.util;

import agh.ics.oop.model.datacollectors.AnimalData;
import javafx.scene.paint.Color;

public class AnimalColor {
    private static final double OPACITY = 1.0;
    public static Color getAnimalColor(AnimalData animalData, int animalEnergy, Color color){
        if(animalData == null) return Color.GRAY;
        double energyRatio = Math.min((double) animalData.energy() / animalEnergy, 1.0);

        // Interpolate between dark gray and the base color
        double r = ColorProvider.DYING_COLOR.getRed() + (color.getRed() - ColorProvider.DYING_COLOR.getRed()) * energyRatio;
        double g = ColorProvider.DYING_COLOR.getGreen() + (color.getGreen() - ColorProvider.DYING_COLOR.getGreen()) * energyRatio;
        double b = ColorProvider.DYING_COLOR.getBlue() + (color.getBlue() - ColorProvider.DYING_COLOR.getBlue()) * energyRatio;

        return new Color(r, g, b, OPACITY);
    }
}