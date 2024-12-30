package agh.ics.oop.model.worldelement;

import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.worldmap.BaseMapTile;
import agh.ics.oop.model.worldmap.MapTile;

import java.util.List;

public interface LivingCreature extends WorldElement {
  public void move(MoveDirection direction, MoveValidator moveValidator);
  public void consume(MapTile mapTile, int energy);
  public int getEnergy();
  public int getLifespan();
  public Genotype getGenotype();
  public List<LivingCreature> getChildren();

}
