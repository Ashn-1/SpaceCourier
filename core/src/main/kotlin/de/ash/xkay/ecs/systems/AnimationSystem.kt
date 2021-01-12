package de.ash.xkay.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import de.ash.xkay.ecs.components.AnimationComponent
import de.ash.xkay.ecs.components.GraphicComponent
import ktx.ashley.allOf
import ktx.ashley.get

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class AnimationSystem : IteratingSystem(
    allOf(AnimationComponent::class, GraphicComponent::class).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animation = entity[AnimationComponent.mapper]
        require(animation != null) { "Entity $entity does not have a AnimationComponent" }
        val graphics = entity[GraphicComponent.mapper]
        require(graphics != null) { "Entity $entity does not have a GraphicComponent" }

        if (animation.update(deltaTime)) {
            graphics.setSprite(animation.getCurrentFrame())
        }
    }
}