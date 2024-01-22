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
 * The box blueprint builds a mesh using two vectors.
 *
 * @param data
 *  The input data string
 */
class BoxBlueprint(data: String) extends Blueprint(data):
  def loadBox(renderingEngine: RenderingEngine, container: Option[RenderObject]): RenderObject =
    println("BOX: " + data)
    val m = Blueprint.stringToArray(data, '|')
    val mesh = Blueprint.generateBox(Vector3(m(0), m(1), m(2)), Vector3(m(3), m(4), m(5)), Color(m(6).toInt, m(7).toInt, m(8).toInt))
    val pos = Vector3(m(9), m(10), m(11))
    val r = Quaternion.constructFullQuaternion(m(12), m(13), m(14))
    val obj = RenderObject(pos, r, mesh, container, renderingEngine)
    container.foreach(_.subObjects += obj)
    //Return
    obj