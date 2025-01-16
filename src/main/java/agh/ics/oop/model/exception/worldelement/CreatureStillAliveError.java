package agh.ics.oop.model.exception.worldelement;

import agh.ics.oop.model.worldelement.abstracts.LivingCreature;

public class CreatureStillAliveError extends RuntimeException {
  private static final String ERROR_MESSAGE = "Cannot get day of death: LivingCreature %s is still alive";
  public CreatureStillAliveError(LivingCreature creature) {
    super(ERROR_MESSAGE.formatted(creature));
  }
}
