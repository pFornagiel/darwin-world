package agh.ics.oop.model.simulation;

import agh.ics.oop.model.worldelement.Animal;

public interface WorldElementVisitor {

  void visit(Animal animal);
}
