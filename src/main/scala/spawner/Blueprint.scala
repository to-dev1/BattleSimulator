package spawner

import control.TurretControlSystem
import game.*
import mathematics.*
import rendering.*
import spawner.*
import terrain.*

import java.awt.Color
import scala.collection.mutable.Buffer

/**
 * Blueprints are used for creating objects from a string of text.
 */
trait Blueprint(val data: String)

/**
 * Helper functions for the blueprint system.
 */
object Blueprint:
  val mainPath = "data/"
  val fileLoader = FileLoader()
  val blueprints = fileLoader.readFolder(mainPath + "blueprints/")

  def getPlanetIndex: Option[Int] =
    fileLoader.readFile(mainPath + "planet.txt").split("\n").mkString.toIntOption

  def loadBattle(simulation: SimulationControl, renderingEngine: RenderingEngine, name: String) =
    val battle = fileLoader.readFile(mainPath + "scenario/" + name)
    val sides = battle.split("\n").mkString.split("VS")
    var team = 0
    var teamPosition = Vector3(1500, 0, 1000)
    for(side <- sides) do
      println("Side: " + side)
      val units = side.split('#').filter(u => u.nonEmpty)
      for(unit <- units) do
        println("Unit: " + unit)
        val unitData = unit.split('|')
        val count = unitData(0).toInt
        for(i <- 0 until count) do
          val spawnPos = Vector3(math.random() * 1024.0, 0, math.random() * 256.0) + teamPosition
          val loaded = loadUnitName(simulation, renderingEngine, unitData(1), Vector3(spawnPos.x, simulation.terrain.getHeight(spawnPos), spawnPos.z), team)
          if(loaded != null) then simulation.units += loaded
      team += 1
      teamPosition += Vector3(0, 0, 2000)

  def loadUnitName(simulation: SimulationControl, renderingEngine: RenderingEngine, name: String, position: Vector3, team: Int) : UnitObject =
    val names = blueprints.map(_._1)
    if(names.contains(name)) then
      loadUnitIndex(simulation, renderingEngine, names.indexOf(name), position, team)
    else
      null

  def loadUnitIndex(simulation: SimulationControl, renderingEngine: RenderingEngine, index: Int, position: Vector3, team: Int): UnitObject =
    loadUnit(simulation, renderingEngine, blueprints(index)._2, position, team)

  private def loadUnit(simulation: SimulationControl, renderingEngine: RenderingEngine, data: String, position: Vector3, team: Int): UnitObject =
    UnitBlueprint(deleteBetween(data, '/').split("\n").mkString).loadUnit(simulation, renderingEngine, position, team)

  private def deleteBetween(data: String, char: Char): String =
    var output = data
    var running = true
    while running do
      if(output.contains(char)) then
        val index = output.indexOf(char) + 1
        var delete = index
        while(output(delete) != char && delete < output.length - 1) do
          delete += 1
        output = output.take(index - 1) + output.drop(delete + 1)
      else
        running = false
    output

  def loadBlock(simulation: SimulationControl, renderingEngine: RenderingEngine, data: String, unit: UnitObject, turret: TurretControlSystem, container: Option[RenderObject]): (TurretControlSystem, Option[RenderObject]) =
    //Return these
    var newContainer = container
    var newTurret = turret
    //Parse
    val parts = data.split('+')
    val function = parts(0).split('!')
    val functionIdentifier = function.head
    //Check identifier
    if(functionIdentifier == "->" || functionIdentifier == ">") then
      container.foreach(c => newContainer = Option(c.subObjects.last))
      println("DOWN")
    else if(functionIdentifier == "<-" || functionIdentifier == "<") then
      container.foreach(c => newContainer = c.containerObject)
      println("UP")
    else
      val visual = parts(1).split('!')
      val visualIdentifier = visual.head
      var renderObject: RenderObject = RenderObject(Vector3(0, 0, 0), Quaternion(1, 0, 0, 0), Mesh(Buffer.empty, Buffer.empty), None, renderingEngine)
      println("visualIdentifier: " + visualIdentifier)
      visualIdentifier match
        case "BOX" =>
          renderObject = BoxBlueprint(visual(1)).loadBox(renderingEngine, container)
        case "HULL" =>
          renderObject = HullBlueprint(visual(1)).loadHull(renderingEngine, container)
        case _ => renderObject =
          EmptyBlueprint(visual(1)).loadEmpty(renderingEngine, container)
      functionIdentifier match
        case "ROTATE" =>
          newTurret = RotateBlueprint(function(1)).loadRotateTurret(simulation, unit, renderObject)
          unit.turrets += newTurret
        case "TURRET" =>
          var azimuth = renderObject
          var containerCount = 0
          function.lift(2).foreach(f => f.toIntOption.foreach(c => containerCount = c))
          for(i <- 0 until containerCount) do
            azimuth = azimuth.containerObject.head
          newTurret = TurretBlueprint(function(1)).loadTurret(simulation, unit, azimuth, renderObject)
          unit.turrets += newTurret
        case "WEAPON" =>
          turret.weapons += WeaponBlueprint(function(1)).loadWeapon(simulation, unit, renderObject)
        case _ =>
          //Do nothing
    //Return turret and container
    (newTurret, newContainer)


  def generateBox(a: Vector3, b: Vector3, color: Color): Mesh =
    val boxMesh = Mesh(Buffer.empty, Buffer.empty)
    val ax = a.x
    val ay = a.y
    val az = a.z
    val bx = b.x
    val by = b.y
    val bz = b.z

    //Top, bottom
    boxMesh.addQuad(Vector3(ax, ay, az), Vector3(bx, by, -bz), Vector3(-bx, by, -bz), Vector3(-ax, ay, az), color)
    boxMesh.addQuad(Vector3(ax, -ay, az), Vector3(bx, -by, -bz), Vector3(-bx, -by, -bz), Vector3(-ax, -ay, az), color)
    //Sides
    boxMesh.addQuad(Vector3(ax, ay, az), Vector3(ax, -ay, az), Vector3(bx, -by, -bz), Vector3(bx, by, -bz), color)
    boxMesh.addQuad(Vector3(-ax, ay, az), Vector3(-ax, -ay, az), Vector3(-bx, -by, -bz), Vector3(-bx, by, -bz), color)
    //Front, back
    boxMesh.addQuad(Vector3(ax, -ay, az), Vector3(ax, ay, az), Vector3(-ax, ay, az), Vector3(-ax, -ay, az), color)
    boxMesh.addQuad(Vector3(bx, -by, -bz), Vector3(bx, by, -bz), Vector3(-bx, by, -bz), Vector3(-bx, -by, -bz), color)

    boxMesh

  def generateTank(a: Vector3, b: Vector3, c: Vector3, d: Vector3, color: Color): Mesh =
    val tankMesh = Mesh(Buffer.empty, Buffer.empty)
    val ax = a.x
    val ay = a.y
    val az = a.z
    val bx = b.x
    val by = b.y
    val bz = b.z
    val cx = c.x
    val cy = c.y
    val cz = c.z
    val dx = d.x
    val dy = d.y
    val dz = d.z

    //Top, bottom
    tankMesh.addQuad(Vector3(ax, ay, az), Vector3(ax, ay, -az), Vector3(-ax, ay, -az), Vector3(-ax, ay, az), color)
    tankMesh.addQuad(Vector3(cx, cy, cz), Vector3(cx, cy, -cz), Vector3(-cx, cy, -cz), Vector3(-cx, cy, cz), color)
    //Sides
    tankMesh.addQuad(Vector3(bx, by, bz), Vector3(bx, by, -dz), Vector3(ax, ay, -az), Vector3(ax, ay, az), color)
    tankMesh.addQuad(Vector3(cx, cy, cz), Vector3(cx, cy, -cz), Vector3(bx, by, -dz), Vector3(bx, by, bz), color)
    tankMesh.addQuad(Vector3(-ax, ay, az), Vector3(-ax, ay, -az), Vector3(-bx, by, -dz), Vector3(-bx, by, bz), color)
    tankMesh.addQuad(Vector3(-bx, by, bz), Vector3(-bx, by, -dz), Vector3(-cx, cy, -cz), Vector3(-cx, cy, cz), color)
    //Front, back
    tankMesh.addQuad(Vector3(bx, by, bz), Vector3(ax, ay, az), Vector3(-ax, ay, az), Vector3(-bx, by, bz), color)
    tankMesh.addQuad(Vector3(bx, by, bz), Vector3(cx, cy, cz), Vector3(-cx, cy, cz), Vector3(-bx, by, bz), color)
    tankMesh.addQuad(Vector3(ax, ay, -az), Vector3(bx, by, -dz), Vector3(-bx, by, -dz), Vector3(-ax, ay, -az), color)
    tankMesh.addQuad(Vector3(cx, cy, -cz), Vector3(bx, by, -dz), Vector3(-bx, by, -dz), Vector3(-cx, cy, -cz), color)

    tankMesh

  def generateWheel(wSize: Double, wWidth: Double, circleColor: Color, sideColor: Color): Mesh =
    val wheelMesh = Mesh(Buffer.empty, Buffer.empty)
    //Side 1
    wheelMesh.addQuad(Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, -wSize, wSize * 3), Vector3(wWidth, -wSize, -wSize * 3), Vector3(wWidth, wSize, -wSize * 3), sideColor)
    wheelMesh.addQuad(Vector3(wWidth, wSize * 3, wSize), Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, wSize, -wSize * 3), Vector3(wWidth, wSize * 3, -wSize), sideColor)
    wheelMesh.addQuad(Vector3(wWidth, -wSize, wSize * 3), Vector3(wWidth, -wSize * 3, wSize), Vector3(wWidth, -wSize * 3, -wSize), Vector3(wWidth, -wSize, -wSize * 3), sideColor)
    //Circle
    wheelMesh.addQuad(Vector3(wWidth, wSize * 3, wSize), Vector3(wWidth, wSize * 3, -wSize), Vector3(-wWidth, wSize * 3, -wSize), Vector3(-wWidth, wSize * 3, wSize), circleColor)
    wheelMesh.addQuad(Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, wSize * 3, wSize), Vector3(-wWidth, wSize * 3, wSize), Vector3(-wWidth, wSize, wSize * 3), circleColor)
    wheelMesh.addQuad(Vector3(wWidth, wSize * 3, -wSize), Vector3(wWidth, wSize, -wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), Vector3(-wWidth, wSize * 3, -wSize), circleColor)
    wheelMesh.addQuad(Vector3(wWidth, -wSize * 3, wSize), Vector3(wWidth, -wSize * 3, -wSize), Vector3(-wWidth, -wSize * 3, -wSize), Vector3(-wWidth, -wSize * 3, wSize), circleColor)
    wheelMesh.addQuad(Vector3(wWidth, -wSize, wSize * 3), Vector3(wWidth, -wSize * 3, wSize), Vector3(-wWidth, -wSize * 3, wSize), Vector3(-wWidth, -wSize, wSize * 3), circleColor)
    wheelMesh.addQuad(Vector3(wWidth, -wSize * 3, -wSize), Vector3(wWidth, -wSize, -wSize * 3), Vector3(-wWidth, -wSize, -wSize * 3), Vector3(-wWidth, -wSize * 3, -wSize), circleColor)
    wheelMesh.addQuad(Vector3(wWidth, wSize, -wSize * 3), Vector3(wWidth, -wSize, -wSize * 3), Vector3(-wWidth, -wSize, -wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), circleColor)
    wheelMesh.addQuad(Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, -wSize, wSize * 3), Vector3(-wWidth, -wSize, wSize * 3), Vector3(-wWidth, wSize, wSize * 3), circleColor)
    //Side 2
    wheelMesh.addQuad(Vector3(-wWidth, wSize, wSize * 3), Vector3(-wWidth, -wSize, wSize * 3), Vector3(-wWidth, -wSize, -wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), sideColor)
    wheelMesh.addQuad(Vector3(-wWidth, wSize * 3, wSize), Vector3(-wWidth, wSize, wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), Vector3(-wWidth, wSize * 3, -wSize), sideColor)
    wheelMesh.addQuad(Vector3(-wWidth, -wSize, wSize * 3), Vector3(-wWidth, -wSize * 3, wSize), Vector3(-wWidth, -wSize * 3, -wSize), Vector3(-wWidth, -wSize, -wSize * 3), sideColor)

    wheelMesh
  
  def stringToArray(data: String, symbol: Char): Array[Double] =
    val a = data.split(symbol)
    val output = Array.fill(a.length)(0.0)
    for(i <- a.indices) do
      output(i) = a(i).toDouble
    output