package de.ashenlab.spacecourier.ecs.systems

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import de.ashenlab.spacecourier.main.XkayRuntimeException
import de.ashenlab.spacecourier.ecs.components.GraphicComponent
import de.ashenlab.spacecourier.ecs.components.HitboxComponent
import de.ashenlab.spacecourier.ecs.components.TransformComponent
import de.ashenlab.spacecourier.extensions.circle
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

/**
 * Handles the rendering of the UI and all entities. Entities are only rendered if they have a [GraphicComponent] and a [TransformComponent].
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class RenderSystem(
    private val batch: SpriteBatch,
    private val shapeRenderer: ShapeRenderer,
    private val stage: Stage,
    private val gameViewport: Viewport,
    var isDebug: Boolean = false
) : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {

    private val logger = ashLogger("RenderSys")

    override fun update(deltaTime: Float) {

        // On super.update call the entities are depth sorted
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

        // Render the stage over the entities
        stage.run {
            viewport.apply()
            act(deltaTime)
            draw()
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
            draw(batch)
        }
    }
}