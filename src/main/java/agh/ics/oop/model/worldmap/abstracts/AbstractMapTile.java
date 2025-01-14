package agh.ics.oop.model.worldmap.abstracts;

import agh.ics.oop.model.exception.worldmap.PlantAlreadyGrownException;
import agh.ics.oop.model.exception.worldmap.PlantNotGrownException;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.WorldElement;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractMapTile<E extends WorldElement> implements MapTile<E> {
  private final Vector2d position;
  private boolean isVerdant = false;
  private boolean plantGrown = false;
  private final HashSet<E> elementSet = new HashSet<>();

  public AbstractMapTile(Vector2d position) {
    this.position = position;
  }

  public AbstractMapTile(Vector2d position, boolean isVerdant) {
    this(position);
    this.isVerdant = isVerdant;
  }

  //  isVerdant methods
  public boolean isVerdant() {
    return isVerdant;
  }
  public void setVerdant() {
    isVerdant = true;
  }

  public boolean isPlantGrown() {
    return plantGrown;
  }
  public void growPlant(){
    if(plantGrown){
      throw new PlantAlreadyGrownException(position);
    }
    plantGrown = true;
  }
  public void eatPlant(){
    if(!plantGrown){
      throw new PlantNotGrownException(position);
    }
    plantGrown = false;
  }
  public boolean isOccupied(){
    return !elementSet.isEmpty();
  }
  public boolean addElement(E element){
    return this.elementSet.add(element);
  }
  public boolean removeElement(E element){
    return this.elementSet.remove(element);
  }
  public boolean containsElement(E element){
    return this.elementSet.contains(element);
  }
  public Set<E> getAnimalSet(){
    return Collections.unmodifiableSet(elementSet);
  }
  public List<E> getElementList(){
//    Return immutable set
    return List.copyOf(elementSet);
  }


}
