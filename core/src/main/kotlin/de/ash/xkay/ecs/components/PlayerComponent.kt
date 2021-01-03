package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class PlayerComponent : Component, Pool.Poolable {



    override fun reset() {
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}