package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.XkayRuntimeException
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.ecs.components.VelocityComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.math.vec2

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class InputSystem(
    private val gameViewport: Viewport
) : IteratingSystem(
    allOf(PlayerComponent::class, VelocityComponent::class).get()
) {

    private enum class Direction {
        NONE,
        LEFT,
        RIGHT
    }

    private val logger = ashLogger("InputSys")

    private var lastTouchDirection = Direction.NONE

    private val touchPositionTmp = Vector2()

    private val movementSpeed = 5f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val velocity = entity[VelocityComponent.mapper]
        require(velocity != null) { "Entity $entity does not have a VelocityComponent" }

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
        // TODO change desktop input to keyboard or follow mouse
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
    }
}