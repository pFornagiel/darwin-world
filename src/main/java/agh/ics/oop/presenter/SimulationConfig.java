package agh.ics.oop.presenter;

public class SimulationConfig {
    public String mapWidth;
    public String mapHeight;
    public String plantCount;
    public String plantEnergy;
    public String animalCount;
    public String animalEnergy;
    public String breedEnergyNeeded;
    public String breedEnergyUsage;
    public String minMutations;
    public String maxMutations;
    public String genesCount;
    public boolean fireMap;
    public boolean insanity;

    public SimulationConfig(String mapWidth, String mapHeight, String plantCount, String plantEnergy,
                            String animalCount, String animalEnergy, String breedEnergyNeeded,
                            String breedEnergyUsage, String minMutations, String maxMutations,
                            String genesCount, boolean fireMap, boolean insanity) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.plantCount = plantCount;
        this.plantEnergy = plantEnergy;
        this.animalCount = animalCount;
        this.animalEnergy = animalEnergy;
        this.breedEnergyNeeded = breedEnergyNeeded;
        this.breedEnergyUsage = breedEnergyUsage;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genesCount = genesCount;
        this.fireMap = fireMap;
        this.insanity = insanity;
    }
}
