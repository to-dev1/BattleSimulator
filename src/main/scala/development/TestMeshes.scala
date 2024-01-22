package development

import java.awt.Color.*
import java.awt.Color
import scala.collection.mutable.Buffer

import mathematics.*
import rendering.Mesh

class TestMeshes:
  val tankMesh = Mesh(Buffer.empty, Buffer.empty)
  tankMesh.addQuad(Vector3(1, 0.5, 2), Vector3(1, 0.5, -2.2), Vector3(-1, 0.5, -2.2), Vector3(-1, 0.5, 2), GRAY)
  tankMesh.addQuad(Vector3(1, -0.5, 2), Vector3(1, -0.5, -2), Vector3(-1, -0.5, -2), Vector3(-1, -0.5, 2), GRAY)
  //Sides
  tankMesh.addQuad(Vector3(1, 0.5, 2), Vector3(1, -0.5, 2), Vector3(1, -0.5, -2), Vector3(1, 0.5, -2.2), GRAY)
  tankMesh.addTri(Vector3(1, 0.5, 2), Vector3(1, 0.2, 3), Vector3(1, -0.5, 2), GRAY)
  tankMesh.addQuad(Vector3(-1, 0.5, 2), Vector3(-1, -0.5, 2), Vector3(-1, -0.5, -2), Vector3(-1, 0.5, -2.2), GRAY)
  tankMesh.addTri(Vector3(-1, 0.5, 2), Vector3(-1, 0.2, 3), Vector3(-1, -0.5, 2), GRAY)
  //Front
  tankMesh.addQuad(Vector3(1, 0.2, 3), Vector3(1, 0.5, 2), Vector3(-1, 0.5, 2), Vector3(-1, 0.2, 3), GRAY)
  tankMesh.addQuad(Vector3(1, 0.2, 3), Vector3(1, -0.5, 2), Vector3(-1, -0.5, 2), Vector3(-1, 0.2, 3), GRAY)
  //Back
  tankMesh.addQuad(Vector3(1, -0.5, -2), Vector3(1, 0.5, -2.2), Vector3(-1, 0.5, -2.2), Vector3(-1, -0.5, -2), GRAY)

  val turretMesh = Mesh(Buffer.empty, Buffer.empty)
  turretMesh.addQuad(Vector3(0.25, 0.5, 0.25), Vector3(0.25, 0.5, -0.25), Vector3(-0.25, 0.5, -0.25), Vector3(-0.25, 0.5, 0.25), LIGHT_GRAY)
  turretMesh.addQuad(Vector3(0.5, 0.0, 1), Vector3(0.25, 0.5, 0.25), Vector3(-0.25, 0.5, 0.25), Vector3(-0.5, 0.0, 1), LIGHT_GRAY)
  turretMesh.addQuad(Vector3(0.25, 0.5, -0.25), Vector3(0.5, 0.0, -0.7), Vector3(-0.5, 0.0, -0.7), Vector3(-0.25, 0.5, -0.25), LIGHT_GRAY)
  turretMesh.addQuad(Vector3(0.25, 0.5, 0.25), Vector3(0.5, 0.0, 1), Vector3(0.5, 0.0, -0.7), Vector3(0.25, 0.5, -0.25), LIGHT_GRAY)
  turretMesh.addQuad(Vector3(-0.25, 0.5, 0.25), Vector3(-0.5, 0.0, 1), Vector3(-0.5, 0.0, -0.7), Vector3(-0.25, 0.5, -0.25), LIGHT_GRAY)

  val cannonMesh = Mesh(Buffer.empty, Buffer.empty)
  //0.06, 2.0
  val cannonSize = 0.08
  val cannonZ = 2.2
  cannonMesh.addQuad(Vector3(cannonSize, cannonSize, cannonZ), Vector3(cannonSize, cannonSize, 0), Vector3(-cannonSize, cannonSize, 0), Vector3(-cannonSize, cannonSize, cannonZ), DARK_GRAY)
  cannonMesh.addQuad(Vector3(cannonSize, -cannonSize, cannonZ), Vector3(cannonSize, -cannonSize, 0), Vector3(-cannonSize, -cannonSize, 0), Vector3(-cannonSize, -cannonSize, cannonZ), DARK_GRAY)
  cannonMesh.addQuad(Vector3(cannonSize, cannonSize, cannonZ), Vector3(cannonSize, -cannonSize, cannonZ), Vector3(cannonSize, -cannonSize, 0), Vector3(cannonSize, cannonSize, 0), DARK_GRAY)
  cannonMesh.addQuad(Vector3(-cannonSize, cannonSize, cannonZ), Vector3(-cannonSize, -cannonSize, cannonZ), Vector3(-cannonSize, -cannonSize, 0), Vector3(-cannonSize, cannonSize, 0), DARK_GRAY)

  val wheelMesh = Mesh(Buffer.empty, Buffer.empty)
  //0.5
  val wSize = 0.16
  val wWidth = 0.16
  //Side 1
  wheelMesh.addQuad(Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, -wSize, wSize * 3), Vector3(wWidth, -wSize, -wSize * 3), Vector3(wWidth, wSize, -wSize * 3), DARK_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, wSize * 3, wSize), Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, wSize, -wSize * 3), Vector3(wWidth, wSize * 3, -wSize), DARK_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, -wSize, wSize * 3), Vector3(wWidth, -wSize * 3, wSize), Vector3(wWidth, -wSize * 3, -wSize), Vector3(wWidth, -wSize, -wSize * 3), DARK_GRAY)
  //Circle
  wheelMesh.addQuad(Vector3(wWidth, wSize * 3, wSize), Vector3(wWidth, wSize * 3, -wWidth), Vector3(-wWidth, wSize * 3, -wSize), Vector3(-wWidth, wSize * 3, wSize), LIGHT_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, wSize * 3, wSize), Vector3(-wWidth, wSize * 3, wSize), Vector3(-wWidth, wSize, wSize * 3), LIGHT_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, wSize * 3, -wSize), Vector3(wWidth, wSize, -wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), Vector3(-wWidth, wSize * 3, -wSize), LIGHT_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, -wSize * 3, wSize), Vector3(wWidth, -wSize * 3, -wWidth), Vector3(-wWidth, -wSize * 3, -wSize), Vector3(-wWidth, -wSize * 3, wSize), LIGHT_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, -wSize, wSize * 3), Vector3(wWidth, -wSize * 3, wSize), Vector3(-wWidth, -wSize * 3, wSize), Vector3(-wWidth, -wSize, wSize * 3), LIGHT_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, -wSize * 3, -wSize), Vector3(wWidth, -wSize, -wSize * 3), Vector3(-wWidth, -wSize, -wSize * 3), Vector3(-wWidth, -wSize * 3, -wSize), LIGHT_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, wSize, -wSize * 3), Vector3(wWidth, -wSize, -wSize * 3), Vector3(-wWidth, -wSize, -wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), LIGHT_GRAY)
  wheelMesh.addQuad(Vector3(wWidth, wSize, wSize * 3), Vector3(wWidth, -wSize, wSize * 3), Vector3(-wWidth, -wSize, wSize * 3), Vector3(-wWidth, wSize, wSize * 3), LIGHT_GRAY)
  //Side 2
  wheelMesh.addQuad(Vector3(-wWidth, wSize, wSize * 3), Vector3(-wWidth, -wSize, wSize * 3), Vector3(-wWidth, -wSize, -wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), DARK_GRAY)
  wheelMesh.addQuad(Vector3(-wWidth, wSize * 3, wSize), Vector3(-wWidth, wSize, wSize * 3), Vector3(-wWidth, wSize, -wSize * 3), Vector3(-wWidth, wSize * 3, -wSize), DARK_GRAY)
  wheelMesh.addQuad(Vector3(-wWidth, -wSize, wSize * 3), Vector3(-wWidth, -wSize * 3, wSize), Vector3(-wWidth, -wSize * 3, -wSize), Vector3(-wWidth, -wSize, -wSize * 3), DARK_GRAY)

  val cubeMesh = Mesh(Buffer.empty, Buffer.empty)
  val cubeSize = 1.0
  cubeMesh.addQuad(Vector3(cubeSize, cubeSize, cubeSize), Vector3(cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, cubeSize), Color(255, 70, 10))
  cubeMesh.addQuad(Vector3(cubeSize, -cubeSize, cubeSize), Vector3(cubeSize, -cubeSize, -cubeSize), Vector3(-cubeSize, -cubeSize, -cubeSize), Vector3(-cubeSize, -cubeSize, cubeSize), Color(255, 150, 10))
  cubeMesh.addQuad(Vector3(cubeSize, cubeSize, cubeSize), Vector3(cubeSize, -cubeSize, cubeSize), Vector3(cubeSize, -cubeSize, -cubeSize), Vector3(cubeSize, cubeSize, -cubeSize), Color(255, 100, 10))
  cubeMesh.addQuad(Vector3(-cubeSize, cubeSize, cubeSize), Vector3(-cubeSize, -cubeSize, cubeSize), Vector3(-cubeSize, -cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, -cubeSize), Color(255, 0, 0))
  cubeMesh.addQuad(Vector3(cubeSize, -cubeSize, cubeSize), Vector3(cubeSize, cubeSize, cubeSize), Vector3(-cubeSize, cubeSize, cubeSize), Vector3(-cubeSize, -cubeSize, cubeSize), Color(255, 70, 10))
  cubeMesh.addQuad(Vector3(cubeSize, -cubeSize, -cubeSize), Vector3(cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, -cubeSize, -cubeSize), Color(255, 150, 10))

  val cubeMesh2 = Mesh(Buffer.empty, Buffer.empty)
  cubeMesh2.addQuad(Vector3(cubeSize, cubeSize, cubeSize), Vector3(cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, cubeSize), Color(50, 50, 50))
  cubeMesh2.addQuad(Vector3(cubeSize, -cubeSize, cubeSize), Vector3(cubeSize, -cubeSize, -cubeSize), Vector3(-cubeSize, -cubeSize, -cubeSize), Vector3(-cubeSize, -cubeSize, cubeSize), Color(50, 50, 50))
  cubeMesh2.addQuad(Vector3(cubeSize, cubeSize, cubeSize), Vector3(cubeSize, -cubeSize, cubeSize), Vector3(cubeSize, -cubeSize, -cubeSize), Vector3(cubeSize, cubeSize, -cubeSize), Color(50, 50, 50))
  cubeMesh2.addQuad(Vector3(-cubeSize, cubeSize, cubeSize), Vector3(-cubeSize, -cubeSize, cubeSize), Vector3(-cubeSize, -cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, -cubeSize), Color(50, 50, 50))
  cubeMesh2.addQuad(Vector3(cubeSize, -cubeSize, cubeSize), Vector3(cubeSize, cubeSize, cubeSize), Vector3(-cubeSize, cubeSize, cubeSize), Vector3(-cubeSize, -cubeSize, cubeSize), Color(50, 50, 50))
  cubeMesh2.addQuad(Vector3(cubeSize, -cubeSize, -cubeSize), Vector3(cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, cubeSize, -cubeSize), Vector3(-cubeSize, -cubeSize, -cubeSize), Color(50, 50, 50))

  val mechHullMesh = generateBox(Vector3(0.7, 0.85, 0.5), Vector3(1, 0.7, 0.5), Color(80, 80, 80))
  val mechPartMesh = generateBox(Vector3(0.36, 0.36, 1), Vector3(0.36, 0.36, 0), Color(80, 80, 80))
  val mechJointMesh = generateBox(Vector3(0.25, 0.25, 0.25), Vector3(0.25, 0.25, 0.25), Color(30, 30, 30))
  val mechArmMesh = generateBox(Vector3(0.3, 0.3, 1.1), Vector3(0.35, 0.35, 0), Color(120, 120, 120))
  val mechArm2Mesh = generateBox(Vector3(0.25, 0.25, 1.7), Vector3(0.3, 0.3, 0), Color(120, 120, 120))
  val mechWeaponMesh = generateBox(Vector3(0.2, 0.3, 2), Vector3(0.2, 0.3, 0), Color(50, 50, 50))

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

