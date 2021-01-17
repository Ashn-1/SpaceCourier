package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {

    val position = Vector2()
    val prevPosition = Vector2()
    val interpolatedPosition = Vector2()

    var depthLevel = 0

    val size = Vector2()

    var rotationDeg = 0f

    override fun reset() {
        position.set(Vector2.Zero)
        prevPosition.set(Vector2.Zero)
        interpolatedPosition.set(Vector2.Zero)

        depthLevel = 0

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
        return if (depthLevel == other.depthLevel) position.y.compareTo(other.position.y)
            else depthLevel.compareTo(other.depthLevel)
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}