package physics

import game.SimulationControl
import mathematics.*
import rendering.RenderObject

/**
 * CollisionSystem for units. Uses collision points to detect collisions with the terrain and calculates force and torque based on them.
 * Also has the possibility of slowing down or moving the unit with friction.
 *
 * @param simulation
 *  The simulation of this CollisionSystem
 * @param points
 *  The collision points, each point has its location, the RenderObject the point is connected to (can be a subobject) and the friction of the point
 */
class UnitCollision(simulation: SimulationControl, points: Array[(Vector3, RenderObject, Double)]) extends CollisionSystem:
  var drivePoints = 0
  var contactPoints = 0
  val lastPointPositions: Array[Vector3] = Array.fill(points.length)(Vector3(0, 0, 0))
  val pointVelocity: Array[Vector3] = Array.fill(points.length)(Vector3(0, 0, 0))

  override def update(position: Vector3, velocity: Vector3, rotation: Quaternion, rotationRate: Quaternion, mass: Double): (Vector3, Vector3, Quaternion, Quaternion, Boolean) =
    var vel = velocity + Vector3(0, -simulation.gravity, 0) * simulation.simulationTime
    var newRotation = rotation
    var turn = rotationRate

    //The + 0.01 used in this file are to avoid division by zero
    val massMulti = 3.0 / (mass + 0.01)

    val dir = Quaternion.rotateVectorByQuaternion(Vector3(0, 0, 1), rotation) * simulation.simulationTime
    drivePoints = 0
    contactPoints = 0

    var index = 0
    for(i <- points) do
      val rVec = RenderObject.getPosition(i._1, i._2)
      val r = Quaternion.rotateVectorByQuaternion(rVec, rotation)
      val p = r + position

      val last = lastPointPositions(index)
      if(last != Vector3(0, 0, 0)) then
        pointVelocity(index) = p - last

      lastPointPositions(index) = p

      //Collision
      val h = simulation.terrain.getHeight(p)
      val d = p.y - h - simulation.terrain.position.y
      if(d < 0) then
        contactPoints += 1
      if(d < 0.1) then
        drivePoints += 1
      index += 1
    end for

    val torqueMultiplier = 1.0 / (contactPoints + 0.01) * 0.1
    val driveMultiplier = 1.0 / (drivePoints + 0.01) * 0.1

    index = 0
    for(i <- points) do
      val rVec = RenderObject.getPosition(i._1, i._2)
      val r = Quaternion.rotateVectorByQuaternion(rVec, rotation)
      val p = r + position

      val pointVel = pointVelocity(index)
      val velMulti = math.abs(pointVel.y) + 0.2
      val friction = i._3

      //Collision
      val h = simulation.terrain.getHeight(p)
      val d = p.y - h - simulation.terrain.position.y
      if(d < 0) then
        //Torque
        val tx = Quaternion.constructQuaternion(-rVec.z * 0.2 * torqueMultiplier * velMulti * simulation.simulationTime, Vector3(1, 0, 0))
        val tz = Quaternion.constructQuaternion(rVec.x * 0.2 * torqueMultiplier * velMulti * simulation.simulationTime, Vector3(0, 0, 1))
        val rx = Quaternion.constructQuaternion(-rVec.z * (1 - d) * 10.0 * torqueMultiplier * massMulti * velMulti * simulation.simulationTime, Vector3(1, 0, 0))
        val rz = Quaternion.constructQuaternion(rVec.x * (1 - d) * 10.0 * torqueMultiplier * massMulti * velMulti * simulation.simulationTime, Vector3(0, 0, 1))

        newRotation = Quaternion.multiplyQuaternions(Quaternion.rotateByQuaternion(rx, newRotation), newRotation)
        newRotation = Quaternion.multiplyQuaternions(Quaternion.rotateByQuaternion(rz, newRotation), newRotation)

        //Force
        val yVel = math.max(velocity.y, 0.01)
        vel = Vector3(velocity.x, math.max(velocity.y, -0.01), velocity.z)

        var groundRepulsion = 1.0
        if(d < -0.1) then groundRepulsion = 3.0

        //Repulsion
        vel += Vector3(0, groundRepulsion * torqueMultiplier * velMulti * 5.0, 0) * simulation.simulationTime
        vel = Vector3(vel.x, vel.y - vel.y * 20.0 * simulation.simulationTime * torqueMultiplier, vel.z)
      if(d < 0.1) then
        //Friction
        vel += Vector3(-pointVel.x * friction * driveMultiplier, 0, -pointVel.z * friction * driveMultiplier) * simulation.simulationTime

      index += 1
    end for

    Quaternion.normalizeQuaternion(turn)
    Quaternion.normalizeQuaternion(newRotation)

    (position, vel, newRotation, turn, false)