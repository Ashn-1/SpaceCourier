package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.RemoveComponent
import de.ash.xkay.events.GameEvent
import de.ash.xkay.events.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.get

/**
 * Removes all the entities that are marked by a [RemoveComponent] from the engine
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class RemoveSystem(
    private val eventManager: GameEventManager
) : IteratingSystem(allOf(RemoveComponent::class).get()) {

    private val logger = ashLogger("RemoveSys")

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // Get the necessary components
        val remove = entity[RemoveComponent.mapper]
        require(remove != null) { "Entity $entity does not have a RemoveComponent" }

        // Reduce delay of the RemoveComponent
        remove.delay -= deltaTime

        // Remove the entity from the engine if the remove delay is done
        if (remove.delay <= 0f) {

            // Player died
            entity[PlayerComponent.mapper]?.let { player ->
                eventManager.dispatchEvent(GameEvent.PlayerDeathEvent.apply {
                    highscore = player.highscore.toInt()
                })
            }

            engine.removeEntity(entity)
        }
    }
}
