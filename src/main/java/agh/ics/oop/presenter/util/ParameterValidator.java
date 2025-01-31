package agh.ics.oop.presenter.util;

public class ParameterValidator {

    private static final String MUST_BE_POSITIVE = " must be positive";
    private static final String MUST_BE_VALID_NUMBER = " must be a valid number";
    private static final String CANNOT_BE_NEGATIVE = " cannot be negative";
    private static final String MUTATIONS_ERROR = "Minimum mutations cannot be greater than maximum mutations.";
    private static final String BREED_ENERGY_ERROR = "Breed energy needed cannot be less than breed energy usage.";
    private static final String ENTITY_COUNT_ERROR = "Count cannot exceed map area.";

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