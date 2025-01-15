package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;

import java.util.HashSet;
import java.util.Set;

public record SimulationData(
  Set<Vector2d> animalPositionSet,
  Set<Vector2d> plantPositionSet,
  Set<Vector2d> verdantFieldPositionSet,
  Set<Vector2d> firePositionSet
) {
  SimulationData(
      Set<Vector2d> animalPositionSet,
      Set<Vector2d> plantPositionSet,
      Set<Vector2d> verdantFieldPositionSet
  ){
    this(animalPositionSet, plantPositionSet,verdantFieldPositionSet, new HashSet<Vector2d>());
  }
}
