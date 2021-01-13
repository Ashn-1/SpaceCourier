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
import de.ash.xkay.main.XkayRuntimeException
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.RemoveComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.ecs.components.VelocityComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.debug
import ktx.math.vec2
import javax.xml.crypto.dsig.Transform
import kotlin.math.abs

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class PlayerInputSystem(
    private val gameViewport: Viewport
) : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class, VelocityComponent::class)
        .exclude(RemoveComponent::class)
        .get()
) {

    /*
    private enum class Direction {
        NONE,
        LEFT,
        RIGHT
    }
    */

    private val logger = ashLogger("InputSys")

    val playerGestureDetection = GestureDetector(PlayerGestureListener())

    //private var lastTouchDirection = Direction.NONE

    private val touchPosition = Vector2()

    private val touchTolerance = 0.1f

    private val movementSpeed = 5f

    private var isDoubleClick = false

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }
        val velocity = entity[VelocityComponent.mapper]?.velocity
        require(velocity != null) { "Entity $entity does not have a VelocityComponent" }

        if (isDoubleClick) {
            isDoubleClick = false

            // TODO activate shield
            logger.debug { "Shields are activated" }
        }

        if (Gdx.input.isTouched) {
            // Get touch position and unproject them
            touchPosition.x = Gdx.input.x.toFloat()
            gameViewport.unproject(touchPosition)

            // Set velocity of the player
            velocity.x = MathUtils.clamp(touchPosition.x - transform.position.x, -movementSpeed, movementSpeed)
            if (abs(velocity.x) < touchTolerance) {
                velocity.x = 0f
            }
        } else {
            // Stop movement
            velocity.x = 0f
        }

        /* OLD INPUT HANDLING
        // Check in which direction the player should move
        val touchPos1 = vec2(x = Gdx.input.getX(0).toFloat())
        val touchPos2 = vec2(x = Gdx.input.getX(1).toFloat())

        // Convert screen coordinates to world coordinates
        gameViewport.unproject(touchPos1)
        gameViewport.unproject(touchPos2)

        // Check which side were pressed
        val isLeftPressed =
            ((Gdx.input.isTouched(0) && touchPos1.x < gameViewport.worldWidth / 2) || Gdx.input.isTouched(1) && touchPos2.x < gameViewport.worldWidth / 2)
        val isRightPressed =
            ((Gdx.input.isTouched(0) && touchPos1.x >= gameViewport.worldWidth / 2) || Gdx.input.isTouched(1) && touchPos2.x >= gameViewport.worldWidth / 2)

        // Inputs are handled differently for different operating systems
        when (Gdx.app.type) {
            Application.ApplicationType.Android, Application.ApplicationType.Desktop -> {
                when { // Split according to the side that was pressed
                    !isLeftPressed && !isRightPressed -> { // No press
                        velocity.velocity.x = 0f
                        lastTouchDirection = Direction.NONE
                    }
                    !isLeftPressed && isRightPressed -> { // Only right press
                        velocity.velocity.x = movementSpeed
                        lastTouchDirection = Direction.RIGHT
                    }
                    isLeftPressed && !isRightPressed -> { // Only left press
                        velocity.velocity.x = -movementSpeed
                        lastTouchDirection = Direction.LEFT
                    }
                    else -> { // Both pressed, which needs special handling
                        when (lastTouchDirection) { // Direction dependend on which side was pressed first
                            Direction.LEFT -> { // Left was pressed first and then right
                                velocity.velocity.x = movementSpeed // -> go right because latest touch
                                lastTouchDirection = Direction.NONE
                            }
                            Direction.RIGHT -> { // Right was pressed first and then right
                                velocity.velocity.x = -movementSpeed // -> go left because latest touch
                                lastTouchDirection = Direction.NONE
                            }
                            else -> {
                            }
                        }
                    }
                }
            }

            else -> throw XkayRuntimeException("OS ${Gdx.app.type} not supported")
        }
        */
    }

    /**
     * True only if a player entity is in the engine -> only the case during the IngameScreen
     */
    fun isActive() = entities.size() > 0

    inner class PlayerGestureListener : GestureDetector.GestureAdapter() {
        override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
            if (!isActive()) return false
            if (count >= 2) {
                isDoubleClick = true
                return true
            }
            return false
        }
    }
}