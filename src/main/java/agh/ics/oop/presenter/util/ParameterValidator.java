package agh.ics.oop.presenter.util;

public class ParameterValidator {

    private static final String MUST_BE_POSITIVE = " must be positive";
    private static final String MUST_BE_VALID_NUMBER = " must be a valid number";
    private static final String CANNOT_BE_NEGATIVE = " cannot be negative";
    private static final String MUTATIONS_ERROR = "Minimum mutations cannot be greater than maximum mutations.";
    private static final String BREED_ENERGY_ERROR = "Breed energy needed cannot be less than breed energy usage.";
    private static final String ENTITY_COUNT_ERROR = "Count cannot exceed map area.";
    private static final String DIMENSION_ERROR = "Dimensions must be between %d and %d.";
    private static final String GENES_COUNT_ERROR = "Genes count must be between %d and %d.";
    private static final String REFRESH_INTERVAL_ERROR = "Map refresh interval must be between %d and %d.";

    private static final int MIN_MAP_DIMENSION = 2;
    private static final int MAX_MAP_DIMENSION = 100;
    private static final int MIN_GENES_COUNT = 2;
    private static final int MAX_GENES_COUNT = 10;
    private static final int MIN_REFRESH_INTERVAL = 100;
    private static final int MAX_REFRESH_INTERVAL = 1000;

    public int validatePositiveInt(String value, String fieldName) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue <= 0) {
                throw new IllegalArgumentException(fieldName + MUST_BE_POSITIVE);
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + MUST_BE_VALID_NUMBER);
        }
    }

    public int validateNonNegativeInt(String value, String fieldName) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 0) {
                throw new IllegalArgumentException(fieldName + CANNOT_BE_NEGATIVE);
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + MUST_BE_VALID_NUMBER);
        }
    }

    public int validateInRange(String value, int min, int max, String fieldName) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < min || parsedValue > max) {
                throw new IllegalArgumentException(String.format("%s must be between %d and %d.", fieldName, min, max));
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + MUST_BE_VALID_NUMBER);
        }
    }

    public void validateEntityCount(int count, int width, int height, String fieldName) {
        if (count > width * height) {
            throw new IllegalArgumentException(String.format("%s %s", fieldName, ENTITY_COUNT_ERROR));
        }
    }

    public void validateMutations(int minMutations, int maxMutations) {
        if (minMutations > maxMutations) {
            throw new IllegalArgumentException(MUTATIONS_ERROR);
        }
    }

    public void validateBreedEnergy(int breedEnergyNeeded, int breedEnergyUsage) {
        if (breedEnergyNeeded < breedEnergyUsage) {
            throw new IllegalArgumentException(BREED_ENERGY_ERROR);
        }
    }

}