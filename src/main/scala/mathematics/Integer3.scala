package mathematics

/**
 * A vector of three integers.
 *
 * @param x
 *  x component
 * @param y
 *  y component
 * @param z
 *  z component
 */
class Integer3(var x: Int, var y: Int, var z: Int):
  override def toString = "(" + x + ", " + y + ", " + z + ")"
  def +(v: Integer3): Integer3 =
    Integer3(x + v.x, y + v.y, z + v.z)
  def *(a: Int): Integer3 =
    Integer3(x * a, y * a, z * a)