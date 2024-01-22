package mathematics

/**
 * Special vector class used for triangle rendering depth calculations.
 * The x and y components represent the pixel position on screen while the z component represents world space.
 *
 * @param x
 *  x component, an integer
 * @param y
 *  y component, an integer
 * @param z
 *  z component, a double
 */
class Vector2z(var x: Int, var y: Int, var z: Double):
  override def toString = "(" + x + ", " + y + " | " + z + ")"
  override def clone(): Vector2z = Vector2z(x, y ,z)