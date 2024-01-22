package physics

import game.SimulationControl
import mathematics.*
import rendering.RenderObject

/**
 * Detects collisions with the terrain and changes the properties of the particle during the particle's lifetime.
 *
 * @param simulation
 *  The simulation of the particle
 * @param render
 *  RenderObject of this particle
 * @param trail
 *  If true, the particle will spawn a trail of particles
 * @param gravityMultiplier
 *  The acceleration caused by the planet's gravity if multiplied by this
 * @param size
 *  Size of the particle
 * @param time
 *  Maximum lifetime of the particle
 */
class ParticleCollision(simulation: SimulationControl, render: RenderObject, trail: Boolean, gravityMultiplier: Double, size: Double, time: Double) extends CollisionSystem:
  var nextTrail = 0.0
  var trailTimeMulti = 0.7
  var timeLeft = time

  override def update(position: Vector3, velocity: Vector3, rotation: Quaternion, rotationRate: Quaternion, mass: Double): (Vector3, Vector3, Quaternion, Quaternion, Boolean) =
    var vel = velocity + Vector3(0, -simulation.gravity * gravityMultiplier, 0) * simulation.simulationTime

    val h = position.y - simulation.terrain.getHeight(position) - simulation.terrain.position.y

    nextTrail -= simulation.simulationTime
    timeLeft -= simulation.simulationTime

    val timeMulti = math.max(timeLeft / (time + 0.001), 0.01)
    render.scale = timeMulti * size

    if(trail && nextTrail < 0) then
      simulation.spawnParticles(position, false, 0.7, 0.0, 1.0, render.scale, trailTimeMulti * timeMulti, 0, 1)
      nextTrail = 0.02 + trailTimeMulti * 0.01

    (position, vel, rotation, rotationRate, (timeLeft < 0) || (h < -size * 2.0))
