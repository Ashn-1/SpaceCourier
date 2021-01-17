package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class PlayerComponent : Component, Pool.Poolable {

    var highscore = 0.0f

    var isShieldActivated = false
    var shieldCooldown = 0.0f
    var shieldActiveTime = 0.0f

    override fun reset() {
        highscore = 0.0f
        isShieldActivated = false
        shieldCooldown = 0.0f
        shieldActiveTime = 0.0f
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}