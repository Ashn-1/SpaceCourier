package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.ecs.components.VelocityComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.math.minus
import ktx.math.minusAssign

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class InputSystem(
    val gameViewport: Viewport
) : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class, VelocityComponent::class).get()
) {

    private val logger = ashLogger("InputSys")

    private val lastTouchPosition = Vector2()

    private val movementSpeed = 10f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }
        val velocity = entity[VelocityComponent.mapper]
        require(velocity != null) { "Entity $entity does not have a VelocityComponent" }

        if (Gdx.input.isTouched) {
            lastTouchPosition.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            gameViewport.unproject(lastTouchPosition)

            velocity.velocity
                .set(lastTouchPosition.x, lastTouchPosition.y)
                .sub(transform.position)
                .setLength(movementSpeed * deltaTime)

        } else {
            velocity.velocity.set(Vector2.Zero)
        }
    }
}