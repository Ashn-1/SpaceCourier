package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import javax.xml.crypto.dsig.Transform

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {

    val position = Vector2()

    override fun reset() {
        position.set(Vector2.Zero)
    }

    override fun compareTo(other: TransformComponent): Int {
        return position.y.compareTo(other.position.y)
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }


}