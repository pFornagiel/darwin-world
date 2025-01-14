package agh.ics.oop.model.simulation;

import agh.ics.oop.model.worldelement.BaseAnimal;

public interface WorldElementVisitor {

  void visit(BaseAnimal animal);
}
