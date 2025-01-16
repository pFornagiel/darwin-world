package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.util.Vector2d;

public record AnimalData(
    int energy,
    Vector2d position,
    Direction orientation,
    int currentGene
) {
}
