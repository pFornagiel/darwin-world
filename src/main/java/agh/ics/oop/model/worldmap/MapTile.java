package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.exception.worldmap.PlantAlreadyGrownException;
import agh.ics.oop.model.exception.worldmap.PlantNotGrownException;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Animal;

import java.util.HashSet;
import java.util.Set;

public class MapTile {
  private final Vector2d position;
  private boolean isVerdant = false;
  private boolean plantGrown = false;
  private final HashSet<Animal> animalSet = new HashSet<>();

  public MapTile(Vector2d position) {
    this.position = position;
  }

  public MapTile(Vector2d position, boolean isVerdant) {
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
    return !animalSet.isEmpty();
  }
  public boolean addAnimal(Animal animal){
    return this.animalSet.add(animal);
  }
  public boolean removeAnimal(Animal animal){
    return this.animalSet.remove(animal);
  }
  public boolean containsAnimal(Animal animal){
    return this.animalSet.contains(animal);
  }
  public Set<Animal> getAnimalSet(){
//    Return immutable set
    return Set.copyOf(animalSet);
  }


}
