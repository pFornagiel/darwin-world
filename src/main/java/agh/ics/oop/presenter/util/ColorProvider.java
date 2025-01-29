package agh.ics.oop.presenter.util;

import javafx.scene.paint.Color;

public record ColorProvider() {
  public final static Color DYING_COLOR = Color.rgb(50, 50, 50);
  public final static Color COLOR_PLANT = Color.rgb(7,123,5);
  public final static Color COLOR_FIRE = Color.rgb(255, 72, 5);
  public final static Color COLOR_ANIMAL = Color.rgb(255, 210, 126);
  public static final Color BORDER_DOMINANT_COLOR = Color.VIOLET;
  public static final Color BORDER_CHOSEN_COLOR = Color.RED;
  public static final Color BACKGROUND_COLOR = Color.rgb(103, 174, 10);
  public static final Color MAP_BORDER_COLOR = Color.rgb(153, 77, 26);
  public static final Color VERDANT_COLOR = Color.rgb(49,157,9);


  public ColorProvider {
    throw new UnsupportedOperationException("Config is a static utility and cannot be instantiated.");
  }
}
