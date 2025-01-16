package agh.ics.oop.model.configuration;

public record ConfigAnimal(
    int initialAnimalCount,
    int initialEnergy,
    int energyToReproduce,
    int energyConsumedByParents,
    int minMutations,
    int maxMutations,
    int genomeLength,
    BehaviorVariant behaviorVariant

) {
}
