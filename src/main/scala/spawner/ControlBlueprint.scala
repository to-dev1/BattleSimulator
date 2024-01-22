package spawner

import control.*
import game.*
import mathematics.*
import physics.*
import rendering.*
import spawner.*

/**
 * Abstract class for blueprints that build a UnitControlSystem
 *
 * @param data
 *  The input data string
 */
abstract class ControlBlueprint(data: String) extends Blueprint(data):
  def loadControl(simulation: SimulationControl, renderingEngine: RenderingEngine, position: Vector3, hull: RenderObject, mass: Double): UnitControlSystem