package physics

import game.*
import mathematics.*
import rendering.RenderObject

/**
 * Collision system for bullets, it detects collisions with the terrain and all units except the unit that fired the bullet.
 *
 * @param simulation
 *  Simulation that contains the bullet
 * @param kinetic
 *  Base kinetic damage of the bullet
 * @param explosive
 *  Magnitude of the explosion caused by this bullet on impact
 * @param sourceUnit
 *  The unit that has the weapon that spawned this bullet
 */
class BulletCollision(simulation: SimulationControl, var kinetic: Double, explosive: Double, sourceUnit: UnitObject) extends CollisionSystem:
  val airResistance = simulation.calculateBulletAirResistance(kinetic, explosive)
  override def update(position: Vector3, velocity: Vector3, rotation: Quaternion, rotationRate: Quaternion, mass: Double): (Vector3, Vector3, Quaternion, Quaternion, Boolean) =
    var vel = velocity + Vector3(0, -simulation.gravity, 0) * simulation.simulationTime
    vel = vel - vel * simulation.simulationTime * airResistance

    var destroyed = false

    //Unit collision
    var inProgress = true
    var index = 0
    val dist = 1.5
    while(inProgress && index < simulation.units.length) do
      val unit = simulation.units(index)
      if(unit != sourceUnit && Vector3.distanceSqr(unit.position, position) < unit.stats.radius * unit.stats.radius) then
        val energy = calculateKineticEnergy(kinetic, vel, unit.velocity)
        val dmg = unit.calculateDamage(energy)
        if(dmg._1 <= 0) then simulation.spawnImpactParticles(position, energy * 0.5)
        if(dmg._2 <= 0) then
          calculateExplosion(position)
          destroyed = true
        else
          kinetic = kinetic * dmg._2
        inProgress = false

      index += 1

    //Terrain collision
    val h = simulation.terrain.getHeight(position) + simulation.terrain.position.y
    val d = position.y - h

    if(d < 0) then
      if(!destroyed) then
        val terrainPos = Vector3(position.x, h, position.z)
        calculateExplosion(terrainPos)
        simulation.spawnImpactParticles(terrainPos, calculateKineticEnergy(kinetic, vel, Vector3(0, 0, 0)))
      destroyed = true
    (position, vel, rotation, rotationRate, destroyed)

  def calculateKineticEnergy(mass: Double, a: Vector3, b: Vector3): Double =
    val v = (a - b).magnitude
    0.5 * mass * v * v

  def calculateExplosion(pos: Vector3) =
    simulation.spawnExplosion(pos, explosive)
    for(u <- simulation.units) do
      var distance = Vector3.distanceSqr(u.position, pos)
      if(distance <= u.stats.radius * u.stats.radius) then
        distance = u.stats.radius
      else
        distance -= u.stats.radius * u.stats.radius + u.stats.radius
      distance = 1.0 / distance
      val dmg = explosive * distance
      u.calculateDamage(dmg)