package control

import control.*
import game.*
import mathematics.*
import physics.*

/**
 * AI for controlling a ground unit.
 * These units should be able to stop, turn and accelerate quite quickly and have a low top speed for this AI system to function correctly.
 *
 * @param simulation
 *  The simulation this AI exists in
 * @param control
 *  The UnitControlSystem of the unit this AI is connected to
 * @param unit
 *  The unit that this AI is conneced to
 */
class GroundUnitAI(simulation: SimulationControl, control: UnitControlSystem, unit: UnitObject) extends UnitAI(simulation, control, unit):
  def update() =
    val target = findTarget()

    if(target != null)
      val targetRange = Vector3.distanceSqr(target.position, unit.position)

      val r = Quaternion.invertQuaternion(control.physicsObject.rotation)
      var targetVector = target.position - unit.position
      targetVector = Quaternion.rotateVectorByQuaternion(targetVector, r)

      if(targetRange > combatRange * combatRange) then
        control.moveUnit(true)

      if(targetRange < maxRange * maxRange) then
        unit.turrets.foreach(_.setTarget(target))
      else
        if(targetVector.x > 0) then
          control.turnUnit(true)
        else
          control.turnUnit(false)
    end if

  def findTarget(): UnitObject =
    var range = 100000000.0
    var target: UnitObject = null
    for(u <- simulation.units) do
      val dist = Vector3.distanceSqr(unit.position, u.position)
      if(u.team != unit.team && u != unit && dist < range) then
        target = u
        range = dist
    target

