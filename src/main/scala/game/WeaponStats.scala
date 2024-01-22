package game

import game.*
import mathematics.*

/**
 * Contains information about a weapon and determines its features.
 *
 * @param speed
 *  Speed of the bullet
 * @param kinetic
 *  Base kinetic damange of the bullet, can be thought of as the mass of the bullet
 * @param explosive
 *  Magnitude of the explosion caused by this bullet on impact
 * @param a
 *  The first vector of the mesh used by the bullet
 * @param b
 *  The second vector of the mesh used by the bullet
 * @param reload
 *  Reload time of the weapon in seconds
 * @param clipAmmo
 *  The weapon can fire this many times before needing a longer reload
 * @param clipReload
 *  Second reload time of the weapon in seconds
 * @param maxRange
 *  (Optional) limit for the maximum range of the weapon, 0 means no limit
 */
class WeaponStats(var speed: Double, var kinetic: Double, var explosive: Double, var a: Vector3, var b: Vector3, var reload: Double, var clipAmmo: Int, var clipReload: Double, var maxRange: Double)
