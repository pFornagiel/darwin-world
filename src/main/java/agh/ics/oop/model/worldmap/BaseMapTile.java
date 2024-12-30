package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.exception.worldmap.PlantAlreadyGrownException;
import agh.ics.oop.model.exception.worldmap.PlantNotGrownException;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Animal;

import java.util.HashSet;
import java.util.Set;

public class BaseMapTile implements MapTile<Animal>{
  private final Vector2d position;
  private boolean isVerdant = false;
  private boolean plantGrown = false;
  private final HashSet<Animal> elementSet = new HashSet<>();

  public BaseMapTile(Vector2d position) {
    this.position = position;
  }

  public BaseMapTile(Vector2d position, boolean isVerdant) {
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

//  plantGrown methods
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

//  animalSet methods
  public boolean isOccupied(){
    return !elementSet.isEmpty();
  }
  public boolean addElement(Animal element){
    return this.elementSet.add(element);
  }
  public boolean removeElement(Animal element){
    return this.elementSet.remove(element);
  }
  public boolean containsElement(Animal element){
    return this.elementSet.contains(element);
  }
  public Set<Animal> getElementSet(){
//    Return immutable set
    return Set.copyOf(elementSet);
  }


}
