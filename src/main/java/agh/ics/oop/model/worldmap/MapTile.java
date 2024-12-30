package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.worldelement.Animal;
import agh.ics.oop.model.worldelement.WorldElement;

import java.util.Set;

public interface MapTile<E extends WorldElement> {
  public boolean isVerdant();
  public void setVerdant();
  public boolean isPlantGrown();
  public void growPlant();
  public void eatPlant();
  public boolean isOccupied();
  public boolean addElement(E element);
  public boolean removeElement(E element);
  public boolean containsElement(E element);
  public Set<E> getElementSet();
}