package terrain

import scala.collection.mutable.Buffer

import java.awt.Color.*

import game.*
import mathematics.*
import rendering.*
import terrain.*

/**
 * One chunk of the entire terrain.
 * Can change resolution based on the camera's distance to improve performance.
 * This is required because the rendering engine is using the computer's CPU and is very slow.
 *
 * @param gridPos
 *  The chunk's position in chunk grid coordinates
 * @param scale
 *  The maximum accuracy of the terrain generation and the minimum size of the terrain mesh triangles
 * @param size
 *  The size of this chunk
 * @param renderObject
 *  The RenderObject used to render this chunk
 * @param terrain
 *  Terrain that contains this chunk
 */
class TerrainChunk(var gridPos: Vector2, var scale: Double, var size: Double, var renderObject: RenderObject, val terrain: Terrain):
  var meshes: Buffer[Mesh] = Buffer.empty
  var position = Vector3(size * gridPos.x, 0, size * gridPos.y) + terrain.position

  def update(cameraPos: Vector3) =
    val pos = position + Vector3(size / 2, 0, size / 2)
    val h = terrain.getHeight(cameraPos) + pos.y
    val meshIndex = math.min(math.sqrt(math.max(math.abs(pos.x - cameraPos.x) * 0.01, math.max(math.abs(h - cameraPos.y) * 0.01, math.abs(pos.z - cameraPos.z) * 0.01))).toInt, meshes.length - 1)
    renderObject.worldPosition = position + Vector3(0, -2 * meshIndex, 0)
    renderObject.mesh = meshes(meshIndex)
    renderObject.update()

  def buildMeshes() =
    var currentScale = scale
    while currentScale <= size do
      createMesh(currentScale, Vector3(0, 0, 0))
      currentScale *= 2

  def createMesh(meshScale: Double, pos: Vector3) =
    val mesh = Mesh(Buffer.empty, Buffer.empty)
    val count = (size / meshScale).toInt
    for(x <- 0 until count) do
      for(y <- 0 until count) do
        val va = Vector3(1, 0, 1)
        val vb = Vector3(1, 0, 0)
        val vc = Vector3(0, 0, 0)
        val vd = Vector3(0, 0, 1)
        val a = pos + Vector3((x + va.x) * meshScale, 0, (y + va.z) * meshScale)
        val b = pos + Vector3((x + vb.x) * meshScale, 0, (y + vb.z) * meshScale)
        val c = pos + Vector3((x + vc.x) * meshScale, 0, (y + vc.z) * meshScale)
        val d = pos + Vector3((x + vd.x) * meshScale, 0, (y + vd.z) * meshScale)

        mesh.addQuad(a + Vector3(0, terrain.getHeight(a + position), 0), b + Vector3(0, terrain.getHeight(b + position), 0), c + Vector3(0, terrain.getHeight(c + position), 0), d + Vector3(0, terrain.getHeight(d + position), 0), terrain.getColor(a + position))

    meshes += mesh