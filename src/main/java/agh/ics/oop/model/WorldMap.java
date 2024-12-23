package agh.ics.oop.model;

import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap<MainElementType extends WorldElement> extends MoveValidator {

    /**
     * Place an MainElementType on the map.
     *
     * @param element The animal to place on the map.
     * @throws IncorrectPositionException when trying to place object of MainElementType onto a field
     * that is occupied by other object
     */
    void place(MainElementType element) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(MainElementType element, MoveDirection direction);

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
    WorldElement objectAt(Vector2d position);

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
    Boundary getCurrentBounds();

    void removeMainElementsFromWorld();
}
