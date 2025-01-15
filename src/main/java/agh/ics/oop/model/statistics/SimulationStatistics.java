package agh.ics.oop.model.statistics;

import agh.ics.oop.model.worldelement.Genotype;

public record SimulationStatistics(
  int amountOfAnimals,
  int amountOfPlants,
  int amountOfFreeFields,
  Genotype[] mostPopularGenotypes,
  double averageEnergy,
  double averageLifespan,
  double averageChildren,
  int amountOfDays
) {}
