package rendering

import game.*
import mathematics.*
import rendering.*

import java.awt.Color.*
import scala.swing.Color

/**
 * The rendering engine, it draws the meshes to screen and can sort pixels with the zBuffer.
 *
 * @param backgroundColor
 *  If no triangle is drawn over this pixel the backgroundColor is shown instead
 * @param windowSettings
 *  The settings of the simulation's window
 */
class RenderingEngine(backgroundColor: java.awt.Color, windowSettings: WindowSettings):
  //Camera
  var fieldOfVision = 0.5
  var cameraPosition = Vector3(0, 0, 0)
  var cameraRotation = Quaternion(1, 0 ,0, 0)

  //Rendering
  var renderFrame = true

  //Screen
  val screenW = windowSettings.width / windowSettings.pixelScale
  val screenH = windowSettings.height / windowSettings.pixelScale

  //Stats
  var totalTriangleCount = 0

  //Pixels
  val emptyPixels: Array[Int] = Array.fill(windowSettings.width * windowSettings.height)(backgroundColor.getRGB)
  val pixels: Array[Int] = Array.fill(windowSettings.width * windowSettings.height)(RED.getRGB)
  val emptyZBuffer: Array[Double] = Array.fill(windowSettings.width * windowSettings.height)(1000000)
  val zBuffer: Array[Double] = Array.fill(windowSettings.width * windowSettings.height)(1000000)

  def getPointToScreen(pos: Vector3, camera: Vector3): Vector2 =
    val vec = getScreenVector(pos, camera)
    val x = vec.x
    val y = vec.y
    var z = vec.z
    if(z < 0.01) then z = 1.0
    val d = 1.0 / fieldOfVision
    val w = z / d
    val m = 1.0 / w

    val vx = x * m * (screenW/2).toDouble
    val vy = y * m * (screenW/2).toDouble

    Vector2(vx.toInt + screenW/2, vy.toInt + screenH/2)

  def getScreenVector(pos: Vector3, camera: Vector3): Vector3 =
    val vec = Quaternion.rotateVectorByQuaternion(Vector3(pos.x - camera.x, pos.y - camera.y, pos.z - camera.z), Quaternion.invertQuaternion(cameraRotation))

    val x: Double = vec.x
    val y: Double = vec.y
    val z: Double = vec.z
    Vector3(x, y, z)

  def renderMesh(mesh: Mesh, pos: Vector3, rotation: Quaternion, scale: Double) =
      for(t <- mesh.triangles) do
        drawTriangle(t, mesh, pos, rotation, scale)

  def drawTriangle(triangle: Triangle, mesh: Mesh, v: Vector3, rotation: Quaternion, scale: Double) =
    val tri1 = Quaternion.rotateVectorByQuaternion(mesh.points(triangle.a) * scale, rotation)
    val tri2 = Quaternion.rotateVectorByQuaternion(mesh.points(triangle.b) * scale, rotation)
    val tri3 = Quaternion.rotateVectorByQuaternion(mesh.points(triangle.c) * scale, rotation)

    val v1 = tri1 + v
    val v2 = tri2 + v
    val v3 = tri3 + v

    val a = getPointToScreen(v1, cameraPosition)
    val b = getPointToScreen(v2, cameraPosition)
    val c = getPointToScreen(v3, cameraPosition)
    val va = getScreenVector(v1, cameraPosition)
    val vb = getScreenVector(v2, cameraPosition)
    val vc = getScreenVector(v3, cameraPosition)

    if((a.x > 0 && a.x < screenW && a.y > 0 && a.y < screenH) || (b.x > 0 && b.x < screenW && b.y > 0 && b.y < screenH) || (c.x > 0 && c.x < screenW && c.y > 0 && c.y < screenH))
    if(va.z > -10.0 && vb.z > -10.0 && vc.z > -10.0) then
      triangleToScreen(Vector2z(a.x, a.y, va.z), Vector2z(b.x, b.y, vb.z), Vector2z(c.x, c.y, vc.z), triangle.color, va, vb, vc)

  def calculateNormal(a: Vector3, b: Vector3, c: Vector3): Vector3 =
      val v1 = Vector3(b.x - a.x, b.y - a.y, b.z - a.z)
      val v2 = Vector3(c.x - a.x, c.y - a.y, c.z - a.z)

      val A = (v1.y * v2.z - v1.z * v2.y)
      val B = (v1.z * v2.x - v1.x * v2.z)
      val C = (v1.x * v2.y - v1.y * v2.x)

      Vector3(A, B, C)

  def triangleToScreen(a: Vector2z, b: Vector2z, c: Vector2z, color: Color, va: Vector3, vb: Vector3, vc: Vector3) =
    totalTriangleCount += 1
    var top = a
    var middle = b
    var bottom = c
    var topV = va
    var middleV = vb
    var bottomV = vc

    if(a.y < b.y) then
      top = b
      middle = a
      topV = vb
      middleV = va
      if(b.y < c.y) then
        top = c
        middle = b
        bottom = a
        topV = vc
        middleV = vb
        bottomV = va
      else if(a.y < c.y) then
        middle = c
        bottom = a
        middleV = vc
        bottomV = va
      end if
    else if(a.y < c.y) then
      top = c
      bottom = a
      topV = vc
      bottomV = va
      if(b.y < a.y) then
        middle = a
        bottom = b
        middleV = va
        bottomV = vb
      end if
    else if(b.y < c.y) then
      middle = c
      bottom = b
      middleV = vc
      bottomV = vb
    end if

    //top --> middle
    val tmh: Double = top.y.toDouble - middle.y.toDouble
    var tm: Double = 1.0
    var tmc: Int = tmh.toInt
    if(tmh != 0) then tm = (middle.x.toDouble - top.x.toDouble) / tmh

    //middle --> bottom
    val mbh: Double = middle.y.toDouble - bottom.y.toDouble
    var mb: Double = 1.0
    var mbc: Int = mbh.toInt
    if(mbh != 0) then mb = (bottom.x.toDouble - middle.x.toDouble) / mbh

    //top --> bottom
    val tbh: Double = top.y.toDouble - bottom.y.toDouble
    var tb: Double = 1.0
    var tbc: Int = tbh.toInt
    if(tbh != 0) then tb = (bottom.x.toDouble - top.x.toDouble) / tbh

    //zBuffer calculations
    val v1 = Vector3(b.x - a.x, b.y - a.y, b.z - a.z)
    val v2 = Vector3(c.x - a.x, c.y - a.y, c.z - a.z)

    val A = (v1.y * v2.z - v1.z * v2.y)
    val B = (v1.z * v2.x - v1.x * v2.z)
    val C = (v1.x * v2.y - v1.y * v2.x)
    val D = A * a.x + B * a.y + C * a.z

    val normal = calculateNormal(va, vb, vc)
    val length = math.sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z)
    val nx = normal.x / length
    val ny = normal.y / length
    val nz = normal.z / length

    //Light vector
    var lv = Quaternion.rotateVectorByQuaternion(Vector3(1.0, 0.8, 0.7), Quaternion.invertQuaternion(cameraRotation))
    var lx = lv.x
    var ly = lv.y
    var lz = lv.z
    val lLength = math.sqrt(lx * lx + ly * ly + lz * lz)
    lx = lx / lLength
    ly = ly / lLength
    lz = lz / lLength

    val dot = nx * lx + ny * ly + nz * lz
    val dotM = 0.75 + dot * 0.25

    var lightColor = java.awt.Color((color.getRed * dotM).toInt, (color.getGreen * dotM).toInt, (color.getBlue * dotM).toInt)

    //Draw triangle
    var x: Double = top.x
    var y: Double = top.y
    var vx: Double = top.x

    for(i <- 0 until tmc) do
      val count = x - vx
      var countSign = -count.sign.toInt
      var lineCount = math.abs(count).round.toInt

      if(math.abs(count) > 0.5) then lineCount += 2
      else lineCount = 0
      var startX = x
      var usedColor = lightColor
      if(x + lineCount * countSign > screenW) then
        lineCount = math.max(screenW - x.toInt, 0)
      if(x + lineCount * countSign < 0) then
        lineCount = math.max(x.toInt, vx.toInt)
        startX = lineCount
        countSign = -1
      drawLine(startX.toInt, startX, y.toInt, y, lineCount, countSign, usedColor, A, B, C, D)
      x += tm
      vx += tb
      y -= 1.0

    if(tmc == 0) then
      x = middle.x
      y = middle.y
      vx = top.x

    for(i <- 0 until mbc) do
      val count = x - vx
      var countSign = -count.sign.toInt
      var lineCount = math.abs(count).round.toInt

      if(math.abs(count) > 0.5) then lineCount += 2
      else lineCount = 0
      var startX = x
      var usedColor = lightColor
      if(x + lineCount * countSign > screenW) then
        lineCount = math.max(screenW - x.toInt, 0)
      if(x + lineCount * countSign < 0) then
        lineCount = math.max(x.toInt, vx.toInt)
        startX = lineCount
        countSign = -1
      drawLine(startX.toInt, startX, y.toInt, y, lineCount, countSign, usedColor, A, B, C, D)
      x += mb
      vx += tb
      y -= 1.0

  def drawLine(startX: Int, realStartX: Double, y: Int, realY: Double, count: Int, dir: Int, color: Color, a: Double, b: Double, c: Double, d: Double) =
    var x = startX
    var realX = realStartX
    for(i <- 0 until count) do
      var z = (d - a * realX.toDouble - b * realY.toDouble) / c
      if(x >= 0 && x < screenW && y >= 0 && y < screenH) then
        val index = x + (screenH - 1 - y) * screenW
        if(z < zBuffer(index)) then
          pixels(index) = color.getRGB
          zBuffer(index) = z
      x += dir
      realX += dir

  def clearPixels() =
    System.arraycopy(emptyPixels, 0, pixels, 0, pixels.length)
    System.arraycopy(emptyZBuffer, 0, zBuffer, 0, zBuffer.length)