package de.ashenlab.spacecourier.ecs

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import de.ashenlab.spacecourier.assets.AtlasAnimationAsset
import de.ashenlab.spacecourier.assets.AtlasAsset
import de.ashenlab.spacecourier.assets.get
import de.ashenlab.spacecourier.ecs.components.*
import de.ashenlab.spacecourier.main.UNIT_SCALE
import de.ashenlab.spacecourier.main.XkayRuntimeException
import de.ashenlab.spacecourier.ecs.components.VelocityComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage

/*
 * Contains presets for some entities as extension functions for [Engine].
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

private const val PLAYER_Y_VIEWPORT_PERCENT: Float = 0.33f

fun Engine.createPlayer(
    assets: AssetStorage,
    gameViewport: Viewport
) : Entity {
    val player = entity {

        val graphic = with<GraphicComponent> {
            setSprite(assets[AtlasAsset.PLAYER_SHIP])
        }

        val transform = with<TransformComponent> {
            setInitialPosition(
                gameViewport.worldWidth * 0.5f,
                gameViewport.worldHeight * PLAYER_Y_VIEWPORT_PERCENT
            )
            size.set(graphic.sprite.width, graphic.sprite.height)
            depthLevel = 1
        }

        with<HitboxComponent> {
            hitbox.setPosition(transform.position)
            hitbox.radius = graphic.sprite.height * 0.5f
        }

        with<PlayerComponent>()
        with<VelocityComponent>()
    }

    // Create engine fire entity
    entity { // Fire engine
        val fireAnimation = with<AnimationComponent> {
            frames = assets[AtlasAnimationAsset.ENGINE_FIRE]
            frameDuration = 1 / 10f
            playMode = AnimationComponent.PlayMode.LOOP
        }
        val fireGraphic = with<GraphicComponent> {
            setSprite(fireAnimation.getCurrentFrame())
        }
        with<TransformComponent> {
            size.set(fireGraphic.sprite.width, fireGraphic.sprite.height)
        }
        with<AttachComponent> {
            entity = player
            offset.set(0f, -6f * UNIT_SCALE)
        }
    }

    return player
}

fun Engine.createShield(assets: AssetStorage, player: Entity, activeTime: Float) : Entity {
    return entity {
        val shieldGraphic = with<GraphicComponent> {
            setSprite(assets[AtlasAsset.SHIELD])
        }
        with<TransformComponent> {
            size.set(shieldGraphic.sprite.width, shieldGraphic.sprite.height)
            depthLevel = 2
        }
        with<AttachComponent> {
            entity = player
        }
        with<RemoveComponent> {
            delay = activeTime
        }
    }
}

fun Engine.createAsteroid(
    asteroid: AtlasAsset,
    assets: AssetStorage,
    gameViewport: Viewport,
) : Entity {
    val playerY = gameViewport.worldHeight * PLAYER_Y_VIEWPORT_PERCENT

    return entity {

        val graphic = with<GraphicComponent> {
            setSprite(assets[asteroid])
            sprite.rotation = MathUtils.random(360f)
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
        }

        with<VelocityComponent> {
            velocity.x = MathUtils.random(-0.25f, 0.25f)
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

            depthLevel = -1

            size.set(graphic.sprite.width, graphic.sprite.height)
        }

        with<VelocityComponent> {
            velocity.y = MathUtils.random(-0.75f, -0.5f)
        }
    }
}

fun Engine.createExplosion(
    assets: AssetStorage,
    x: Float,
    y: Float
) : Entity {
    return entity {
        val animation = with<AnimationComponent> {
            frames = assets[AtlasAnimationAsset.EXPLOSION]
            frameDuration = 0.9f / frames.size
        }

        val graphic = with<GraphicComponent> {
            setSprite(animation.getCurrentFrame())
        }

        with<TransformComponent> {
            setInitialPosition(x, y)
            size.set(graphic.sprite.width, graphic.sprite.height)
            depthLevel = 1
        }

        with<RemoveComponent> {
            delay = 0.9f
        }
    }
}
