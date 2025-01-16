package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.worldelement.util.Genotype;

import java.util.List;

public record SimulationStatistics(
  int amountOfAnimals,
  int amountOfPlants,
  int amountOfFreeFields,
  List<Genotype> mostPopularGenotypes,
  double averageEnergy,
  double averageLifespan,
  double averageChildren,
  int amountOfDays
) {}
