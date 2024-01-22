package physics
import mathematics.{Quaternion, Vector3}

/**
 * CollisionSystem that does nothing
 */
class BuildingCollision extends CollisionSystem:
  override def update(position: Vector3, velocity: Vector3, rotation: Quaternion, rotationRate: Quaternion, mass: Double): (Vector3, Vector3, Quaternion, Quaternion, Boolean) =
    //Do nothing
    (position, velocity, rotation, rotationRate, false)

