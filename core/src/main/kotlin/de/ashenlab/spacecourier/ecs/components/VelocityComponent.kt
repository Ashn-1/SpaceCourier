package de.ashenlab.spacecourier.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class VelocityComponent : Component, Pool.Poolable {

    val velocity = Vector2()

    override fun reset() {
        velocity.set(Vector2.Zero)
    }

    companion object {
        val mapper = mapperFor<VelocityComponent>()
    }
}