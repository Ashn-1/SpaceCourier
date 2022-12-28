package de.ashenlab.spacecourier.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import de.ashenlab.spacecourier.audio.AudioService
import de.ashenlab.spacecourier.ecs.components.*
import de.ashenlab.spacecourier.ecs.createExplosion
import de.ashenlab.spacecourier.assets.SoundAsset
import ktx.ashley.*
import ktx.assets.async.AssetStorage

/**
 * Handles the collision detection between different entities with hitboxes.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class CollisionSystem(
    private val assets: AssetStorage,
    private val audioService: AudioService
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

            val isShieldActive: Boolean = entity[ShieldComponent.mapper]?.isActive() ?: false

            // Check hitbox against all other hitboxes in the game
            for (otherEntity in entities) {
                // Skip if its the same entity
                if (entity == otherEntity) continue

                // Check collision if other entity has hitbox
                otherEntity[HitboxComponent.mapper]?.let { other ->
                    // Check if a collision between the two entities happened
                    if (hitbox.overlaps(other.hitbox)) {
                        // Handle collision depending on player shield status
                        if (isShieldActive) { // -> destroy other entity
                            otherEntity.addComponent<RemoveComponent>(engine)
                            audioService.play(SoundAsset.EXPLOSION)
                            Gdx.input.vibrate(100)

                        } else { // -> kill player
                            // Schedule player entity for removal
                            entity.addComponent<RemoveComponent>(engine) {
                                delay = 1f
                            }

                            // Set player to invisible
                            entity[GraphicComponent.mapper]?.sprite?.setAlpha(0f)

                            // Add explosion entity
                            engine.createExplosion(assets, hitbox.x, hitbox.y)
                            audioService.play(SoundAsset.EXPLOSION)

                            // Vibrate the phone
                            Gdx.input.vibrate(500)
                        }
                    }
                }
            }
        }
    }
}