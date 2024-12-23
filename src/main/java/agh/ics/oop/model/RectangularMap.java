package agh.ics.oop.model;


import agh.ics.oop.model.util.IncorrectPositionException;

//Define animal as mainElement type
public class RectangularMap extends AbstractWorldMap<Animal>{
  private final static Vector2d MAP_LOWER_BOUND = new Vector2d(0,0);
  private final Vector2d MAP_UPPER_BOUND;

  public RectangularMap(int width, int height) {
    super();
    this.MAP_UPPER_BOUND = new Vector2d(width,height);
    this.mapBoundaries = new Boundary(MAP_LOWER_BOUND, MAP_UPPER_BOUND);
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    Vector2d currentPosition = animal.getPosition();
    animal.move(direction,this);
    if(currentPosition != animal.getPosition()){
      mainElementPositionMap.put(animal.getPosition(), animal);
      mainElementPositionMap.put(currentPosition, null);
    }
    notifyAccordingToMove("Animal", direction, currentPosition, animal.getPosition());
  }

  @Override
  public void place(Animal animal) throws IncorrectPositionException {
    super.place(animal);
    notifyAccordingToMove("Animal", animal.getPosition());
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    boolean isInBounds = position.precedes(MAP_LOWER_BOUND) && position.follows(MAP_UPPER_BOUND);
    return isInBounds && super.canMoveTo(position);
  }



  @Override
  public String toString() {
    return this.mapVisualizer.draw(MAP_LOWER_BOUND, MAP_UPPER_BOUND);
  }
}