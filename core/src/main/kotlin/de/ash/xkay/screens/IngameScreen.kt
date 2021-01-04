package de.ash.xkay.screens

import ashutils.ktx.AshKtxLogger
import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import de.ash.xkay.Xkay
import de.ash.xkay.assets.TextureAsset
import de.ash.xkay.assets.get
import de.ash.xkay.ecs.components.GraphicComponent
import de.ash.xkay.ecs.components.PlayerComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.ecs.components.VelocityComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import kotlin.math.min

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class IngameScreen(game: Xkay) : XkayScreen(game) {

    private val logger = ashLogger("Ingame")

    /**
     * The maximum delta time that will be used to update the game. This ensures that delta dependent logic will not jump due to lag (i.e. big delta values) or in case low framerates occur.
     */
    private val maxDeltaTime = 1 / 20f

    private val engine = game.engine

    override fun show() {
        logger.debug { "Ingame entered" }
    }

    val player: Entity = engine.entity {
        with<PlayerComponent>()
        with<TransformComponent>().apply {
            setInitialPosition(gameViewport.worldWidth * 0.5f, gameViewport.worldHeight * 0.25f)
        }
        with<VelocityComponent>()
        with<GraphicComponent>().apply {
            setSprite(assets[TextureAsset.PLAYER_BASE_SHIP])
        }
    }

    override fun render(delta: Float) {
        game.gameViewport.apply(true)
        engine.update(min(delta, maxDeltaTime))
    }

    override fun dispose() {
        super.dispose()
        logger.debug { "Ingame exited" }
    }
}