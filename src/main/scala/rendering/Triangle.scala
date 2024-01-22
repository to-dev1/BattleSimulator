package rendering

import scala.swing.Color

/**
 * A triangle is defined by three vertices and has a color used for rendering.
 *
 * @param a
 *  Index of the first vertex
 * @param b
 *  Index of the third vertex
 * @param c
 *  Index of the second vertex
 * @param color
 *  The color of the triangle
 */
class Triangle(var a: Int, var b: Int, var c: Int, var color: Color)
