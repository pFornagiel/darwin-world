package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.datacollectors.DataVisitor;
import agh.ics.oop.model.datacollectors.SimulationData;
import agh.ics.oop.model.simulation.SimulationVisitor;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.worldelement.abstracts.AnimalFactory;
import agh.ics.oop.model.worldmap.abstracts.AbstractAnimalMap;

public class BaseWorldMap extends AbstractAnimalMap<BaseMapTile> {

  public BaseWorldMap(int mapWidth, int mapHeight, AnimalFactory animalFactory) {
    super(mapWidth, mapHeight, animalFactory);
  }

  @Override
  protected void initialiseTileMap(int mapWidth, int mapHeight) {
    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        Vector2d position = new Vector2d(x,y);
        tileMap.put(position, new BaseMapTile(position));
      }
    }
  }


  @Override
  public void accept(SimulationVisitor visitor){
    visitor.visit(this);
  }

  @Override
  public SimulationData acceptData(DataVisitor visitor) {
    return visitor.visitWorldMapData(this);
  }
}
