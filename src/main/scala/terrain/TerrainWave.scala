package terrain

import mathematics.*
import terrain.*

/**
 * Settings used for generating the terrain.
 * Generation is done using multiple sine waves.
 *
 * @param mainWave
 *  Main wave frequency, small scale terrain changes
 * @param mainMulti
 *  Main wave multiplier
 * @param secondWave
 *  Secondary wave frequency, medium scale terrain changes
 * @param secondMulti
 *  Secondary wave multiplier
 * @param aWave
 *  A wave frequency, this is usually used for the overall shape of the terrain
 * @param aMulti
 *  A wave multiplier
 * @param bWave
 *  B wave frequency, this is used for mountains and craters
 * @param bMulti
 *  B wave multiplier
 * @param multiOffset
 *  The offset vector used for calculating the multipliers
 */
class TerrainWave(val mainWave: Int, val mainMulti: Double, val secondWave: Int, val secondMulti: Double, val aWave: Int, val aMulti: Double, val bWave: Int, val bMulti: Double, val multiOffset: Vector3)
