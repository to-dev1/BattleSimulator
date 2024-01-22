package control

import control.*
import game.*
import mathematics.*
import physics.*

/**
 * Control system for a tank, it uses the collision points of the wheels to move.
 *
 * @param simulation
 *  The simulation containing this tank
 * @param phys
 *  Physics object
 * @param coll
 *  Collision system
 */
class TankControl(simulation: SimulationControl, phys: PhysicsObject, coll: UnitCollision) extends UnitControlSystem(simulation, phys, coll):
  var moving = 0
  var speedMulti = 1.0
  var rotateMulti = 1.0 / math.sqrt(phys.mass)
  def position = phys.position
  def velocity = phys.velocity
  def update() =
    phys.update()
    destroyed = phys.destroyed

    if(moving == 1) then
      if(coll.contactPoints > 1.0) then
        val dir = Quaternion.rotateVectorByQuaternion(Vector3(0, 0, 1), phys.rotation) * simulation.simulationTime
        phys.velocity += dir * coll.drivePoints * 0.004 * speedMulti
    end if
    moving = 0

  def turnUnit(direction: Boolean) =
    var a = -0.1
    if(direction) then a = 0.1
    val r = Quaternion.constructQuaternion(a * simulation.simulationTime * coll.contactPoints * rotateMulti, Vector3(0, 1, 0))
    phys.rotation = Quaternion.multiplyQuaternions(Quaternion.rotateByQuaternion(r, phys.rotation), phys.rotation)

  def moveUnit(direction: Boolean) =
    moving = -1
    if(direction) moving = 1



