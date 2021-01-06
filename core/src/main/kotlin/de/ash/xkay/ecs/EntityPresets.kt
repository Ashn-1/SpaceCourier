package de.ash.xkay.ecs

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.assets.AtlasAsset
import de.ash.xkay.assets.TextureAsset
import de.ash.xkay.assets.get
import de.ash.xkay.ecs.components.*
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage

/**
 * Contains presets for some entities as extension functions for [Engine].
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

fun Engine.createPlayer(
    assets: AssetStorage,
    gameViewport: Viewport
) : Entity {
    return entity {
        val graphic = with<GraphicComponent> {
            setSprite(assets[AtlasAsset.PLAYER_BASE_SHIP])
        }

        val transform = with<TransformComponent> {
            setInitialPosition(gameViewport.worldWidth * 0.5f, gameViewport.worldHeight * 0.25f)
            size.set(graphic.sprite.width, graphic.sprite.height)
        }

        with<HitboxComponent> {
            hitbox.setPosition(transform.position)
            hitbox.radius = graphic.sprite.height * 0.5f
        }

        with<PlayerComponent>()
        with<VelocityComponent>()
    }
}

fun Engine.createAsteroid(
    assets: AssetStorage,
    gameViewport: Viewport
) : Entity {
    return entity {
        val graphic = with<GraphicComponent> {
            setSprite(assets[AtlasAsset.ASTEROID_BASIC])
        }

        val transform = with<TransformComponent> {
            setInitialPosition(
                MathUtils.random(gameViewport.worldWidth - graphic.sprite.width * 0.66f),
                MathUtils.random(gameViewport.worldHeight * 0.1f + graphic.sprite.height) + gameViewport.worldHeight
            )

            size.set(graphic.sprite.width, graphic.sprite.height)

            rotationDeg = MathUtils.random(360f)
        }

        with<VelocityComponent> {
            velocity.y = -2.5f
        }

        with<HitboxComponent> {
            hitbox.setPosition(transform.position)
            hitbox.radius = graphic.sprite.height * 0.33f
        }
    }
}
