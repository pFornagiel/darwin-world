package agh.ics.oop.model.statistics;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Genotype;

public record AnimalStatistics(
  Vector2d coordinates,
  int energy,
  int orientation,
  Genotype genotype,
  int eatenPlantsCount,
  int childrenCount,
  int descendantCount,
  int lifespan,
  int dayOfDeath
) {}
