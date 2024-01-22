package testing

import control.*
import game.*
import mathematics.*
import rendering.RenderingEngine
import spawner.Blueprint
import testing.*

import scala.swing.{MainFrame, SimpleSwingApplication}
import scala.collection.mutable.Buffer

object BlueprintTest extends SimpleSwingApplication:

  //1000, 500
  //1500, 900
  val settings = WindowSettings(1000, 500, 1, "Simulation testing")
  val window: MainWindow = MainWindow(settings, this)
  val top: MainFrame = window

  val test = TestingSystem(window.simulation, window.simulation.renderingEngine)
  test.runTests()

  quit()

class TestingSystem(simulationControl: SimulationControl, renderingEngine: RenderingEngine):
  def runTests() =
    println("START TESTING")
    val reports: Buffer[String] = Buffer.empty
    val startTime = System.nanoTime()
    try
      var total = 0
      var correct = 0
      reports += test(testUnitLoading("broken-test-1"), null, "load broken unit")
      reports += test(testUnitLoading("broken-test-2"), null, "load invalid syntax")
      reports += test(testUnitLoading("valid-test-1").control.getClass, classOf[TankControl], "check control system tank type")
      reports += test(testUnitLoading("valid-test-2").control.getClass, classOf[MechControl], "check control system mech type")
      reports += test(testUnitLoading("valid-test-3"), null, "check wrong unit type identifier")
      reports += test(testUnitLoading("valid-test-1").stats.health, 160.0, "check stats")
    catch
      case e: Exception =>
        e.printStackTrace()
        println("ERROR WHILE RUNNING TEST " + reports.length + 1)

    val endTime = System.nanoTime()
    val c = 50
    println("-" * c)
    println("TESTING COMPLETE")

    val timeTaken = (endTime - startTime) * 0.000001
    println("Total time taken: " + timeTaken.toInt + " ms")

    reports.foreach(r => println(r))

    println("-" * c)

  def testUnitLoading(name: String): UnitObject =
    Blueprint.loadUnitName(simulationControl, renderingEngine, name, Vector3(0, 0, 0), 0)

  def test[T](result: T, expected: T, name: String): String =

    val t = checkResult(result, expected)
    var s = ""
    if(t) then
      s += "[PASSED]"
    else
      s += "[FAILED]"
    s + " - " + name

  def checkResult[T](result: T, expected: T): Boolean =
    result == expected
