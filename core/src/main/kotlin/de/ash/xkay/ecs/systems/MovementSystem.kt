package de.ash.xkay.ecs.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.XkayRuntimeException
import de.ash.xkay.ecs.components.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.math.plusAssign

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class MovementSystem(
    private val gameViewport: Viewport
) : IteratingSystem(
    allOf(TransformComponent::class, VelocityComponent::class)
        .exclude(RemoveComponent::class)
        .get()
) {

    // UPS for the movement system are set to 30
    private val updateRate = 1 / 30f

    private val pointsPerSecond = 50f

    private var accumulator = 0f

    override fun update(deltaTime: Float) {

        /*
         * This system prevents the update to the movement to be done too fast in case the framerate dips for a short time. Then the delta time would be high and the movement very large, which could break game logic.
         */
        accumulator += deltaTime
        while (accumulator >= updateRate) {
            accumulator -= updateRate

            super.update(updateRate)
        }

        // Update the interpolated position based on the accumulator value
        val alpha = accumulator / updateRate
        entities.forEach { entity ->
            entity[TransformComponent.mapper]?.let { transform ->
                transform.interpolatedPosition.set(
                    MathUtils.lerp(transform.prevPosition.x, transform.position.x, alpha),
                    MathUtils.lerp(transform.prevPosition.y, transform.position.y, alpha)
                )
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }
        val velocity = entity[VelocityComponent.mapper]
        require(velocity != null) { "Entity $entity does not have a VelocityComponent" }

        // Save the position before updating it -> used for interpolated position updates
        transform.prevPosition.set(transform.position)

        // Update the position based on the velocity
        transform.position.mulAdd(velocity.velocity, deltaTime)

        // Player entity needs some special handling
        entity[PlayerComponent.mapper]?.let { // if not null

            // Clamp the player position so he doesn't go out of bounds
            transform.position.x = MathUtils.clamp(
                transform.position.x,
                0f + transform.size.x / 2f,
                gameViewport.worldWidth - transform.size.x / 2f
            )

            // Increase player highscore
            it.highscore += (pointsPerSecond * deltaTime)
        }

        // Update hitbox positions if available
        entity[HitboxComponent.mapper]?.hitbox?.setPosition(transform.position)
    }
}