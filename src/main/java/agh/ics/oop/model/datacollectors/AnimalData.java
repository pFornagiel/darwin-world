package agh.ics.oop.model.datacollectors;

import agh.ics.oop.model.util.Direction;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.util.Genotype;

public record AnimalData(
    int energy,
    Vector2d position,
    Direction orientation,
    int currentGene,
    Genotype genotype
) {
}
