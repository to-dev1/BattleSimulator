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
 * Blueprint for a tank wheel.
 *
 * @param data
 *  The input data string
 */
class WheelBlueprint(data: String) extends Blueprint(data):
  def loadWheel(renderingEngine: RenderingEngine, container: Option[RenderObject]): (RenderObject, Double) =
    println("WHEEL: " + data)
    val m = Blueprint.stringToArray(data, '|')
    val mesh = Blueprint.generateWheel(m(0), m(1), Color(m(2).toInt, m(3).toInt, m(4).toInt), Color(m(5).toInt, m(6).toInt, m(7).toInt))
    val pos = Vector3(m(8), m(9), m(10))
    val r = Quaternion.constructFullQuaternion(m(11), m(12), m(13))
    val obj = RenderObject(pos, r, mesh, container, renderingEngine)
    container.foreach(_.subObjects += obj)
    //Return
    (obj, m(0))
