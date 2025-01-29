package agh.ics.oop.presenter.util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class SimulationConfigManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveConfig(SimulationConfig config, Path configFilePath) {
        try (FileWriter writer = new FileWriter(configFilePath.toFile())) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static SimulationConfig loadConfig(Path configFilePath) {
        try (FileReader reader = new FileReader(configFilePath.toFile())) {
            return gson.fromJson(reader, SimulationConfig.class);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}