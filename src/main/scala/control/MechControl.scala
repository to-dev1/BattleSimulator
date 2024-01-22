package control

import control.*
import game.*
import mathematics.*
import physics.*
import rendering.*

import scala.collection.mutable.Buffer

/**
 * Mechs walk on two legs using four joints. The mech is moved forward when the ground prevents the mech's leg
 * from moving due to friction and as a reaction causes the hull to move.
 *
 * @param simulation
 *  The simulation containing this mech
 * @param phys
 *  Physics object
 * @param coll
 *  Collision system
 * @param joints
 *  The four joints of this mech
 */
class MechControl(simulation: SimulationControl, phys: PhysicsObject, coll: UnitCollision, joints: Array[RenderObject]) extends UnitControlSystem(simulation, phys, coll):
  var walkPhase = 0
  var timeMulti = 1.0
  var speedMulti = 1.5
  var angleMulti = 1.0

  var moving = 0

  val jointAngles: Array[Double] = Array.fill(joints.length)(0)
  var jointTargetAngles: Array[Buffer[Double]] = Array.fill(joints.length)(Buffer.empty)
  val jointSpeeds: Array[Double] = Array.fill(joints.length)(1.0)
  var jointsReady: Array[Boolean] = Array.fill(joints.length)(false)

  def position = phys.position
  def velocity = phys.velocity
  def update() =
    phys.update()
    destroyed = phys.destroyed
    val r = phys.rotation

    if(phys.mass > 100.0) then angleMulti = 0.4

    //update
    rotateJoints()
    if(jointsReady.forall(j => j)) then
      walkPhase += 1
      if(walkPhase == 0) then
        addTarget(0, -1.0, 1.0)
        addTarget(1, 1.5, 2.0)
        addTarget(1, 1.0, 2.0)
        addTarget(2, 0.5, 1.0)
        addTarget(3, 0.0, 1.0)
      else if(walkPhase == 1) then
        addTarget(0, 0.5, 1.0)
        addTarget(1, 0.0, 1.0)
        addTarget(2, -1.0, 1.0)
        addTarget(3, 1.5, 2.0)
        addTarget(3, 1.0, 2.0)
        if(moving == 1) then
          walkPhase = -1
        else
          walkPhase = 2
      else if(walkPhase > 1) then
        addTarget(0, 0, 1.0)
        addTarget(1, 0, 1.0)
        addTarget(2, 0, 1.0)
        addTarget(3, 0, 1.0)
        if(moving == 1) then
          walkPhase = -1

    moving = 0

    def moveJoint(index: Int, amount: Double) =
      joints(index).rotation = Quaternion.multiplyQuaternions(Quaternion.constructFullQuaternion(simulation.simulationTime * speedMulti * amount, 0, 0), joints(index).rotation)
      jointAngles(index) += simulation.simulationTime * speedMulti * amount

    def addTarget(index: Int, target: Double, speed: Double) =
      jointTargetAngles(index) += target * angleMulti
      jointSpeeds(index) = speed

    def rotateJoints() =
      for(i <- joints.indices) do
        jointsReady(i) = true
        if(jointTargetAngles(i).nonEmpty) then
          jointsReady(i) = false
          val a = jointAngles(i)
          val t = jointTargetAngles(i).head
          val s = jointSpeeds(i)
          if(t > a) then
            moveJoint(i, s)
          else
            moveJoint(i, -s)
          if(math.abs(t - a) < 0.04 * speedMulti) then jointTargetAngles(i).remove(0)
        end if
  end update

  def turnUnit(direction: Boolean) =
    var a = -0.5
    if(direction) then a = 0.5
    val r = Quaternion.constructQuaternion(a * simulation.simulationTime * speedMulti * 0.7, Vector3(0, 1, 0))
    phys.rotation = Quaternion.multiplyQuaternions(Quaternion.rotateByQuaternion(r, phys.rotation), phys.rotation)

  def moveUnit(direction: Boolean) =
    moving = -1
    if(direction) moving = 1


