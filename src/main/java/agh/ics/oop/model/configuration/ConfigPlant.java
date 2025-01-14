package agh.ics.oop.model.configuration;

public record ConfigPlant(
    int initialPlantCount,
    int energyPerPlant,
    int dailyPlantGrowth
) {
}
