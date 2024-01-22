package mathematics

/**
 * The standard vector with three doubles and useful methods.
 *
 * @param x
 *  x component
 * @param y
 *  y component
 * @param z
 *  z component
 */
class Vector3(var x: Double, var y: Double, var z: Double):
  override def toString = "(" + x + ", " + y + ", " + z + ")"
  def +(v: Vector3): Vector3 =
    Vector3(x + v.x, y + v.y, z + v.z)
  def -(v: Vector3): Vector3 =
    Vector3(x - v.x, y - v.y, z - v.z)
  def *(a: Double): Vector3 =
    Vector3(x * a, y * a, z * a)
  def magnitude: Double =
    math.sqrt(x * x + y * y + z * z)

object Vector3:
  def normalize(v: Vector3): Vector3 =
    val l = math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z)
    v * (1.0 / l)

  def crossProduct(a: Vector3, b: Vector3): Vector3 =
    Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x)

  def dotProduct(a: Vector3, b: Vector3): Double =
    a.x * b.x + a.y * b.y + a.z * b.z

  def distance(a: Vector3, b: Vector3): Double =
    (b - a).magnitude

  def distanceSqr(a: Vector3, b: Vector3): Double =
    val v = b - a
    v.x * v.x + v.y * v.y + v.z * v.z
