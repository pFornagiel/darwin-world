package agh.ics.oop.model.exception.resources;

import java.nio.file.Path;

public class ImageFileCouldNotBeFoundException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Image could not be loaded: Path %s does not exist.";

  public ImageFileCouldNotBeFoundException(Path path) {
    super(ERROR_MESSAGE.formatted(path));
  }
}
