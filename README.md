# Darwin World 🐌
A simulation project that models an ecosystem where creatures interact with their environment and evolve.
The simulation features customizable parameters, real-time statistics, and visual representation of the ecosystem.

The project is a part of Object Oriented Programming course at AGH University of Cracow, Computer Science.

## Authors
- Paweł Fornagiel [[pFornagiel]](https://github.com/pFornagiel)
- Adrian Krawczyk [[adriankrawczyk]](https://github.com/adriankrawczyk)


## About

This project is a simple simulation of a world where herbivores roam the map, searching for food to survive and reproduce. Over time, these creatures evolve into different species based on their genetic traits, which influence their movement and behaviour.

The world itself a rectangular grid divided into square tiles. Most of the map is covered by steppes with sparse vegetation, while some areas, around the center of the map, are dense jungles where plants grow faster. Plants appear randomly, with higher concentrations in jungles.

Each creature is a herbivore, characterised by its position, energy, direction and genotype.

One day of simulation consists of the following: 
- Creatures with not enough energy are removed from the map
- Creatures consume plants on their fields and reproduce based on their current energy
- Creatures move one tile in the direction they are facing
- Next gene of each creature activates, which determines their rotation
- Creatures rotate according to the genotype

The simulation is meant to show how these creatures change their genotype over time, adapting to their environment through natural selection. Enjoy watching evolution unfold!

<br/>
<p align="center"><img src="https://github.com/user-attachments/assets/15a089eb-8698-4c2f-b1dc-356678c5eece" style="width:800px" /></p>

## Features  

### Simulation Configuration  

Both creature and map behaviors are highly customizable. Users can tweak minor settings, such as plant growth rates, animal energy levels, and genotype mutation frequencies, or introduce more dramatic elements like forest fires, which spread each turn and add an extra layer of challenge for survival.

Parameters GUI includes functionalities such as:
- Parameter validation and error handling  
- Visual feedback for input validation
- Save/Load functionality for created configurations

<br/>

<p align="center"><img src="https://github.com/user-attachments/assets/b74c7aaa-a4a5-4f22-9259-7862513f0aef" style="width:800px" /></p>

### Ecosystem Real-Time Visualisation

The app provides a real-time visualization of roaming creatures and dynamic environmental changes. User can observe the simulation as it unfolds, with options to pause, restart or initiate a new simulation asynchronically, independently of the current one.

A comprehensive statistics panel provides deep insights into the ecosystem. Users can track individual creatures, monitor the most common genotypes, and analyze creature-to-plant ratio graphs to study long-term trends.

Additionally, to ensure optimal performance and visibility, based on the map size creatures are rendered either as sprites on larger maps and as single pixels on smaller ones.

<br/>

<p align="center"><img src="https://github.com/user-attachments/assets/28446f22-be21-4cd7-b298-4d6cbb187732" style="width:800px" /></p>

## Technical Requirements  
- Java 17 or higher  
- JavaFX  
- Gradle build system


<p align="center"><img src="https://github.com/user-attachments/assets/e582b6f1-0e13-4650-9241-9d0d0bf51abb" style="width:400px" /></p>




