package agh.ics.oop.model.worldmap.abstracts;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.LivingCreature;

public interface SimulatableMap<E extends LivingCreature> extends WorldMap<E> {
  // Method to handle the killing of a dying animal
  void killDyingCreature(E creature);

  // Method to rotate an animal
  void rotateCreature(E animal);

  // Method to move an animal
  void moveCreature(E animal);

  // Method to handle consumption of plants by animals
  void consumePlant(Vector2d position);
  // Method to breed animals at a specific position
  void breedCreatures(Vector2d position);
  
  // Method to grow a specified amount of plants
  void growPlants(int amountOfPlants);
}
