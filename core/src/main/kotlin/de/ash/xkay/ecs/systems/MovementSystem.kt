package de.ash.xkay.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.ecs.components.VelocityComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.math.plus
import ktx.math.plusAssign

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class MovementSystem : IteratingSystem(
    allOf(TransformComponent::class, VelocityComponent::class).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }
        val velocity = entity[VelocityComponent.mapper]
        require(velocity != null) { "Entity $entity does not have a VelocityComponent" }

        transform.position.plusAssign(velocity.velocity)
    }
}