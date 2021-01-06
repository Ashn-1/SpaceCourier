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
    val prevPosition = Vector2()
    val interpolatedPosition = Vector2()

    val size = Vector2()

    var rotationDeg = 0f

    override fun reset() {
        position.set(Vector2.Zero)
        prevPosition.set(Vector2.Zero)
        interpolatedPosition.set(Vector2.Zero)

        size.set(Vector2.Zero)

        rotationDeg = 0f
    }

    fun setInitialPosition(x: Float, y: Float) {
        position.set(x, y)
        prevPosition.set(x, y)
        interpolatedPosition.set(x, y)
    }

    fun setInitialPosition(pos: Vector2) {
        setInitialPosition(pos.x, pos.y)
    }

    override fun compareTo(other: TransformComponent): Int {
        return position.y.compareTo(other.position.y)
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}