# BattleSimulator
A battle simulator with physics, CPU rendering, simple terrain generation, unit types and unit AI. The simulator has been developed with Scala and uses the Swing library for displaying the pixels on screen.

# Features
Physics
- force and torque calculations
- units move on the terrain due to the interaction between the unit's collision points and the ground
- mechs walk using the physics system instead of only a visual animation
  
Software renderer
- simple renderer based on rasterization
- runs on the CPU
- uses z-buffering to represent depth information

Environment
- simple terrains built with sine waves
- different gravity and atmosphere on different planets
- unit AI adapts to the provided environment

Units
- loaded from a custom text file format
- AI to handle moving the unit and using its weapons
- walkers and tanks

# Controls
Camera movement
W - forward
S - back
A - left
D - right
Q - up
E - down

Rotate camera with the arrow keys

R - teleport camera forward
T - slower time scale
Y - faster time scale
F - launch projectile from camera

# Files
You can create your own units with simple text files. Follow the instructions in the superheavy-mech.txt file which can be found in the folder data/blueprints/faction 1. Define the battle scenario in the battle.txt and select the planet by its index in planet.txt. The different planets can be found in the source file PlanetDatabase.

# Building the project
The build.sbt file is provided and has the correct scala and swing versions. Clone the project with git, open the project in for example intellij and try to start the program in the WindowSystem entry point. In error cases check the installed versions of scala and scala SDK and confirm that the swing library has been installed. The data folder which contains multiple default units is included in this repository. The program detects if data files are missing and prints information to console. If no units appear, check the console output and check if the data folder is correctly included in the project.

# Structure
The entry point to the program is the WindowSystem object in the WindowSystem file. The simulation is updated by the SimulationControl class, which also handles user input. The blueprint classes are a bridge between the text file format and actual simulated units. It enables creating complex units with multiple levels of subobjects.
