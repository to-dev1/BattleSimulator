package rendering

import scala.collection.mutable.Buffer

import mathematics.*

/**
 * The basic component of the rendering system
 *
 * @param position
 *  Position of the object, a Vector3
 * @param rotation
 *  The rotation of the object, a Quaternion
 * @param mesh
 *  The Mesh of the object
 * @param containerObject
 *  The RenderObject that contains this object, can be None
 * @param renderingEngine
 *  The RenderingEngine used to render this object
 */
class RenderObject(position: Vector3, rotation: Quaternion, var mesh: Mesh, var containerObject: Option[RenderObject], val renderingEngine: RenderingEngine) extends RenderSystem(position, rotation):
    var subObjects: Buffer[RenderObject] = Buffer.empty
    //This is used on the highest level object after changing its position and rotation
    def update(): Unit =
      //Render this object
      if(renderingEngine.renderFrame) then renderingEngine.renderMesh(mesh, worldPosition, worldRotation, scale)
      for s <- subObjects do
        //Calculate position and rotation
        s.worldPosition = worldPosition + Quaternion.rotateVectorByQuaternion(s.position, worldRotation)
        s.worldRotation = Quaternion.multiplyQuaternions(Quaternion.rotateByQuaternion(s.rotation, worldRotation), worldRotation)
        //Recursion
        s.update()

object RenderObject:
  def getPosition(p: Vector3, renderObject: RenderObject): Vector3 =
    var path: Buffer[RenderObject] = Buffer(renderObject)
    var current = renderObject
    while(current.containerObject.isDefined) do
      current.containerObject.foreach(c => path += c)
      current = path.last
    path = path.reverse

    var pos = Vector3(0, 0, 0)
    var rt = Quaternion(1, 0, 0, 0)
    for s <- path do
      pos = pos + Quaternion.rotateVectorByQuaternion(s.position, rt)
      rt = Quaternion.multiplyQuaternions(Quaternion.rotateByQuaternion(s.rotation, rt), rt)

    pos + Quaternion.rotateVectorByQuaternion(p, rt)


