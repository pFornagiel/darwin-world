package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.worldelement.Animal;
import agh.ics.oop.model.worldelement.WorldElement;

import java.util.Set;

public interface MapTile<E extends WorldElement> {
  boolean isVerdant();
  void setVerdant();
  boolean isPlantGrown();
  void growPlant();
  void eatPlant();
  boolean isOccupied();
  boolean addElement(E element);
  boolean removeElement(E element);
  boolean containsElement(E element);
  Set<E> getElementSet();
}