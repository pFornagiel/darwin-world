package agh.ics.oop.presenter.statistics;

import agh.ics.oop.model.datacollectors.SimulationStatistics;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

public class StatisticsChartManager {
    private static final int MAX_DATA_POINTS = 100;
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
            xAxis.setLabel("Day");
            xAxis.setTickUnit(1);
            xAxis.setAutoRanging(false);
            xAxis.setForceZeroInRange(false);
            xAxis.setLowerBound(1);
            xAxis.setUpperBound(MAX_DATA_POINTS);
            yAxis.setLabel("Count");
            yAxis.setTickUnit(5);
            yAxis.setAutoRanging(true);
            yAxis.setForceZeroInRange(true);
            chart.getData().add(animalSeries);
            chart.getData().add(plantSeries);
            animalSeries.getNode().setStyle("-fx-stroke: #ff7f0e;");
            plantSeries.getNode().setStyle("-fx-stroke: #1f77b4;");
        });
    }

    public void updateChart(SimulationStatistics statistics) {
        if (statistics == null) return;

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
                animalSeries.getData().remove(0);
            }
            if (plantSeries.getData().size() > MAX_DATA_POINTS) {
                plantSeries.getData().remove(0);
            }

            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            int currentDay = statistics.amountOfDays();
            int lowerBound = Math.max(1, currentDay - MAX_DATA_POINTS + 1);
            xAxis.setLowerBound(lowerBound);
            xAxis.setUpperBound(Math.max(MAX_DATA_POINTS, currentDay + 1));
        });
    }
}