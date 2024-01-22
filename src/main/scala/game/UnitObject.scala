package game

import control.*
import game.*
import mathematics.*
import physics.*

import scala.collection.mutable.Buffer

/**
 * A unit within the simulation that can move and fight other units. The AI operates the unit using the control system which in turn
 * uses the physics object to for example move forward.
 *
 * @param simulation
 *  The simulation containing this unit
 * @param ai
 *  UnitAI of the unit
 * @param control
 *  UnitControlSystem of the unit
 * @param stats
 *  Statistics about the unit such as health and armor
 * @param turrets
 *  Turrets connected to this unit
 * @param team
 *  The team the unit is on, units on any other teams are considered to be enemies
 */
class UnitObject(val simulation: SimulationControl, var ai: UnitAI, val control: UnitControlSystem, val stats: UnitStats, var turrets: Buffer[TurretControlSystem], var team: Int):
  var destroyed = false
  def update() =
    ai.update()
    control.update()
    turrets.foreach(_.update())
    if(control.destroyed) then destroy()
  def position = control.position
  def velocity = control.velocity

  def calculateDamage(damage: Double): (Double, Double) =
    val dmg = damage - stats.armor
    var dmgLeft = dmg
    if(dmg > 0) then
      if(stats.health >= dmgLeft) then dmgLeft = 0
      else dmgLeft = dmgLeft - stats.health
      stats.health -= dmg
      if(stats.health <= 0) then destroy()
    (dmg, if(damage == 0) then 0.0 else dmgLeft / damage)

  def destroy() =
    simulation.spawnExplosion(position, stats.explosion)
    destroyed = true

