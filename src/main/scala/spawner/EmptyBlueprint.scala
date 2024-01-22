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
 * Blueprint that has a position and rotation but an empty mesh.
 *
 * @param data
 *  The input data string, only contains position and rotation
 */
class EmptyBlueprint(data: String) extends Blueprint(data):
  def loadEmpty(renderingEngine: RenderingEngine, container: Option[RenderObject]): RenderObject =
    println("EMPTY: " + data)
    val m = Blueprint.stringToArray(data, '|')
    val pos = Vector3(m(0), m(1), m(2))
    val r = Quaternion.constructFullQuaternion(m(3), m(4), m(5))
    val obj = RenderObject(pos, r, Mesh(Buffer.empty, Buffer.empty), container, renderingEngine)
    container.foreach(_.subObjects += obj)
    //Return
    obj
