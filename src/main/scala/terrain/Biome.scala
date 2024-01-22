package terrain

import mathematics.*
import terrain.*

import java.awt.Color

/**
 * Defines the color of the planet's surface.
 *
 * @param mainColor
 *  The base color used by this biome
 * @param colorAreas
 *  The rate of change in rgb, larger values lead to more frequent color change in the terrain
 * @param colorAmount
 *  Multiply the change by this amount
 */
class Biome(val mainColor: Color, val colorAreas: Integer3, val colorAmount: Int)
