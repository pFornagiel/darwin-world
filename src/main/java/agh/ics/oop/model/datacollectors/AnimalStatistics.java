package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.util.Genotype;

public record AnimalStatistics(
  Vector2d coordinates,
  int energy,
  Direction orientation,
  Genotype genotype,
  int currentGene,
  int eatenPlantsCount,
  int childrenCount,
  int descendantCount,
  int lifespan,
  int dayOfDeath
) {}
