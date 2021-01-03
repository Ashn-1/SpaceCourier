/*
 * Copyright (c) 2020 Cpt-Ash (Ahmad Haidari)
 * All rights reserved.
 */

package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Marks the entity to be removed.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class RemoveComponent : Component, Pool.Poolable {
    var delay: Float = 0f

    override fun reset() {
        delay = 0f
    }

    companion object {
        val mapper = mapperFor<RemoveComponent>()
    }
}
