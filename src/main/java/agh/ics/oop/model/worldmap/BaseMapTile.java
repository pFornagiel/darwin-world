package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.abstracts.AbstractMapTile;

public class BaseMapTile extends AbstractMapTile<Animal> {
  public BaseMapTile(Vector2d position) {
    super(position);
  }

  public BaseMapTile(Vector2d position, boolean isVerdant) {
    super(position, isVerdant);
  }
}

