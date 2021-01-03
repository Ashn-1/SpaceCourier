package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.Xkay
import de.ash.xkay.XkayRuntimeException
import de.ash.xkay.ecs.components.GraphicComponent
import de.ash.xkay.ecs.components.RemoveComponent
import de.ash.xkay.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.debug

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class RenderSystem(
    private val batch: SpriteBatch,
    private val gameViewport: Viewport,
    backgroundTexture: Texture
) : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).exclude(RemoveComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {

    private val logger = ashLogger("RenderSys")

    private val backgroundScrollSpeed = 2f

    private val backgroundSprite = Sprite(backgroundTexture).apply {
        setSize(texture.width * Xkay.UNIT_SCALE, texture.height * Xkay.UNIT_SCALE)
        y = -MathUtils.random(height)
    }

    override fun update(deltaTime: Float) {

        // Force sorting on next updates
        forceSort()

        // Set gameview (and update camera)
        gameViewport.apply(true)

        batch.use(gameViewport.camera) {

            // Scroll the background and render it
            backgroundSprite.run {
                // FIXME transition from end of background to beginning is not correct
                y -= backgroundScrollSpeed * deltaTime
                if (y < -backgroundSprite.height + gameViewport.worldHeight) y = 0f
                draw(batch)
            }

            // Render entities
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // Get the necessary components
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity $entity does not have a TransformComponent" }
        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) { "Entity $entity does not have a GraphicComponent" }

        // Check if the sprite was given a texture
        if (graphic.sprite.texture == null) {
            throw XkayRuntimeException("Entity $entity's GraphicComponent does not have a texture")
        }

        // Update the sprite transform
        graphic.sprite.run {
            setCenter(transform.position.x, transform.position.y)
            draw(batch)
        }
    }
}