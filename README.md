# Darwin World 🐌
A simulation project that models an ecosystem where animals evolve and interact with their environment.
The simulation features customizable parameters, real-time statistics, and visual representation of the ecosystem.

The project is a part of Object Oriented Programming course at AGH University of Cracow, Computer Science.

## Authors
- Paweł Fornagiel [[pFornagiel]](https://github.com/pFornagiel)
- Adrian Krawczyk [[adriankrawczyk]](https://github.com/adriankrawczyk)

## About

This project is a simple simulation of a world where herbivores roam the map, searching for food to survive and reproduce. Over time, these creatures evolve into different species based on their genetic traits, which influences their behaviour and movement.

The world itself a rectangular grid divided into square tiles. Most of the map is covered by steppes with sparse vegetation, while some areas, around the center of the map, are dense jungles where plants grow faster. Plants appear randomly, with higher concentrations in jungles.

Each creature is a herbivore, which is characterised by its position, energy, direction and genotype affecting behaviour.

One day of simulation consists of the following: 
- At the begging, creatures with not enough energy are removed from the map
- Creatures consume plants on their fields and reproduce
- After that, creatures move one tile in the direction they are facing
- After move, next gene of creature activates, which determines their rotation, and creatures rotate according to the genotype

The simulation is meant to show how these creatures evolve over time, adapting to their environment through natural selection. Enjoy watching evolution unfold!

![second](https://github.com/user-attachments/assets/15a089eb-8698-4c2f-b1dc-356678c5eece)

## Features  

### Simulation Configuration  
- Custom parameter input fields for simulation setup  
- Save/Load functionality for simulation configurations  
- Parameter validation and error handling  
- Visual feedback for input validation

![Main-page](https://github.com/user-attachments/assets/b74c7aaa-a4a5-4f22-9259-7862513f0aef)

### Ecosystem Real-Time Visualisation

- Sprite-based rendering for smaller maps  
- Color-based rendering for larger maps
- Detailed statistics panel
- Simulation flow controls
- Optional statistics logging
- Animal tracking
- Map and animal behaviour variations
 
![screen](https://github.com/user-attachments/assets/28446f22-be21-4cd7-b298-4d6cbb187732)

## Technical Requirements  
- Java 17 or higher  
- JavaFX  
- Gradle build system

![presentation](https://github.com/user-attachments/assets/e582b6f1-0e13-4650-9241-9d0d0bf51abb)


