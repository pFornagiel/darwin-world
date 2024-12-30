package agh.ics.oop.model.simulation;

import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Animal;
import agh.ics.oop.model.worldmap.WorldMap;

import java.util.ArrayList;

public class Simulation implements Runnable {
//  ArrayList is more suitable in our case, because the `get` operation, which allows for reading
//  data at specified index, is more optimal in comparison to LinkedList. We do not have to
//  add any data to already existing lists after initialisation - if we did, the LinkedList datatype
//  would prove more efficient.
  ArrayList<Vector2d> defaultPositionList;
  ArrayList<MoveDirection> moveDirectionList;
  ArrayList<Animal> animalList = new ArrayList<>();
  WorldMap map;

  private static final String PLACE_ERROR_MESSAGE = "Could not place animal: %s%n";
  private static final String INTERRUPT_ERROR_MESSAGE = "Engine: Interrupted. Restoring interrupted status. Reason: %s%n";

  public Simulation(ArrayList<Vector2d> defaultPositionList, ArrayList<MoveDirection> moveDirectionList, WorldMap map) {
    this.defaultPositionList = defaultPositionList;
    this.moveDirectionList = moveDirectionList;
    this.map = map;
    for(Vector2d position : defaultPositionList){
      animalList.add(new Animal(position));
    }
  }

  public Animal getAnimalAt(int index){
    return animalList.get(index);
  }

  public int getAnimalListLength(){
    return animalList.size();
  }


  @Override
  public void run(){
    int index = 0;
    int length = animalList.size();
    for(Animal animal: animalList){
      try{
        map.placeElement(animal);
      } catch (IncorrectPositionException error){
        System.out.printf(PLACE_ERROR_MESSAGE, error.getMessage());
      }
    }
    try{
      for(MoveDirection direction: moveDirectionList){
        Animal currentAnimal = animalList.get(index);
        map.move(currentAnimal, direction);
        index = (index+1)%length;
        Thread.sleep(500);
      }
    } catch(InterruptedException error){
      System.out.printf(INTERRUPT_ERROR_MESSAGE, error.getMessage());
      Thread.currentThread().interrupt(); //Restoring interrupted status
    }
  }



}
