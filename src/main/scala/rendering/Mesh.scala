package rendering

import scala.collection.mutable.Buffer
import scala.swing.Color

import mathematics.Vector3
import rendering.Triangle

/**
 * A Mesh is a collection of points and triangles that moves and rotates as one object and is drawn to the screen by the RenderingEngine.
 *
 * @param points
 *  The vertices of this mesh
 * @param triangles
 *  The triangles of the mesh, they contain the indices of the three vertices of this mesh that define the triangle
 */
class Mesh(var points: Buffer[Vector3], var triangles: Buffer[Triangle]):
  /*
   --->
  d    a

  c    b
   <---

  a, b, c
  c, d, a
  */
  def addQuad(a: Vector3, b: Vector3, c: Vector3, d: Vector3, color: Color) =
    val index = points.length
    points += a
    points += b
    points += c
    points += d
    triangles += Triangle(index, index + 1, index + 2, color)
    triangles += Triangle(index + 2, index + 3, index, color)

  def addTri(a: Vector3, b: Vector3, c: Vector3, color: Color) =
    val index = points.length
    points += a
    points += b
    points += c
    triangles += Triangle(index, index + 1, index + 2, color)
