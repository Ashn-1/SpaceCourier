package de.ashenlab.spacecourier.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Attaches one entity to another with a given offset to that entity.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class AttachComponent : Component, Pool.Poolable {

    lateinit var entity: Entity
    val offset = Vector2()

    override fun reset() {
        offset.set(Vector2.Zero)
    }

    companion object {
        val mapper = mapperFor<AttachComponent>()
    }
}