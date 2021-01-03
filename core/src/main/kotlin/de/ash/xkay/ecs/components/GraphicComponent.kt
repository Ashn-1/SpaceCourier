package de.ash.xkay.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Pool
import de.ash.xkay.Xkay
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

    fun setSprite(texture: Texture) {
        sprite.run {
            setRegion(texture)
            setSize(
                texture.width * Xkay.UNIT_SCALE,
                texture.height * Xkay.UNIT_SCALE
            )
            setOriginCenter()
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}