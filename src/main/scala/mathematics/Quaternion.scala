package mathematics

import scala.math.*

import mathematics.Vector3

/**
 * A mathematical object to represent three dimensional rotations.
 *
 * @param w
 *  w component
 * @param x
 *  x component
 * @param y
 *  y component
 * @param z
 *  z component
 */
class Quaternion(var w: Double, var x: Double, var y: Double, var z: Double):
  override def toString = "(" + w + ", " + x + ", " + y + ", " + z + ")"
  def magnitude: Double = math.sqrt(w * w + x * x + y * y + z * z)

/**
 * Companion object of the Quaternion class with useful methods for building and modifying Quaternions.
 */
object Quaternion:
  def multiplyQuaternions(a: Quaternion, b: Quaternion): Quaternion =
    val w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z
    val x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y
    val y = a.w * b.y + a.y * b.w + a.z * b.x - a.x * b.z
    val z = a.w * b.z + a.z * b.w + a.x * b.y - a.y * b.x
    Quaternion(w, x, y, z)

  def multiplyVectorByQuaternion(a: Vector3, b: Quaternion) =
    val v = multiplyQuaternions(Quaternion(0, a.x, a.y, a.z), b)
    Vector3(v.x, v.y, v.z)

  def invertQuaternion(q: Quaternion): Quaternion =
    val m = q.magnitude * q.magnitude
    val w = q.w / m
    val x = -q.x / m
    val y = -q.y / m
    val z = -q.z / m
    Quaternion(w, x, y, z)

  def normalizeQuaternion(q: Quaternion): Quaternion =
    val m = 1.0 / q.magnitude
    val w = q.w * m
    val x = q.x * m
    val y = q.y * m
    val z = q.z * m
    Quaternion(w, x, y, z)

  def constructQuaternion(a: Double, v: Vector3): Quaternion =
    val w = math.cos(0.5 * a)
    val x = math.sin(0.5 * a) * v.x
    val y = math.sin(0.5 * a) * v.y
    val z = math.sin(0.5 * a) * v.z
    Quaternion(w, x, y, z)

  def constructFullQuaternion(a: Double, b: Double, c: Double): Quaternion =
    multiplyQuaternions(constructQuaternion(c, Vector3(0, 0, 1)), multiplyQuaternions(constructQuaternion(b, Vector3(0, 1, 0)), constructQuaternion(a, Vector3(1, 0, 0))))

  def fromVectors(a: Vector3, b: Vector3): Quaternion =
    val v = Vector3.crossProduct(a, b)
    Quaternion(math.sqrt(a.magnitude * a.magnitude + b.magnitude * b.magnitude) + Vector3.dotProduct(a, b), v.x, v.y, v.z)

  def calculateRotation(angle: Double, unitVector: Vector3, q: Quaternion): Quaternion =
    val r = constructQuaternion(angle, unitVector)
    multiplyQuaternions(multiplyQuaternions(multiplyQuaternions(q, r), invertQuaternion(q)), q)

  def rotateByQuaternion(v: Quaternion, q: Quaternion): Quaternion =
    multiplyQuaternions(multiplyQuaternions(q, v), invertQuaternion(q))

  def rotateVectorByQuaternion(v: Vector3, q: Quaternion): Vector3 =
    val r = multiplyQuaternions(multiplyQuaternions(q, Quaternion(0, v.x, v.y, v.z)), invertQuaternion(q))
    Vector3(r.x, r.y, r.z)


