package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.util.MoveDirection;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Animal;
import agh.ics.oop.model.worldelement.WorldElement;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place an MainElementType on the map.
     *
     * @param animal The animal to place on the map.
     * @throws IncorrectPositionException when trying to place object of MainElementType onto a field
     * that is occupied by other object
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, MoveDirection direction);

    /**
     * Grows plant on the mapTile at given position.
     * @param position Position of MapTile, where plant should be grown.
     */
    void growPlantAtPosition(Vector2d position);


    /**
     * Deletes plant from the mapTile at given position.
     * @param position Position of MapTile, where plant should be deleted from.
     */
    void deletePlantAtPosition(Vector2d position);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    Set<Animal> objectsAt(Vector2d position);

    /**
     * Get Arraylist of all elements on map.
     *
     * @return Arraylist of WorldElement objects placed on the map.
     */
    ArrayList<WorldElement> getElements();

    /**
     * Get UUID of the map.
     *
     * @return UUID of the map.
     */
    UUID getId();

    /**
     * Get current boundary of the map represented by two Vector2d objects.
     *
     * @return Boundary record.
     */
    Boundary getBoundaries();
}
