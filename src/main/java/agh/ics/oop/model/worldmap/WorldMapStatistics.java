package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.worldelement.Genotype;

import java.util.List;

public record WorldMapStatistics(
    int amountOfAnimals,
    int amountOfPlants,
    int amountOfFreeFields,
    List<Genotype> mostPopularGenotypes,
    double averageEnergyLevel,
    double averageLifespan,
    double averageAmountOfChildren
    ) {

}
