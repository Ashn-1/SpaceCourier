package de.ash.xkay.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import de.ash.xkay.assets.AtlasAsset
import de.ash.xkay.assets.get
import de.ash.xkay.ecs.components.GraphicComponent
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.RemoveComponent
import de.ash.xkay.ecs.components.VelocityComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.assets.async.AssetStorage

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class PlayerAnimationSystem(private val assets: AssetStorage) : IteratingSystem(
    allOf(PlayerComponent::class, VelocityComponent::class, GraphicComponent::class)
        .exclude(RemoveComponent::class)
        .get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[VelocityComponent.mapper]?.velocity?.let { velocity ->
            entity[GraphicComponent.mapper]?.let { graphic ->
                val textureRegion = when {
                    velocity.x < 0f -> assets[AtlasAsset.PLAYER_SHIP_LEFT]
                    velocity.x > 0f -> assets[AtlasAsset.PLAYER_SHIP_RIGHT]
                    else -> assets[AtlasAsset.PLAYER_SHIP]
                }
                graphic.setSprite(textureRegion)
            }
        }
    }
}