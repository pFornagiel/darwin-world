package agh.ics.oop.model.util.abstracts;

import agh.ics.oop.model.util.Vector2d;

public interface MoveHandler {

    /**
     * Indicate if any object can move to position after move.
     *
     * @param position New position of object.
      @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Return new position of the object, after moving in certain direction.
     *
     * @param position The position checked for the movement possibility.
     * @return Vector2d - new object position.
     */
    Vector2d determinePositionAfterMove(Vector2d position, Vector2d move);
}
