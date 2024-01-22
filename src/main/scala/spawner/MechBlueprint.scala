package spawner

import control.*
import game.*
import mathematics.*
import physics.*
import rendering.*
import spawner.*

/**
 * Blueprint for building a MechControl.
 *
 * @param data
 *  The input data string
 */
class MechBlueprint(data: String) extends ControlBlueprint(data):
  def loadControl(simulation: SimulationControl, renderingEngine: RenderingEngine, position: Vector3, hull: RenderObject, mass: Double): UnitControlSystem =
    println("MECH: " + data)
    val dataStrings = data.split("!")
    //leg1
    val l1j1 = BoxBlueprint(dataStrings(1)).loadBox(renderingEngine, Option(hull))
    val l11 = BoxBlueprint(dataStrings(2)).loadBox(renderingEngine, Option(l1j1))
    val l1j2 = BoxBlueprint(dataStrings(3)).loadBox(renderingEngine, Option(l11))
    val l12 = BoxBlueprint(dataStrings(4)).loadBox(renderingEngine, Option(l1j2))
    //leg2
    val l2j1 = BoxBlueprint(dataStrings(5)).loadBox(renderingEngine, Option(hull))
    val l21 = BoxBlueprint(dataStrings(6)).loadBox(renderingEngine, Option(l2j1))
    val l2j2 = BoxBlueprint(dataStrings(7)).loadBox(renderingEngine, Option(l21))
    val l22 = BoxBlueprint(dataStrings(8)).loadBox(renderingEngine, Option(l2j2))

    //Physics and collision
    val coll = UnitCollision(simulation, Array((Vector3(0, 0, l12.mesh.points.head.z), l12, 150.0), (Vector3(0, 0, l22.mesh.points.head.z), l22, 150.0)))
    val phys = PhysicsObject(position, Vector3(0, 0, 0), Quaternion(1, 0, 0, 0), mass, coll, hull, simulation)

    //Control
    val control = MechControl(simulation, phys, coll, Array(l11, l12, l21, l22))
    control.speedMulti = dataStrings(0).toDouble

    //Return control
    control
