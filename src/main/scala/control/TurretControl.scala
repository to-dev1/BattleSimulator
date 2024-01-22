package control

import control.*
import game.*
import mathematics.*
import rendering.*

import scala.collection.mutable.Buffer

/**
 * Turret with rotation control on two axes.
 *
 * @param simulation
 *  The turret exists in this simulation
 * @param unit
 *  The unit that contains this turret
 * @param weapons
 *  Weapon of the turret
 * @param azimuth
 *  The object that represents the azimuth rotation axis
 * @param elevation
 *  The object that represents the elevation rotation axis, must be in the subobject tree of the azimuth object to function correctly
 * @param elevationLimits
 *  Limit the elevation axis in radians
 * @param turnSpeeds
 *  The rotation speeds around the axes, azimuth first, then elevation
 */
class TurretControl(simulation: SimulationControl, unit: UnitObject, weapons: Buffer[WeaponObject], azimuth: RenderObject, elevation: RenderObject, elevationLimits: Array[Double], turnSpeeds: Array[Double]) extends TurretControlSystem(simulation, unit, weapons):
  var currentElevation = 0.0
  def update() =
    weapons.foreach(_.update())

    if(target != null) then
      val aimSolution = calculateAimVector(target.position, elevation)
      val aimVector = aimSolution._1

      //azimuth
      val az = turnSpeeds(0) * simulation.simulationTime
      if(aimVector.x > 0) then
        azimuth.rotation = rotateTurret(az, Vector3(0, 1, 0), azimuth.rotation)
      else
        azimuth.rotation = rotateTurret(-az, Vector3(0, 1, 0), azimuth.rotation)

      //elevation
      val el = turnSpeeds(1) * simulation.simulationTime
      if(aimVector.y > 0 && currentElevation > elevationLimits(0)) then
        elevation.rotation = rotateTurret(-el, Vector3(1, 0, 0), elevation.rotation)
        currentElevation += -el
      else if(currentElevation < elevationLimits(1))
        elevation.rotation = rotateTurret(el, Vector3(1, 0, 0), elevation.rotation)
        currentElevation += el

      if(aimSolution._2) then
        if(math.abs(aimVector.x) < 0.5 && math.abs(aimVector.y) < 0.5) then weapons.foreach(_.fire())
      else
        unit.control.moveUnit(true)
        if(aimVector.x > 0) then
          unit.control.turnUnit(true)
        else
          unit.control.turnUnit(false)
    end if
