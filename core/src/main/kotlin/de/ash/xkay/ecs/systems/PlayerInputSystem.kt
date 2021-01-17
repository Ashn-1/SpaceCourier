package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.RemoveComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.ecs.components.VelocityComponent
import de.ash.xkay.ecs.createShield
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.assets.async.AssetStorage
import ktx.log.debug
import ktx.log.error
import kotlin.math.abs
import kotlin.math.max

const val SHIELD_ACTIVE_TIME = 2.0f
const val SHIELD_COOLDOWN = 10.0f

/**
 * Handles the player input and adjusts the player entity accordingly (velocity, skills, etc...).
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class PlayerInputSystem(
    private val assets: AssetStorage,
    private val gameViewport: Viewport
) : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class, VelocityComponent::class)
        .exclude(RemoveComponent::class)
        .get()
) {
    private val logger = ashLogger("InputSys")

    val playerGestureDetection = GestureDetector(PlayerGestureListener())

    val playerInput = PlayerInputAdapter()

    private val touchPosition = Vector2()

    private val touchTolerance = 0.1f

    private val movementSpeed = 5f

    private var activateShield = false

    private var isMovingDesktop = false

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val player = entity[PlayerComponent.mapper]
        require(player != null) { "Entity $entity does not have a PlayerComponent" }
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }
        val velocity = entity[VelocityComponent.mapper]?.velocity
        require(velocity != null) { "Entity $entity does not have a VelocityComponent" }

        // Shield logic
        player.shieldCooldown = max(0.0f, player.shieldCooldown - deltaTime)

        if (player.isShieldActivated) {
            player.shieldActiveTime = player.shieldActiveTime - deltaTime
            if (player.shieldActiveTime < 0.0f) {
                player.isShieldActivated = false
                player.shieldActiveTime = 0.0f
            }
        }

        if (activateShield) {
            if (player.shieldCooldown == 0.0f) {
                engine.createShield(assets, entity, SHIELD_ACTIVE_TIME)
                player.isShieldActivated = true
                player.shieldActiveTime = SHIELD_ACTIVE_TIME
                player.shieldCooldown = SHIELD_COOLDOWN

                activateShield = false
                logger.debug { "Shields are activated" }
            } else {
                activateShield = false
                logger.debug { "Shield is still on cooldown" }
            }
        }

        // Movement logic
        when (Gdx.app.type) {
            Application.ApplicationType.Android -> {
                if (Gdx.input.isTouched) {
                    // Get touch position and unproject them
                    touchPosition.x = Gdx.input.x.toFloat()
                    gameViewport.unproject(touchPosition)

                    setPlayerVelocity(transform.position, velocity)
                } else {
                    // Stop movement
                    velocity.x = 0f
                }
            }
            Application.ApplicationType.Desktop -> {
                if (isMovingDesktop) {
                    setPlayerVelocity(transform.position, velocity)
                } else {
                    velocity.x = 0f
                }
            }
            else -> logger.error { "OS not supported" }
        }
    }

    private fun setPlayerVelocity(position: Vector2, velocity: Vector2) {
        velocity.x = MathUtils.clamp(touchPosition.x - position.x, -movementSpeed, movementSpeed)
        if (abs(velocity.x) < touchTolerance) {
            velocity.x = 0f
        }
    }

    /**
     * True only if a player entity is in the engine -> only the case during the IngameScreen
     */
    fun isActive() = entities.size() > 0


    inner class PlayerGestureListener : GestureDetector.GestureAdapter() {
        override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
            if (!isActive()) return false
            if (count >= 2) {
                activateShield = true
                return true
            }
            return false
        }
    }


    inner class PlayerInputAdapter : InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            if (!isActive()) return false

            if (Gdx.app.type == Application.ApplicationType.Desktop) {
                when (keycode) {
                    Input.Keys.A, Input.Keys.LEFT -> {
                        touchPosition.x = 0.0f
                        isMovingDesktop = true
                    }
                    Input.Keys.D, Input.Keys.RIGHT -> {
                        touchPosition.x = gameViewport.worldWidth
                        isMovingDesktop = true
                    }
                    Input.Keys.SPACE -> {
                        activateShield = true
                    }
                    else -> return false
                }
            }
            return true
        }

        override fun keyUp(keycode: Int): Boolean {
            if (!isActive()) return false

            if (Gdx.app.type == Application.ApplicationType.Desktop) {
                when (keycode) {
                    Input.Keys.A, Input.Keys.LEFT -> {
                        if (touchPosition.x == 0.0f)
                            isMovingDesktop = false
                    }
                    Input.Keys.D, Input.Keys.RIGHT -> {
                        if (touchPosition.x == gameViewport.worldWidth)
                            isMovingDesktop = false
                    }
                    else -> return false
                }
            }
            return true
        }
    }
}