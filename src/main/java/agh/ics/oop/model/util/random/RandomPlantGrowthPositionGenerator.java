package agh.ics.oop.model.util.random;

import agh.ics.oop.model.exception.util.random.RandomPositionOutOfRangeException;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldmap.abstracts.MapTile;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * A generator for randomly selecting positions for plant growth on a map.
 * The generator prioritizes verdant tiles over non-verdant tiles based on a weighted probability.
 *
 *  * <p>Implements {@link Iterable} to allow iteration over randomly generated positions.
 * @param <M> The type of map tile (extends {@link MapTile}) associated with the positions.
 */
public class RandomPlantGrowthPositionGenerator<M extends MapTile> implements Iterable<Vector2d> {
  private static final Random rand = new Random();
  private static final int VERDANT_TILE_PROBABILITY_MULTIPLIER = 8;
  private static final int NON_VERDANT_TILE_PROBABILITY_MULTIPLIER = 2;

  private final ArrayList<Vector2d> verdantPositions = new ArrayList<>();
  private final ArrayList<Vector2d> nonVerdantPositions = new ArrayList<>();

  private int numberOfPlants;
  private int verdantTileProbabilisticMeasure;
  private int nonVerdantTileProbabilisticMeasure;

  public RandomPlantGrowthPositionGenerator(HashMap<Vector2d, M> tileMap, int numberOfPlants) {
    this.numberOfPlants = numberOfPlants;
    for (HashMap.Entry<Vector2d, M> entry : tileMap.entrySet()) {
      Vector2d currentPosition = entry.getKey();
      M currentMapTile = entry.getValue();
      if (currentMapTile.isPlantGrown()) continue;
      if (currentMapTile.isVerdant()) {
        verdantPositions.add(currentPosition);
      } else {
        nonVerdantPositions.add(currentPosition);
      }
    }

    this.verdantTileProbabilisticMeasure = verdantPositions.size() * VERDANT_TILE_PROBABILITY_MULTIPLIER;
    this.nonVerdantTileProbabilisticMeasure = nonVerdantPositions.size() * NON_VERDANT_TILE_PROBABILITY_MULTIPLIER;
  }

  public Vector2d get() {
    numberOfPlants--;
    if(numberOfPlants < 0) {
      throw new RandomPositionOutOfRangeException();
    }

    int totalProbabilityMeasure = verdantTileProbabilisticMeasure + nonVerdantTileProbabilisticMeasure;
    int randomNumber = rand.nextInt(totalProbabilityMeasure)+1;
    Vector2d resultVector;
    if(randomNumber <= verdantTileProbabilisticMeasure) {
      int randomIndex = rand.nextInt(verdantPositions.size());
      resultVector = verdantPositions.get(randomIndex);
      verdantPositions.remove(randomIndex);
      verdantTileProbabilisticMeasure -= VERDANT_TILE_PROBABILITY_MULTIPLIER;
    } else {
      int randomIndex = rand.nextInt(nonVerdantPositions.size());
      resultVector = nonVerdantPositions.get(randomIndex);
      nonVerdantPositions.remove(randomIndex);
      nonVerdantTileProbabilisticMeasure -= NON_VERDANT_TILE_PROBABILITY_MULTIPLIER;
    }
    return resultVector;
  }

  @Override
  public Iterator<Vector2d> iterator() {
    return new RandomPlantGrowthPositionGeneratorIterator();
  }

  private class RandomPlantGrowthPositionGeneratorIterator implements Iterator<Vector2d> {

    @Override
    public boolean hasNext() {
      return numberOfPlants > 0 && verdantTileProbabilisticMeasure + nonVerdantTileProbabilisticMeasure > 0;
    }

    @Override
    public Vector2d next() {
      return get();
    }
  }
}
