package de.ash.xkay.ecs

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.assets.AtlasAsset
import de.ash.xkay.assets.get
import de.ash.xkay.ecs.components.*
import de.ash.xkay.main.XkayRuntimeException
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
    asteroid: AtlasAsset,
    assets: AssetStorage,
    gameViewport: Viewport,
) : Entity {
    return entity {

        val graphic = with<GraphicComponent> {
            setSprite(assets[asteroid])
        }

        val transform = with<TransformComponent> {
            setInitialPosition(
                MathUtils.random(
                    0f + graphic.sprite.width * 0.5f,
                    gameViewport.worldWidth - graphic.sprite.width * 0.5f
                ),
                MathUtils.random(gameViewport.worldHeight * 0.1f + graphic.sprite.height) + gameViewport.worldHeight
            )

            size.set(graphic.sprite.width, graphic.sprite.height)

            rotationDeg = MathUtils.random(360f)
        }

        with<VelocityComponent> {
            velocity.y = MathUtils.random(-3f, -2f)
        }

        with<RotateComponent> {
            degreesPerSecond = MathUtils.random(-45f, 45f)
        }

        with<HitboxComponent> {
            hitbox.setPosition(transform.position)
            hitbox.radius = when (asteroid) {
                AtlasAsset.ASTEROID_SMALL -> graphic.sprite.height * 0.33f
                AtlasAsset.ASTEROID_MID_1 -> graphic.sprite.height * 0.45f
                AtlasAsset.ASTEROID_MID_2 -> graphic.sprite.height * 0.33f
                AtlasAsset.ASTEROID_BIG -> graphic.sprite.height * 0.45f
                else -> {
                    throw XkayRuntimeException("Given AtlasAsset is not an asteroid: ${asteroid.name}")
                }
            }
        }
    }
}

fun Engine.createStar(
    star: AtlasAsset,
    assets: AssetStorage,
    gameViewport: Viewport,
    onGameScreen: Boolean = false
) : Entity {

    return entity {

        val graphic = with<GraphicComponent> {
            setSprite(assets[star])
        }

        val yCoord = if (onGameScreen) MathUtils.random(gameViewport.worldHeight)
            else MathUtils.random(gameViewport.worldHeight * 0.1f) + gameViewport.worldHeight
        with<TransformComponent> {
            setInitialPosition(
                MathUtils.random(gameViewport.worldWidth),
                yCoord
            )

            size.set(graphic.sprite.width, graphic.sprite.height)
        }

        with<VelocityComponent> {
            velocity.y = MathUtils.random(-0.75f, -0.5f)
        }
    }
}
