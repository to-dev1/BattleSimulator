package control

import control.*
import game.*
import mathematics.*
import physics.*

/**
 * UnitControlSystem is used to control vehicles with various methods of movement.
 */
trait UnitControlSystem(val simulation: SimulationControl, val physicsObject: PhysicsObject, val collisionSystem: UnitCollision):
  var destroyed = false
  def update(): Unit
  //Movement
  def turnUnit(direction: Boolean): Unit
  def moveUnit(direction: Boolean): Unit
  //Info
  def position: Vector3
  def velocity: Vector3

