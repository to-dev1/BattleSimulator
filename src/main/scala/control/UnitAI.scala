package control

import control.*
import game.*
import mathematics.*
import physics.*

/**
 * UnitAI uses the UnitControlSystem to effectively operate the unit.
 */
trait UnitAI(val simulation: SimulationControl, val control: UnitControlSystem, var unit: UnitObject):
  var maxRange = calculateMaxRange
  var combatRange = maxRange - math.pow(maxRange * 0.8, 0.8)
  def update(): Unit
  def findTarget(): UnitObject
  def calculateMaxRange: Double =
    var maxRange = 0.0
    for(t <- unit.turrets) do
      val stats = t.weapons.head.stats
      val airResistance = simulation.calculateBulletAirResistance(stats.kinetic, stats.explosive)
      var range = stats.speed * 750.0 - math.pow(airResistance, 0.67) * 4000.0
      if(stats.maxRange != 0) range = math.min(stats.maxRange, range)
      maxRange = math.max(maxRange, range)
    maxRange
  def calculateMinRange: Double =
    var minRange = 100000000.0
    for(t <- unit.turrets) do
      val stats = t.weapons.head.stats
      val airResistance = simulation.calculateBulletAirResistance(stats.kinetic, stats.explosive)
      var range = stats.speed * 750.0 - math.pow(airResistance, 0.67) * 4000.0
      if(stats.maxRange != 0) range = math.min(stats.maxRange, range)
      minRange = math.min(minRange, range)
    minRange

