package agh.ics.oop.model.datacollectors;

import java.io.*;
import java.nio.file.*;
import agh.ics.oop.model.worldelement.util.Genotype;

public class SimulationStatisticsCSVSaver {
    private final Path csvFilePath;
    private static final String HEADER = "day,animals,plants,freeFields,avgEnergy,avgLifespan,avgChildren,mostPopularGenotype";
    private int lastDay = -1;

    public SimulationStatisticsCSVSaver() {
        this.csvFilePath = Paths.get("simulation_stats.csv");
        initializeFile();
    }

    private void initializeFile() {
        try {
            Files.write(csvFilePath, (HEADER + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            System.err.println("Failed to initialize CSV file: " + e.getMessage());
        }
    }

    private String formatGenotype(Genotype genotype) {
        if (genotype == null) return "\"none\"";
        return "\"" + genotype.toString().replaceAll("\\s+", "") + "\"";
    }

    private String formatDecimal(double value) {
        return String.format("%.2f", value).replace('.', ',');
    }

    public void saveStatistics(SimulationStatistics stats) {
        try {
            if (stats.amountOfDays() == lastDay) {
                return;
            }

            lastDay = stats.amountOfDays();

            String mostPopularGenotype = stats.mostPopularGenotypes().isEmpty() ?
                    "\"none\"" :
                    formatGenotype(stats.mostPopularGenotypes().getFirst());

            String csvLine = String.format("%d,%d,%d,%d,%s,%s,%s,%s",
                    stats.amountOfDays(),
                    stats.amountOfAnimals(),
                    stats.amountOfPlants(),
                    stats.amountOfFreeFields(),
                    formatDecimal(stats.averageEnergy()),
                    formatDecimal(stats.averageLifespan()),
                    formatDecimal(stats.averageChildren()),
                    mostPopularGenotype
            );

            Files.write(
                    csvFilePath,
                    (csvLine + System.lineSeparator()).getBytes(),
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Failed to save statistics to CSV: " + e.getMessage());
        }
    }

    public Path getFilePath() {
        return csvFilePath;
    }
}