package game

import game.*
import mathematics.*
import rendering.*

/**
 * A weapon that can spawn a bullet into the simulation
 *
 * @param simulation
 *  Simulation that contains this weapon
 * @param weapon
 *  The RenderObject that is considered to be the location of this weapon
 * @param stats
 *  Information about the weapon
 * @param unit
 *  The unit that contains this weapon
 */
class WeaponObject(val simulation: SimulationControl, val weapon: RenderObject, val stats: WeaponStats, val unit: UnitObject):
  var nextFire = 0.0
  var nextClip = 0.0
  var clipAmmoLeft = stats.clipAmmo
  def fire() =
    if(nextClip <= 0 && clipAmmoLeft == 0)
      clipAmmoLeft = stats.clipAmmo
    if(nextFire < 0 && clipAmmoLeft > 0) then
      nextFire = stats.reload
      simulation.spawnBullet(weapon.worldPosition, weapon.worldRotation, stats.speed, stats.kinetic, stats.explosive, stats.a, stats.b, unit)
      clipAmmoLeft -= 1
      if(clipAmmoLeft == 0) then nextClip = stats.clipReload
    end if

  def update() =
    nextFire -= simulation.simulationTime
    nextClip -= simulation.simulationTime

