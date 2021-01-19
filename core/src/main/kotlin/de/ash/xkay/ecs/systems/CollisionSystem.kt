package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import de.ash.xkay.assets.*
import de.ash.xkay.audio.AudioService
import de.ash.xkay.ecs.components.*
import de.ash.xkay.ecs.createExplosion
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

            // Skip collision detection if shield is active
            if (entity[ShieldComponent.mapper]?.isActive() == true) {
                return
            }

            // Check hitbox against all other hitboxes in the game
            for (otherEntity in entities) {
                // Skip if its the same entity
                if (entity == otherEntity) continue

                // Check collision if other entity has hitbox
                otherEntity[HitboxComponent.mapper]?.let { other ->
                    if (hitbox.overlaps(other.hitbox)) {
                        // Player collided with obstacle -> kill him

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