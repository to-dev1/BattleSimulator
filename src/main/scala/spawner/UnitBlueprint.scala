package spawner

import control.*
import game.*
import mathematics.*
import physics.*
import rendering.*
import spawner.*

import scala.collection.mutable.Buffer

/**
 * Blueprint for a UnitObject, this branches into a tree of blueprints when loading.
 *
 * @param data
 *  The entire input data string with comments and line changes removed
 */
class UnitBlueprint(data: String) extends Blueprint(data):
  def loadUnit(simulation: SimulationControl, renderingEngine: RenderingEngine, position: Vector3, team: Int): UnitObject =
    def errorReturn(message: String) =
      println("WARNING: " + message + " LOADING UNIT FAILED")
      null
    try
      //Load unit
      println("LOADING UNIT: " + data)
      val dataStrings = data.split('#')
      if(dataStrings.isEmpty) then
        return errorReturn("empty data string")

      val t = dataStrings(0)

      var hull: RenderObject = null

      //Select unit type TANK or MECH
      var controlBlueprint: ControlBlueprint = null
      if(t == "MECH")
        //Hull
        hull = BoxBlueprint(dataStrings(2)).loadBox(renderingEngine, None)
        controlBlueprint = MechBlueprint(dataStrings(4))
      else if(t == "TANK")
        //Hull
        hull = HullBlueprint(dataStrings(2)).loadHull(renderingEngine, None)
        controlBlueprint = TankBlueprint(dataStrings(4))
      else
        return errorReturn("invalid unit type " + t)

      //Stats
      val stats = Blueprint.stringToArray(dataStrings(1), '|')
      val unitStats = UnitStats(stats(0), stats(1), stats(2), stats(3))

      val mass = dataStrings(3).toDouble
      var yVec = Vector3(0, 0, 0)
      if(mass > 100.0) then yVec = Vector3(0, 40, 0)

      //Control
      val control = controlBlueprint.loadControl(simulation, renderingEngine, position + yVec, hull, mass)

      //Unit
      val unit = UnitObject(simulation, null, control, unitStats, Buffer.empty, team)

      //Blocks
      var output: (TurretControlSystem, Option[RenderObject]) = (null, Option(hull))
      for(i <- dataStrings.drop(5)) do
        output = Blueprint.loadBlock(simulation, renderingEngine, i, unit, output._1, output._2)

      val ai = GroundUnitAI(simulation, control, unit)
      unit.ai = ai
      //Return
      unit
    catch
      case e: Exception =>
        e.printStackTrace()
        errorReturn("something went wrong")

