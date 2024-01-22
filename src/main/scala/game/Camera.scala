package game

import game.*
import mathematics.*
import physics.*
import rendering.*

import scala.swing.event.*

/**
 * The Camera class enables the player to move and rotate the area being rendered.
*/
class Camera(var position: Vector3, var angles: Vector3, val simulation: SimulationControl):
  var speed = 100.0
  var turnSpeed = 2.0
  var rotation: Quaternion = Quaternion(1, 0, 0, 0)

  def moveCamera(v: Vector3) =
    position += Quaternion.rotateVectorByQuaternion(v, rotation) * speed * simulation.deltaTime

  def moveCameraBy(v: Vector3, dist: Double) =
    position += Quaternion.rotateVectorByQuaternion(v, rotation) * dist

  def rotateCamera(a: Double, v: Vector3) =
    angles += v * a * turnSpeed * simulation.deltaTime

    val x = Quaternion.constructQuaternion(angles.x, Vector3(1, 0, 0))
    val y = Quaternion.constructQuaternion(angles.y, Vector3(0, 1, 0))

    rotation = Quaternion.multiplyQuaternions(y, x)