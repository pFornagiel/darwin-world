package agh.ics.oop.presenter;
import agh.ics.oop.presenter.SimulationConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SimulationConfigManager {
    private static final String CONFIG_FILE = "config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void saveConfig(SimulationConfig config) {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(config, writer);
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }
    public static SimulationConfig loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return gson.fromJson(reader, SimulationConfig.class);
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
            return null;
        }
    }
}
