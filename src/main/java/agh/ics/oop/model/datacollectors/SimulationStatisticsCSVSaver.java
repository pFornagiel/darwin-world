package agh.ics.oop.model.datacollectors;

import java.io.*;
import java.nio.file.*;
import agh.ics.oop.model.worldelement.util.Genotype;

/**
 * A class responsible for saving the simulation statistics into a CSV file. It writes data such as
 * the number of animals, plants, free fields, and other relevant statistics (e.g., average energy,
 * lifespan, children, and the most popular genotype) to a CSV file on each simulation day.
 */
public class SimulationStatisticsCSVSaver {
    private final Path csvFilePath;
    private static final String HEADER = "day;animals;plants;freeFields;avgEnergy;avgLifespan;avgChildren;mostPopularGenotype";
    private int lastDay = -1;

    public SimulationStatisticsCSVSaver(int hash) {
        this.csvFilePath = Paths.get(String.format("simulation_csv_%d.csv", hash));
        initializeFile();
    }

    private void initializeFile() {
        try {
            Files.write(csvFilePath, (HEADER + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatGenotype(Genotype genotype) {
        if (genotype == null) return "\"none\"";
        return "\"" + genotype.toString().replaceAll("\\s+", "") + "\"";
    }

    private String formatDecimal(double value) {
        return String.format("%.2f", value);
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

            String csvLine = String.format("%d;%d;%d;%d;%s;%s;%s;%s",
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
            e.printStackTrace();
        }
    }

}