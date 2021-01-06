package de.ash.xkay.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.Xkay
import de.ash.xkay.XkayRuntimeException
import de.ash.xkay.ecs.components.GraphicComponent
import de.ash.xkay.ecs.components.HitboxComponent
import de.ash.xkay.ecs.components.RemoveComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.extensions.circle
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
    private val shapeRenderer: ShapeRenderer,
    private val gameViewport: Viewport,
    private val uiViewport: Viewport,
    backgroundTexture: Texture,
    var isDebug: Boolean = false
) : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {

    private val logger = ashLogger("RenderSys")

    private val backgroundScrollSpeed = 0.01f

    private val backgroundSprite = Sprite(backgroundTexture.apply {
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    }).apply {
        //setSize(texture.width * Xkay.UNIT_SCALE, texture.height * Xkay.UNIT_SCALE)
        //y = -MathUtils.random(height)
    }

    override fun update(deltaTime: Float) {

        // Use UI view for larger things like UI and background
        uiViewport.apply()
        batch.use(uiViewport.camera) {
            // Scroll the background and render it
            backgroundSprite.run {
                scroll(0f, -backgroundScrollSpeed * deltaTime)
                draw(batch)
            }
        }

        // Force sorting on next updates
        forceSort()

        // Use game viewport for small entities
        gameViewport.apply()
        batch.use(gameViewport.camera) {
            // Render entities
            super.update(deltaTime)
        }

        if (isDebug) {
            shapeRenderer.color = Color.MAGENTA
            shapeRenderer.use(ShapeRenderer.ShapeType.Line, gameViewport.camera) { shaper ->
                for (entity in entities) {
                    entity[HitboxComponent.mapper]?.let { hitbox ->
                        shaper.circle(hitbox.hitbox)
                    }
                }
            }
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
            setCenter(transform.interpolatedPosition.x, transform.interpolatedPosition.y)
            setSize(transform.size.x, transform.size.y)
            rotation = transform.rotationDeg
            draw(batch)
        }
    }
}