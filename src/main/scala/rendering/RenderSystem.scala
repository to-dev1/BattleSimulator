package rendering

import mathematics.*

/**
 * A rendering system that is used to render an object at a specific position and rotation.
 */
trait RenderSystem(var position: Vector3, var rotation: Quaternion):
  var worldPosition: Vector3 = position
  var worldRotation: Quaternion = rotation
  var scale: Double = 1.0
  
  def update(): Unit
