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
 * Blueprint for a weapon.
 *
 * @param data
 *  The input data string
 */
class WeaponBlueprint(data: String) extends Blueprint(data):
  def loadWeapon(simulation: SimulationControl, unit: UnitObject, weapon: RenderObject): WeaponObject =
    println("WEAPON: " + data)
    val t = Blueprint.stringToArray(data, '|')
    var maxRange: Double = 0
    t.lift(12).foreach(r => maxRange = r)
    WeaponObject(simulation, weapon, WeaponStats(t(0), t(1), t(2), Vector3(t(3), t(4), t(5)), Vector3(t(6), t(7), t(8)), t(9), t(10).toInt, t(11), maxRange), unit)