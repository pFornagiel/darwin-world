package agh.ics.oop.model.exception.util;

import agh.ics.oop.model.worldelement.abstracts.WorldElement;

public class DatacollectorNotInitialized extends RuntimeException {

  private static final String ERROR_MESSAGE = "Datacollector is not initialized";

  public DatacollectorNotInitialized() {
    super(ERROR_MESSAGE);
  }
}


