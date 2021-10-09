package de.ashenlab.spacecourier.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import de.ashenlab.spacecourier.ecs.components.*
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get

/**
 * Updates the position of an entity based on the entity that it is attached to. If an entity is removed from the engine, all entities that are attached to it are also marked for removal.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class AttachSystem : EntityListener, IteratingSystem(
    allOf(AttachComponent::class, TransformComponent::class).get()
) {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val attach = entity[AttachComponent.mapper]
        require(attach != null) { "Entity $entity does not have a AttachComponent" }
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }

        attach.entity[TransformComponent.mapper]?.let { attachTransform ->
            transform.interpolatedPosition.set(attachTransform.interpolatedPosition).add(attach.offset)
        }

        entity[GraphicComponent.mapper]?.let { graphic ->
            attach.entity[GraphicComponent.mapper]?.let { attachGraphic ->
                graphic.sprite.setAlpha(attachGraphic.sprite.color.a)
            }
        }
    }

    override fun entityAdded(entity: Entity) = Unit

    override fun entityRemoved(entity: Entity) {
        entities.forEach {
            it[AttachComponent.mapper]?.let { attach ->
                if (attach.entity == entity) {
                    it.addComponent<RemoveComponent>(engine)
                }
            }
        }
    }
}
