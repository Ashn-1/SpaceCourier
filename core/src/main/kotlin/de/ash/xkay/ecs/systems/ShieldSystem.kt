package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import de.ash.xkay.ecs.components.RemoveComponent
import de.ash.xkay.ecs.components.ShieldComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.remove
import ktx.log.debug

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class ShieldSystem : IteratingSystem(
    allOf(ShieldComponent::class).get()
) {

    private val logger = ashLogger("ShieldSys")

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[ShieldComponent.mapper]?.let { shield ->
            shield.shieldActiveTime -= deltaTime
            shield.shieldCooldown -= deltaTime

            if (shield.shieldCooldown < 0.0f) {
                entity.remove<ShieldComponent>()
                logger.debug { "Shield removed" }
            }
        }
    }
}