package physics

import game.SimulationControl
import mathematics.*
import rendering.RenderSystem

/**
 * The central component of the entire simulation system which connects the rendering system to the higher level control systems.
 *
 * @param position
 *  position of the object, a Vector3
 * @param velocity
 *  velocity of the object, a Vector3
 * @param rotation
 *  rotation of the object, a Quaterion
 * @param mass
 *  mass of the object, a Double
 * @param collisionSystem
 *  the used CollisionSystem
 * @param renderSystem
 *  the connected RenderSystem
 * @param simulation
 *  a reference to the SimulationControl containing this object
 */
class PhysicsObject(var position: Vector3, var velocity: Vector3, var rotation: Quaternion, var mass: Double, var collisionSystem: CollisionSystem, var renderSystem: RenderSystem, val simulation: SimulationControl):
  var rotationRate = Quaternion(1, 0, 0, 0)
  var destroyed = false

  def update() =
    //Move object
    position += velocity * simulation.simulationTime * 100.0
    rotation = Quaternion.multiplyQuaternions(rotationRate, rotation)

    //Detect collisions and apply effect
    val col = collisionSystem.update(position, velocity, rotation, rotationRate, mass)
    position = col._1
    velocity = col._2
    rotation = col._3
    rotationRate = col._4
    destroyed = col._5

    //Apply changes to rendering and update
    renderSystem.worldPosition = position
    renderSystem.worldRotation = rotation
    renderSystem.update()

