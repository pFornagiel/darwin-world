package agh.ics.oop.presenter.statistics;

import agh.ics.oop.model.datacollectors.SimulationStatistics;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

public class StatisticsChartManager {
    private static final int MAX_DATA_POINTS = 100;
    private static final int X_AXIS_TICK_UNIT = 5;
    private static final int Y_AXIS_TICK_UNIT = 5;
    private static final int X_AXIS_LOWER_BOUND = 1;
    private static final String X_AXIS_LABEL = "Day";
    private static final String Y_AXIS_LABEL = "Count";
    private static final String ANIMAL_SERIES_STYLE = "-fx-stroke: #ff7f0e;";
    private static final String PLANT_SERIES_STYLE = "-fx-stroke: #67AE0A;";

    private final LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> animalSeries;
    private XYChart.Series<Number, Number> plantSeries;

    public StatisticsChartManager(LineChart<Number, Number> chart) {
        this.chart = chart;
        initializeChart();
    }

    private void initializeChart() {
        Platform.runLater(() -> {
            chart.getData().clear();
            animalSeries = new XYChart.Series<>();
            plantSeries = new XYChart.Series<>();
            chart.setAnimated(false);
            chart.setCreateSymbols(false);

            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            NumberAxis yAxis = (NumberAxis) chart.getYAxis();

            xAxis.setLabel(X_AXIS_LABEL);
            xAxis.setTickUnit(X_AXIS_TICK_UNIT);
            xAxis.setAutoRanging(false);
            xAxis.setForceZeroInRange(false);
            xAxis.setLowerBound(X_AXIS_LOWER_BOUND);
            xAxis.setUpperBound(MAX_DATA_POINTS);

            yAxis.setLabel(Y_AXIS_LABEL);
            yAxis.setTickUnit(Y_AXIS_TICK_UNIT);
            yAxis.setAutoRanging(true);
            yAxis.setForceZeroInRange(true);

            chart.getData().add(animalSeries);
            chart.getData().add(plantSeries);

            animalSeries.getNode().setStyle(ANIMAL_SERIES_STYLE);
            plantSeries.getNode().setStyle(PLANT_SERIES_STYLE);
        });
    }

    public void updateChart(SimulationStatistics statistics) {

        Platform.runLater(() -> {
            XYChart.Data<Number, Number> animalData = new XYChart.Data<>(
                    statistics.amountOfDays(),
                    statistics.amountOfAnimals()
            );
            XYChart.Data<Number, Number> plantData = new XYChart.Data<>(
                    statistics.amountOfDays(),
                    statistics.amountOfPlants()
            );

            animalSeries.getData().add(animalData);
            plantSeries.getData().add(plantData);

            if (animalSeries.getData().size() > MAX_DATA_POINTS) {
                animalSeries.getData().removeFirst();
            }
            if (plantSeries.getData().size() > MAX_DATA_POINTS) {
                plantSeries.getData().removeFirst();
            }

            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            int currentDay = statistics.amountOfDays();
            int lowerBound = Math.max(X_AXIS_LOWER_BOUND, currentDay - MAX_DATA_POINTS + 1);
            xAxis.setLowerBound(lowerBound);
            xAxis.setUpperBound(Math.max(MAX_DATA_POINTS, currentDay + 1));
        });
    }
}