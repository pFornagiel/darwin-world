package agh.ics.oop.model.worldmap.abstracts;

import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.util.MoveHandler;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.WorldElement;
import agh.ics.oop.model.worldmap.util.Boundary;

import java.util.*;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap<E extends WorldElement> extends MoveHandler {

    /**
     * Place an MainElementType on the map.
     *
     * @param element The element to place on the map.
     * @throws IncorrectPositionException when trying to place object of MainElementType onto a field
     * that is occupied by other object
     */
    void placeElement(E element) throws IncorrectPositionException;/**
     * Place an MainElementType on the map.
     *
     * @param element The element to place on the map.
     * @throws IncorrectPositionException when trying to place object of MainElementType onto a field
     * that is occupied by other object
     */
    void placeElement(E element, Vector2d Position) throws IncorrectPositionException;

    /**
     * Remove element from the map.
     *
     * @param element The element to remove from the map.
     */
    void removeElement(E element);


    /**
     * Moves a Moveable element (if it is present on the map) according to specified direction.
     */
    void moveElementTo(E element, Vector2d position);

    /**
     * Grows plant on the mapTile at given position.
     * @param position Position of MapTile, where plant should be grown.
     */
    void growPlantAtPosition(Vector2d position);


    void randomPlantGrowth(int numberOfPlants);

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

    boolean isPlantGrown(Vector2d position);
    /**
     * Return WorldElements at a given position.
     *
     * @param position Given position.
     * @return Set of WorldElements null if the position is not occupied.
     */
    Set<E> objectsAt(Vector2d position);

    /**
     * Get Set of all elements on map.
     *
     * @return Arraylist of WorldElement objects placed on the map.
     */
    Set<E> getElements();

    /**
     * Get amount of all elements on map.
     *
     * @return Arraylist of WorldElement objects placed on the map.
     */
    int getAmountOfElements();

    /**
     * Get Set of all fields occupied by elements on the map.
     *
     * @return Arraylist of Vector2d positions.
     */
    Set<Vector2d> getElementPositionSet();

    /**
     * Get Set of all fields occupied by plants on the map.
     *
     * @return Arraylist of Vector2d positions.
     */
    Set<Vector2d> getPlantPositionSet();

    /**
     * Get Set of all fields occupied by plants on the map.
     *
     * @return Arraylist of Vector2d positions.
     */
    Set<Vector2d> getVerdantFieldPositionSet();

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

    /**
     * Check if position is within y bounds (lowerBound inclusive, upperBound exclusive)
     *
     * @return boolean result.
     */
}
