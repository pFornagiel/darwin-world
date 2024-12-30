package agh.ics.oop.model.worldmap;

public interface WorldMapVisitor {
  WorldMapStatistics visit(BaseWorldMap map);
}
