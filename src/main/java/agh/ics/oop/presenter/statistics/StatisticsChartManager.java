package agh.ics.oop.presenter.statistics;

import agh.ics.oop.model.datacollectors.SimulationStatistics;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatisticsChartManager {
    private final LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> animalSeries;
    private XYChart.Series<Number, Number> plantSeries;
    private final Map<Integer, SimulationStatistics> statisticsMap;

    public StatisticsChartManager(LineChart<Number, Number> chart) {
        this.chart = chart;
        this.statisticsMap = new LinkedHashMap<>();
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
            xAxis.setUpperBound(2);
            yAxis.setLabel("Count");
            yAxis.setTickUnit(5);
            yAxis.setAutoRanging(true);
            yAxis.setForceZeroInRange(true);
            chart.getData().add(animalSeries);
            chart.getData().add(plantSeries);

            // Set the line colors for the series
            animalSeries.getNode().setStyle("-fx-stroke: #ff7f0e;");
            plantSeries.getNode().setStyle("-fx-stroke: #1f77b4;");
        });
    }

    public void updateChart(SimulationStatistics statistics) {
        if (statistics == null) return;

        Platform.runLater(() -> {
            statisticsMap.put(statistics.amountOfDays(), statistics);
            animalSeries.getData().clear();
            plantSeries.getData().clear();

            // Update x-axis bounds
            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            xAxis.setUpperBound(statistics.amountOfDays() + 1);

            for (SimulationStatistics stats : statisticsMap.values()) {
                XYChart.Data<Number, Number> animalData = new XYChart.Data<>(
                        stats.amountOfDays(),
                        stats.amountOfAnimals()
                );
                XYChart.Data<Number, Number> plantData = new XYChart.Data<>(
                        stats.amountOfDays(),
                        stats.amountOfPlants()
                );

                animalSeries.getData().add(animalData);
                plantSeries.getData().add(plantData);
            }
        });
    }

    public void reset() {
        Platform.runLater(() -> {
            statisticsMap.clear();
            initializeChart();
        });
    }
}