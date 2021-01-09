package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import de.ash.xkay.main.UNIT_SCALE
import de.ash.xkay.main.Xkay
import ktx.ashley.mapperFor

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class GraphicComponent : Component, Pool.Poolable {

    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
        sprite.setPosition(0f, 0f)
    }

    fun setSprite(region: TextureRegion) {
        sprite.run {
            setRegion(region)
            setSize(
                region.regionWidth * UNIT_SCALE,
                region.regionHeight * UNIT_SCALE
            )
            setOriginCenter()
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}