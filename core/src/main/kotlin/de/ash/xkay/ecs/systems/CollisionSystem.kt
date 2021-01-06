package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import de.ash.xkay.assets.TextureAsset
import de.ash.xkay.assets.get
import de.ash.xkay.ecs.components.*
import ktx.ashley.*
import ktx.assets.async.AssetStorage
import ktx.log.debug

/**
 * Handles the collision detection between different entities with hitboxes.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class CollisionSystem(
    private val assets: AssetStorage
) : IteratingSystem(
    allOf(HitboxComponent::class)
        .exclude(RemoveComponent::class)
        .get()
) {

    private val logger = ashLogger("CollisionSys")

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val hitbox = entity[HitboxComponent.mapper]?.hitbox
        require(hitbox != null) { "Entity $entity does not have a HitboxComponent" }

        // Check collision of player entity with all other entities
        entity[PlayerComponent.mapper]?.let {
            // Check hitbox against all other hitboxes in the game
            for (otherEntity in entities) {
                // Skip if its the same entity
                if (entity == otherEntity) continue

                // Check collision if other entity has hitbox
                otherEntity[HitboxComponent.mapper]?.let { other ->
                    if (hitbox.overlaps(other.hitbox)) { // Player collided with obstacle

                        entity.addComponent<RemoveComponent>(engine) {
                            delay = 1f
                        }
                        engine.entity {
                            val graphic = with<GraphicComponent> {
                                setSprite(assets[TextureAsset.EXPLOSION])
                            }

                            with<TransformComponent> {
                                setInitialPosition(hitbox.x, hitbox.y)
                                size.set(graphic.sprite.width, graphic.sprite.height)
                            }

                            with<RemoveComponent> {
                                delay = 0.9f
                            }
                        }
                    }
                }
            }
        }
    }
}