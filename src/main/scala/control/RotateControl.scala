package control

import control.*
import game.*
import mathematics.*
import rendering.*

import scala.collection.mutable.Buffer

/**
 * Turret with rotation control around one axis. Uses the unit's UnitControlSystem to rotate on the azimuth axis.
 *
 * @param simulation
 *  The turret exists in this simulation
 * @param unit
 *  The unit that contains this turret
 * @param weapons
 *  Weapon of the turret
 * @param elevation
 *  The object that represents the elevation rotation axis
 * @param elevationLimits
 *  Limit the elevation axis in radians
 * @param turnSpeed
 *  Rotation speed
 */
class RotateControl(simulation: SimulationControl, unit: UnitObject, weapons: Buffer[WeaponObject], elevation: RenderObject, elevationLimits: Array[Double], turnSpeed: Double) extends TurretControlSystem(simulation, unit, weapons):
  var currentElevation = 0.0
  def update() =
    weapons.foreach(_.update())

    if(target != null) then
      val aimSolution = calculateAimVector(target.position, elevation)
      val aimVector = aimSolution._1

      //azimuth
      if(aimVector.x > 0) then
        unit.control.turnUnit(true)
      else
        unit.control.turnUnit(false)

      //elevation
      val el = turnSpeed * simulation.simulationTime
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
    end if
