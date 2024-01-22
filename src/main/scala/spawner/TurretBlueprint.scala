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
 * Blueprint for a TurretControlSystem with two axes of rotation, builds a TurretControl.
 *
 * @param data
 *  The input data string
 */
class TurretBlueprint(data: String) extends Blueprint(data):
  def loadTurret(simulation: SimulationControl, unit: UnitObject, azimuth: RenderObject, elevation: RenderObject): TurretControl =
    println("TURRET: " + data)
    val t = Blueprint.stringToArray(data, '|')
    TurretControl(simulation, unit, Buffer.empty, azimuth, elevation, Array(t(0), t(1)), Array(t(2), t(3)))