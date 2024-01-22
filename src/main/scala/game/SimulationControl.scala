package game

import java.awt.image.BufferedImage
import java.awt.Color.*
import java.awt.Color
import java.awt.Image
import game.WindowSettings

import java.awt.Component

import scala.collection.mutable.Buffer
import scala.swing.*
import scala.swing.event.Key

import control.*
import development.*
import game.*
import mathematics.*
import physics.*
import rendering.*
import spawner.Blueprint
import terrain.*

/**
 * Simulation control connects the simulated to world to the window system and user input.
 *
 * @param windowSettings
 *  the settings of the connected window
 * @param window
 *  the connected window
 */
class SimulationControl(windowSettings: WindowSettings, window: MainWindow):
  //Planet
  val planetDatabase = PlanetDatabase()
  var planetIndex = 0
  Blueprint.getPlanetIndex.foreach(p => if (p < planetDatabase.planets.length && p >= 0) then planetIndex = p)
  var planet = planetDatabase.planets(planetIndex)

  //Rendering
  val renderingEngine = RenderingEngine(planet.atmosphereColor, windowSettings)
  val testMeshes = TestMeshes()
  var renderFrame = true

  //Simulation
  var deltaTime = 1.0
  var timeScale = 1.0
  var simulationTime = 1.0
  var gravity = planet.gravity

  //Camera
  val camera = Camera(Vector3(1750, 100, 1250), Vector3(0, 0, 0), this)
  var spawningTeam = 0

  //Objects
  var units: Buffer[UnitObject] = Buffer.empty
  var particles: Buffer[PhysicsObject] = Buffer.empty
  var spawnedParticles: Buffer[PhysicsObject] = Buffer.empty
  var bullets: Buffer[PhysicsObject] = Buffer.empty

  var testObjects: Buffer[RenderObject] = Buffer.empty

  camera.position = Vector3(0, 0, 0)
  //10000
  //for(i <- 0 until 100) do
    //testObjects += RenderObject(Vector3(math.random() * 300 - 150, math.random() * 300 - 150, math.random() * 1000 + 10), Quaternion.constructFullQuaternion(math.random() * 3, math.random() * 3, math.random() * 3), testMeshes.cubeMesh, None, renderingEngine)


  //Terrain
  var terrain = Terrain(Vector3(0, -20, 0), planet, this)
  terrain.generate()

  Blueprint.loadBattle(this, renderingEngine, "battle.txt")

  def update() =
    renderingEngine.renderFrame = renderFrame

    //The order is: delete --> add --> update

    //Filter out destoryed objects
    particles = particles.filterNot(_.destroyed)
    units = units.filterNot(_.destroyed)
    bullets = bullets.filterNot(_.destroyed)

    //Add new particles
    particles.addAll(spawnedParticles)
    spawnedParticles.clear()

    //Update all objects
    units.foreach(_.update())
    particles.foreach(_.update())
    bullets.foreach(_.update())

    testObjects.foreach(_.update())

    //Render the terrain
    terrain.render()

    //Handle input
    if(keyDown(Key.Escape)) then window.application.quit()

    //Camera movement
    if(keyDown(Key.W)) then camera.moveCamera(Vector3(0, 0, 1))
    if(keyDown(Key.S)) then camera.moveCamera(Vector3(0, 0, -1))
    if(keyDown(Key.A)) then camera.moveCamera(Vector3(-1, 0, 0))
    if(keyDown(Key.D)) then camera.moveCamera(Vector3(1, 0, 0))
    if(keyDown(Key.Q)) then camera.moveCamera(Vector3(0, 1, 0))
    if(keyDown(Key.E)) then camera.moveCamera(Vector3(0, -1, 0))

    //Camera teleport
    if(keyDown(Key.R)) then camera.moveCameraBy(Vector3(0, 0, 1), 512)

    //Camera rotation
    if(keyDown(Key.Up)) then camera.rotateCamera(1.0, Vector3(-1, 0, 0))
    if(keyDown(Key.Down)) then camera.rotateCamera(1.0, Vector3(1, 0, 0))
    if(keyDown(Key.Left)) then camera.rotateCamera(1.0, Vector3(0, -1, 0))
    if(keyDown(Key.Right)) then camera.rotateCamera(1.0, Vector3(0, 1, 0))

    //Spawning objects
    if(keyDown(Key.N)) then debugSpawnTest(4, camera.position, spawningTeam)
    if(keyDown(Key.V)) then
      if(spawningTeam == 0) then spawningTeam = 1 else spawningTeam = 0

    //Time control
    if(keyDown(Key.T)) then
      if(timeScale > 0.01) then
        timeScale = 0.01
      else
        timeScale = 1.0
    if(keyDown(Key.Y)) then
      if(timeScale > 1.0) then
        timeScale = 1.0
      else
        timeScale = 10.0

    //Spawning bullets
    if(keyDown(Key.F)) then debugSpawnBullet(camera.position, 2.0, 1000.0, 10000.0)

    //Destroy all units
    if(keyDown(Key.O)) then
      units.foreach(u => u.destroy())

    window.pressedKeys.clear()
  end update

  def render(): BufferedImage =
    val startTime = System.nanoTime()
    val startTimeRender = System.nanoTime()

    renderingEngine.totalTriangleCount = 0
    renderingEngine.clearPixels()
    renderingEngine.cameraPosition = camera.position
    renderingEngine.cameraRotation = camera.rotation

    val simulationFrames = ((deltaTime * timeScale * 1000.0) / 25.0).toInt + 1
    simulationTime = simulationTime / simulationFrames.toDouble

    for(i <- 0 until simulationFrames) do
      renderFrame = false
      if(i == 0) then renderFrame = true
      update()

    val endTimeRender = System.nanoTime()
    val frameTimeRender = (endTimeRender - startTimeRender) * 0.000001

    val img = new BufferedImage(windowSettings.width, windowSettings.height, BufferedImage.TYPE_INT_ARGB)
    img.setRGB(0, 0, windowSettings.width, windowSettings.height, renderingEngine.pixels, 0, windowSettings.width)

    val endTime = System.nanoTime()
    val frameTime = (endTime - startTime) * 0.000001
    println("render time: " + frameTimeRender.toInt + " ms" + ", frame time: " + frameTime.toInt + " ms, fps: " + (1000.0 / frameTime).toInt + ", simulation frames: " + simulationFrames + ", triangles: " + renderingEngine.totalTriangleCount + ", terrain: " + terrain.getHeight(camera.position))

    deltaTime = frameTime / 1000.0
    simulationTime = deltaTime * timeScale

    //Return
    img

  end render

  //Keys

  def keyDown(key: scala.swing.event.Key.Value) =
    window.pressedKeys.contains(key)

  //Misc

  def debugSpawnTest(index: Int, pos: Vector3, team: Int) =
    units += Blueprint.loadUnitIndex(this, renderingEngine, index, pos, team)
    //units.last.control.physicsObject.rotation = Quaternion.constructFullQuaternion(1, 0, 0)

  def debugSpawnBullet(pos: Vector3, speed: Double, kinetic: Double, explosive: Double) =
    var mesh = testMeshes.cubeMesh
      val bullet = RenderObject(Vector3(0, 0, 0), Quaternion(1, 0, 0, 0), testMeshes.generateBox(Vector3(1, 1, 2), Vector3(1, 1, 2), Color(50, 50, 50)), None, renderingEngine)
      //0.1, 5.0
      bullet.scale = math.sqrt(math.sqrt(explosive)) * 0.2 * 0.2
      val collisionSystem = BulletCollision(this, kinetic, explosive, null)
      val vel = Quaternion.rotateVectorByQuaternion(Vector3(0, 0, 1), camera.rotation) * speed
      val spawned = PhysicsObject(pos, vel, camera.rotation, 0, collisionSystem, bullet, this)
      bullets += spawned

  def spawnBullet(pos: Vector3, rotation: Quaternion, speed: Double, kinetic: Double, explosive: Double, a: Vector3, b: Vector3, unit: UnitObject) =
    var mesh = testMeshes.cubeMesh
      val green = (255.0 * (100.0 / (explosive + 100.0 + speed * speed * speed))).toInt
      val blue = (155.0 * (1.0 / (explosive + 1.0))).toInt
      val bullet = RenderObject(Vector3(0, 0, 0), Quaternion(1, 0, 0, 0), testMeshes.generateBox(a, b, Color(255, green, blue)), None, renderingEngine)
      val collisionSystem = BulletCollision(this, kinetic, explosive, unit)
      val vel = Quaternion.rotateVectorByQuaternion(Vector3(0, 0, 1), rotation) * speed
      val spawned = PhysicsObject(pos, vel, rotation, 0, collisionSystem, bullet, this)
      bullets += spawned

  def spawnParticles(pos: Vector3, trail: Boolean, trailTimeMulti: Double, gravityMultiplier: Double, sizeMulti: Double, setScale: Double, time: Double, speed: Double, count: Int) =
    for(i <- 0 until count) do
      var mesh = testMeshes.cubeMesh
      if(gravityMultiplier < 0) then mesh = testMeshes.cubeMesh2
      val particle = RenderObject(Vector3(0, 0, 0), Quaternion(1, 0, 0, 0), mesh, None, renderingEngine)
      var size = math.random() * sizeMulti
      if(setScale > 0) then size = setScale
      //particle.scale = size * 0.5 + 0.1
      particle.scale = size + 0.001
      val collisionSystem = ParticleCollision(this, particle, trail, gravityMultiplier, particle.scale, time)
      collisionSystem.trailTimeMulti = trailTimeMulti
      val x = Quaternion.constructQuaternion(math.random() * 3.14 * 2.0, Vector3(1, 0, 0))
      val y = Quaternion.constructQuaternion(math.random() * 3.14 * 2.0, Vector3(0, 1, 0))
      val r = Quaternion.multiplyQuaternions(y, Quaternion.multiplyQuaternions(x, Quaternion(1, 0, 0, 0)))
      val vel = Quaternion.rotateVectorByQuaternion(Vector3(0, 0, 1), r) * (1.0 / (size + 0.5)) * speed
      val spawned = PhysicsObject(pos, vel, r, 0, collisionSystem, particle, this)
      spawnedParticles += spawned

  def spawnExplosion(pos: Vector3, magnitude: Double) =
    val mg = magnitude * 0.3

    val sqr2 = math.sqrt(mg)
    val sqr25 = math.pow(mg, 0.40)
    val sqr3 = math.pow(mg, 0.33)
    val sqr4 = math.sqrt(sqr2)
    val sqr6 = math.sqrt(sqr3)

    val m = 0.2

    //pos, trail, trailTime, gravity, size, setScale, time, speed, count
    spawnParticles(pos, true, 0.7, 1.0, 0.5 * sqr3 * m, 0, sqr3, sqr4 * 0.3 * 2.0 * m, (sqr3 * 4.0).toInt)
    spawnParticles(pos, true, sqr6 * 0.7, 1.0, 1.0 * m, sqr3 * 1.5 * m, sqr6 * 0.2, sqr25 * 0.5 * m, (sqr4 * 4.0).toInt + 1)
    spawnParticles(pos, false, 0.7, -0.2, sqr3 * m, 0, 5.0, sqr3 * 0.05 * m, (sqr3 * 4.0).toInt)

  def spawnImpactParticles(pos: Vector3, magnitude: Double) =
    val mg = magnitude * 0.5

    val sqr2 = math.sqrt(mg)
    val sqr3 = math.pow(mg, 0.33)
    val sqr4 = math.sqrt(sqr2)

    //0.2
    val m = 0.2

    //pos, trail, trailTime, gravity, size, setScale, time, speed, count
    spawnParticles(pos, false, 0.7, 1.0, 0.5 * sqr3 * m, 0, sqr3, sqr4 * 0.3 * m, (sqr3 * 6.0).toInt)

  def solveTrajectoryVector(v: Double, pos: Vector3, target: Vector3, airResistance: Double): (Vector3, Boolean) =
    val tx = target.x - pos.x
    val tz = target.z - pos.z
    val x = math.sqrt(tx * tx + tz * tz)
    val y = target.y - pos.y
    val g = gravity

    val sqr = v * v * v * v - g * (g * x * x + 2 * y * v * v)
    val a = math.atan((v * v - math.sqrt(sqr)) / (g * x))

    val p = getTrajectoryPosition(v, a, 0.1)
    var vec = Vector3(0, p._2, 0) + Vector3.normalize(Vector3(tx, 0, tz)) * p._1

    if(sqr < 0) then
      vec = target - pos

    (pos + vec, checkTrajectoryTerrain(v, a, tx, tz, x, pos) && sqr >= 0.0)

  def getTrajectoryPosition(v: Double, a: Double, t: Double): (Double, Double) =
    (v * math.cos(a) * t, v * math.sin(a) * t - 0.5 * gravity * t * t)

  def checkTrajectoryTerrain(v: Double, a: Double, tx: Double, tz: Double, dist: Double, pos: Vector3): Boolean =
    var count = 0
    var solution = true
    var notFound = true
    var step = 1.0 / v * 20.0
    var current = 0.1
    while(notFound && count < 100) do
      val p = getTrajectoryPosition(v, a, current)
      val vec = Vector3(0, p._2, 0) + Vector3.normalize(Vector3(tx, 0, tz)) * p._1 + pos
      val h = terrain.getHeight(vec)
      val d = vec.y - h - terrain.position.y
      if(d < 0) then
        notFound = false
        solution = dist < p._1
      current += step
      count += 1
    //Return
    solution

  def calculateBulletAirResistance(kinetic: Double, explosive: Double) =
    val mass = kinetic + explosive * 0.5
    calculateAirResistance(math.pow(mass, 2.0), math.pow(mass, 3.0))

  def calculateWeaponAirResistance(weapon: WeaponObject) =
    val stats = weapon.stats
    calculateBulletAirResistance(stats.kinetic, stats.explosive)

  def calculateAirResistance(surface: Double, volume: Double) =
    surface / volume * planet.airResistance



