package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class HitboxComponent : Component, Pool.Poolable {

    var hitbox: Circle = Circle()

    override fun reset() {
        hitbox.set(0f, 0f, 0f)
    }

    companion object {
        val mapper = mapperFor<HitboxComponent>()
    }
}