package spawner

import control.*
import game.*
import mathematics.*
import physics.*
import rendering.*
import spawner.*

import java.awt.Color
import scala.collection.mutable.Buffer

/**
 * Blueprint for a tank hull, builds a mesh using four vectors.
 *
 * @param data
 *  The input data string
 */
class HullBlueprint(data: String) extends Blueprint(data):
  def loadHull(renderingEngine: RenderingEngine, container: Option[RenderObject]): RenderObject =
    println("HULL: " + data)
    val m = Blueprint.stringToArray(data, '|')
    val mesh = Blueprint.generateTank(Vector3(m(0), m(1), m(2)), Vector3(m(3), m(4), m(5)), Vector3(m(6), m(7), m(8)), Vector3(m(9), m(10), m(11)), Color(m(12).toInt, m(13).toInt, m(14).toInt))
    val pos = Vector3(m(15), m(16), m(17))
    val r = Quaternion.constructFullQuaternion(m(18), m(19), m(20))
    val obj = RenderObject(pos, r, mesh, container, renderingEngine)
    container.foreach(_.subObjects += obj)
    //Return
    obj
