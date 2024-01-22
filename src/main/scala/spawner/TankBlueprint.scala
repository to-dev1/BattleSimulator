package spawner

import control.*
import game.*
import mathematics.*
import physics.*
import rendering.*
import spawner.*

import scala.collection.mutable.Buffer

/**
 * Blueprint for building a TankControl.
 *
 * @param data
 *  The input data string
 */
class TankBlueprint(data: String) extends ControlBlueprint(data):
  def loadControl(simulation: SimulationControl, renderingEngine: RenderingEngine, position: Vector3, hull: RenderObject, mass: Double): UnitControlSystem =
    println("TANK: " + data)
    val dataStrings = data.split("!")
    val collPoints: Buffer[(Vector3, RenderObject, Double)] = Buffer.empty

    for(p <- dataStrings.drop(1)) do
      val wheel = WheelBlueprint(p).loadWheel(renderingEngine, Option(hull))
      collPoints += ((Vector3(0, -wheel._2 * 2.0, 0) + wheel._1.position, hull, 10.0))

    //Physics and collision
    val coll = UnitCollision(simulation, collPoints.toArray)
    val phys = PhysicsObject(position, Vector3(0, 0, 0), Quaternion(1, 0, 0, 0), mass, coll, hull, simulation)

    //Control
    val control = TankControl(simulation, phys, coll)
    control.speedMulti = dataStrings(0).toDouble

    //Return control
    control