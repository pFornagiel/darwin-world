# Darwin World  
A simulation project that models an ecosystem where animals evolve and interact with their environment.  
The simulation features customizable parameters, real-time statistics, and visual representation of the ecosystem.  

## Features  

### Simulation Configuration Window  
![Bez tytułu](https://github.com/user-attachments/assets/2e1bac15-b2ff-4bab-895e-29a70d7ec4d4)
- Custom parameter input fields for simulation setup  
- Save/Load functionality for simulation configurations  
- Parameter validation and error handling  
- Quick access to saved configurations  
- Visual feedback for input validation  

### Main Simulation Window  
![screen](https://github.com/user-attachments/assets/28446f22-be21-4cd7-b298-4d6cbb187732)
- Real-time visualization of the ecosystem  
- Sprite-based rendering for smaller maps  
- Color-based rendering for larger maps  

### Controls for Simulation Flow  
- Start/Stop simulation  
- Start new simulation  
- Reset current simulation  

### Detailed Statistics Panel  
- Current day count  
- Number of plants  
- Free fields count  
- Top 3 genotypes  
- Average energy of animals  
- Average lifespan  
- Average number of children  

### Animal Tracking  
- Current energy level  
- Number of children  
- Number of descendants  
- Complete genome  
- Currently active gene  
- Day of death (if applicable)  
- Animal title/identifier  

### Map Variants  
- **Fire Map** - Features periodic fires that can spread and affect both plants and animals  
- **"A Bit of Madness" Behavior Model** - Animals follow their genetic code with 80% probability, and make random moves 20% of the time  

## Technical Requirements  
- Java 17 or higher  
- JavaFX  
- Gradle build system  
