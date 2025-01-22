package agh.ics.oop.presenter.renderer;
import agh.ics.oop.presenter.grid.GridManager;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
public class GridRenderer {
    private final GridPane gridPane;
    private final GridManager gridManager;

    public GridRenderer(GridPane gridPane, GridManager gridManager) {
        this.gridPane = gridPane;
        this.gridManager = gridManager;
    }

    public void setGridCell(int xPosition, int yPosition, Color color) {
        double cellSize = gridManager.calculateCellSize();
        Pane cell = new Pane();
        cell.setMinSize(cellSize, cellSize);
        cell.setPrefSize(cellSize, cellSize);
        cell.setMaxSize(cellSize, cellSize);

        BackgroundFill backgroundFill = new BackgroundFill(color, null, null);
        Background background = new Background(backgroundFill);
        cell.setBackground(background);

        BorderStroke borderStroke = new BorderStroke(
                Color.GRAY,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0.5)
        );
        Border border = new Border(borderStroke);
        cell.setBorder(border);

        gridPane.add(cell, xPosition, yPosition);
        GridPane.setHalignment(cell, HPos.CENTER);
        GridPane.setValignment(cell, VPos.CENTER);
    }

}