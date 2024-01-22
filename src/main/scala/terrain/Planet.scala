package terrain

import mathematics.*
import terrain.*

import java.awt.Color

/**
 * Defines a planet with the specified features.
 * The selected planet can change the simulation by a lot.
 *
 * @param gravity
 *  The gravity on this planet, a good default value is 0.35, which with the game's scale should be about 17.5 m/s^2
 * @param airResistance
 *  Air resistance multiplier of the planet, has a very large effect on low mass projectiles
 * @param atmosphereColor
 *  The background color used in the RenderingEngine with this planet
 * @param biome
 *  The color of the planet's terrain
 * @param terrainWave
 *  The settings that define the generation of the planet's terrain using sine waves
 */
class Planet(val gravity: Double, val airResistance: Double, val atmosphereColor: Color, val biome: Biome, val terrainWave: TerrainWave)
