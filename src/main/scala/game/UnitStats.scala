package game

import game.*
import mathematics.*

/**
 * Container for information about the unit's current state.
 *
 * @param health
 *  The amount of damage this unit can take before being destroyed
 * @param armor
 *  When hit, the incoming damage is reduced by this amount, cannot be reduced to less than zero
 * @param radius
 *  The radius within which bullets can damage this unit
 * @param explosion
 *  Magnitude of the explosion when this unit is destroyed
 */
class UnitStats(var health: Double, var armor: Double, var radius: Double, var explosion: Double):
  val maxHealth = health
