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
 * Blueprint for building a RotateControl TurretControlSystem.
 *
 * @param data
 *  The input data string
 */
class RotateBlueprint(data: String) extends Blueprint(data):
  def loadRotateTurret(simulation: SimulationControl, unit: UnitObject, elevation: RenderObject): RotateControl =
    println("ROTATE: " + data)
    val t = Blueprint.stringToArray(data, '|')
    RotateControl(simulation, unit, Buffer.empty, elevation, Array(t(0), t(1)), t(2))