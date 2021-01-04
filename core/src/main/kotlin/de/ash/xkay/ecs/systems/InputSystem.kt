package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.ecs.components.VelocityComponent
import ktx.ashley.allOf
import ktx.ashley.get

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class InputSystem(
    private val gameViewport: Viewport
) : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class, VelocityComponent::class).get()
) {

    private enum class Direction {
        NONE,
        LEFT,
        RIGHT
    }

    private val logger = ashLogger("InputSys")

    private var direction = Direction.NONE

    private val lastTouchPosition = Vector2()

    private val movementSpeed = 5f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }
        val velocity = entity[VelocityComponent.mapper]
        require(velocity != null) { "Entity $entity does not have a VelocityComponent" }

        direction = if (Gdx.input.isTouched) {
            lastTouchPosition.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            gameViewport.unproject(lastTouchPosition)

            if (lastTouchPosition.x < gameViewport.worldWidth / 2f) Direction.LEFT
            else Direction.RIGHT

        } else {
            Direction.NONE
        }

        velocity.velocity.x = when(direction) {
            Direction.NONE -> 0f
            Direction.LEFT -> -movementSpeed
            Direction.RIGHT -> movementSpeed
        }
    }
}