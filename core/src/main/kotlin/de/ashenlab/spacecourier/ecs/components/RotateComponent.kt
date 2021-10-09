package de.ashenlab.spacecourier.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class RotateComponent : Component, Pool.Poolable {

    var degreesPerSecond = 0f

    override fun reset() {
        degreesPerSecond = 0f
    }

    companion object {
        val mapper = mapperFor<RotateComponent>()
    }
}