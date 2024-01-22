package terrain

import scala.math
import scala.collection.mutable.Buffer
import game.*
import mathematics.*
import rendering.*
import terrain.*

import java.awt.Color

/**
 * The terrain used by the SimulationControl.
 *
 * @param position
 *  Position of the terrain relative to the origin
 * @param planet
 *  The planet used for the terrain generation
 * @param simulation
 *  The simulation using this terrain
 */
class Terrain(var position: Vector3, val planet: Planet, simulation: SimulationControl):

  //Size
  var size = 32
  var chunkScale = 4
  var chunkSize = 128

  //Planet
  var biome = planet.biome
  var terrainWave = planet.terrainWave
  var multiOffset = Vector3(0, 0, 0)

  //Terrain
  var chunks: Array[TerrainChunk] = Array.empty

  def getHeight(pos: Vector3) =
    val m = getHeightMulti(pos)
    val a = math.sin(pos.x / terrainWave.aWave) * terrainWave.aMulti + math.sin(pos.z / terrainWave.aWave) * terrainWave.aMulti
    val b = math.pow(math.sin(pos.x / terrainWave.bWave + terrainWave.multiOffset.x) * terrainWave.bMulti * m + math.sin(pos.z / terrainWave.bWave + terrainWave.multiOffset.z) * terrainWave.bMulti * m, 3.0)
    math.sin(pos.x / terrainWave.mainWave) * terrainWave.mainMulti * m + math.sin(pos.z / terrainWave.mainWave) * terrainWave.mainMulti * m + a + b

  def getHeightMulti(pos: Vector3) =
    math.sin(pos.x / terrainWave.secondWave + terrainWave.multiOffset.x) * terrainWave.secondMulti + math.sin(pos.z / terrainWave.secondWave + terrainWave.multiOffset.z) * terrainWave.secondMulti

  def getColor(pos: Vector3): Color =
    val r = getColorMulti(pos + Vector3(64, 0, 64), biome.colorAreas.x, biome.colorAmount)
    val g = getColorMulti(pos + Vector3(-32, 0, 128), biome.colorAreas.y, biome.colorAmount)
    val b = getColorMulti(pos + Vector3(128, 0, -32), biome.colorAreas.z, biome.colorAmount)
    val color = biome.mainColor
    Color((color.getRed.toDouble * r).toInt, (color.getGreen.toDouble * g).toInt, (color.getBlue.toDouble * b).toInt)

  def getColorMulti(pos: Vector3, area: Int, amount: Int) =
    ((math.sin(pos.x / area + 15000) + 1.0) * amount / 2.0 + (math.sin(pos.z / area - 27000) + 1.0) * amount / 2.0 + (255 - 2 * amount)) / 255.0

  def render() =
    chunks.foreach(_.update(simulation.camera.position))

  def generate() =
    chunks = Array.fill(size * size)(null)

    for(x <- 0 until size) do
      for(y <- 0 until size) do
        val chunk = TerrainChunk(Vector2(x, y), chunkScale, chunkSize, RenderObject(Vector3(0, 0, 0), Quaternion(1, 0, 0, 0), Mesh(Buffer.empty, Buffer.empty), None, simulation.renderingEngine), this)
        chunk.buildMeshes()
        chunks(x * size + y) = chunk