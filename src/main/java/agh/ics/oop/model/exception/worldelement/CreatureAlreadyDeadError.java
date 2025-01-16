package agh.ics.oop.model.exception.worldelement;

import agh.ics.oop.model.worldelement.abstracts.LivingCreature;

public class CreatureAlreadyDeadError extends RuntimeException {
  private static final String ERROR_MESSAGE = "Cannot kill LivingCreature: %s is already dead.";
  public CreatureAlreadyDeadError(LivingCreature creature) {
    super(ERROR_MESSAGE.formatted(creature));
  }
}
