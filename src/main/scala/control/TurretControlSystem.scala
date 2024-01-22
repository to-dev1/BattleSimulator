package control

import control.*
import game.*
import mathematics.*
import physics.*
import rendering.RenderObject

import scala.collection.mutable.Buffer

/**
 * Turrets contain weapons and are a part of a unit.
 */
trait TurretControlSystem(val simulation: SimulationControl, val unit: UnitObject, val weapons: Buffer[WeaponObject]):
  var target: UnitObject = null
  var airResistance = 0.0
  def update(): Unit
  def calculateAimVector(target: Vector3, weapon: RenderObject): (Vector3, Boolean) =
    airResistance = simulation.calculateWeaponAirResistance(weapons.head)
    val r = Quaternion.invertQuaternion(weapon.worldRotation)
    val trajectory = simulation.solveTrajectoryVector(weapons.head.stats.speed * 10.0, weapon.worldPosition, target, airResistance)
    val targetVector = trajectory._1 - weapon.worldPosition
    var inRange = true
    val maxRange = weapons.head.stats.maxRange
    if(maxRange != 0)
      val dist = Vector3.distanceSqr(weapon.worldPosition, target)
      inRange = dist < maxRange * maxRange
    (Quaternion.rotateVectorByQuaternion(targetVector, r), trajectory._2 && inRange)
  def rotateTurret(a: Double, v: Vector3, q: Quaternion) =
    Quaternion.multiplyQuaternions(Quaternion.rotateByQuaternion(Quaternion.constructQuaternion(a, v), q), q)
  def setTarget(t: UnitObject) =
    target = t