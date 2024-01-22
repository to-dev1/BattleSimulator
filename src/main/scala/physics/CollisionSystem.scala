package physics

import mathematics.*

/**
 * Collision systems enable physics objects to react to the environment.
 */
trait CollisionSystem:
  def update(position: Vector3, velocity: Vector3, rotation: Quaternion, rotationRate: Quaternion, mass: Double): (Vector3, Vector3, Quaternion, Quaternion, Boolean)
