package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.exception.IncorrectPositionException;
import agh.ics.oop.model.simulation.WorldElementVisitor;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.Direction;
import agh.ics.oop.model.worldelement.LivingCreature;
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
public interface WorldMap<E extends WorldElement> extends MoveValidator {

    /**
     * Place an MainElementType on the map.
     *
     * @param element The element to place on the map.
     * @throws IncorrectPositionException when trying to place object of MainElementType onto a field
     * that is occupied by other object
     */
    void placeElement(E element) throws IncorrectPositionException;

    /**
     * Remove element from the map.
     *
     * @param element The element to remove from the map.
     */
    void removeElement(E element);


    /**
     * Moves a Moveable element (if it is present on the map) according to specified direction.
     */
    void move(LivingCreature element, Direction direction);

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
     * Makes consumer consume plant at given position.
     * @param consumer element which is to consume plant.
     * @param energy energy granted after consuming plant.
     */
    void consumePlant(LivingCreature consumer, int energy);

    /**
     * Makes element interact with the world using a visitor.
     * @param element element which is to interact.
     * @param visitor visitor allowing element to perform an action.
     */
    void interact(E element, WorldElementVisitor visitor);

    void listMapStatistics(WorldMapVisitor visitor);

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
     * Return WorldElements at a given position.
     *
     * @param position Given position.
     * @return Set of WorldElements null if the position is not occupied.
     */
    Set<E> objectsAt(Vector2d position);

    /**
     * Get Arraylist of all elements on map.
     *
     * @return Arraylist of WorldElement objects placed on the map.
     */
    ArrayList<E> getElements();

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
