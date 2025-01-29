package agh.ics.oop.presenter.renderer;

import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.presenter.grid.GridRenderer;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ElementRenderer {

    private final GridRenderer gridRenderer;
    public ElementRenderer(GridRenderer gridRenderer) {
        this.gridRenderer = gridRenderer;
    }

    public void drawElements(Iterable<Vector2d> positions, Image image) {
        for (Vector2d position : positions) {
            gridRenderer.drawElement(image, position);
        }
    }

    public void drawColoredElements(Iterable<Vector2d> positions, Color color) {
        for (Vector2d position : positions) {
            gridRenderer.drawColor(color, position);
        }
    }
}