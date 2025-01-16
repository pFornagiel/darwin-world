package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.Animal;
import agh.ics.oop.model.worldmap.abstracts.AbstractMapTile;

public class FireMapTile extends AbstractMapTile<Animal> {
  private boolean onFire = false;
  private int fireDuration = 0;

  public FireMapTile(Vector2d position) {
    super(position);
  }

  public FireMapTile(Vector2d position, boolean isVerdant) {
    super(position, isVerdant);
  }

  public boolean isOnFire() {
    return onFire;
  }
  public void setOnFire(){
    onFire = true;
  }
  public void extenguish(){
    fireDuration = 0;
    onFire = false;
  }

  public void updateFireDuration(){
    fireDuration++;
  }

  public int getFireDuration(){
    return fireDuration;
  }
}
