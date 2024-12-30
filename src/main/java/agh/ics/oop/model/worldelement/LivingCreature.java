package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.worldmap.MapTile;

import java.util.List;

public interface LivingCreature extends WorldElement {
  void move(MoveDirection direction, MoveValidator moveValidator);
  void consume(MapTile mapTile, int energy);
  int getEnergy();
  int getLifespan();
  Genotype getGenotype();
  List<LivingCreature> getChildren();

}
