# BattleSimulator
A battle simulator with physics, CPU rendering, simple terrain generation, unit types and unit AI. The simulator has been developed with Scala and uses the Swing library for displaying the pixels on screen.

# Features
Physics
- units move on the terrain due to the interaction between the unit's collision points and the ground
- mechs walk using the physics system instead of only a visual animation
  
Software renderer
- simple renderer based on rasterization
- runs on the CPU

Environment
- simple terrains built with sine waves
- different gravity and atmosphere on different planets

Units
- loaded from a custom text file format
- AI to handle moving the unit and using its weapons

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
Create your own units with simple text files! Follow the instructions in the superheavy-mech.txt file which can be found in the folder data/blueprints/faction 1. Define the battle scenario in the battle.txt and select the planet by its index in planet.txt. The different planets can be found in the source file PlanetDatabase.

# Structure
The entry point to the program is the WindowSystem object in the WindowSystem file. The simulation is updated by the SimulationControl class, which also handles user input.
