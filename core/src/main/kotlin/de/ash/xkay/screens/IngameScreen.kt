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

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class IngameScreen(game: Xkay) : XkayScreen(game) {

    private val logger = ashLogger("Ingame")

    val engine = game.engine
    val gameCamera = game.gameCamera

    val shapeRenderer = ShapeRenderer()

    override fun show() {
        logger.debug { "Ingame entered" }
    }

    val player: Entity = engine.entity {
        with<PlayerComponent>()
        with<TransformComponent>()
        with<VelocityComponent>()
        with<GraphicComponent>().apply {
            setSprite(assets[TextureAsset.PLAYER_BASE_SHIP])
        }
    }

    override fun resize(width: Int, height: Int) {
        game.gameViewport.update(width, height)
    }

    override fun render(delta: Float) {
        game.gameViewport.apply(true)

        engine.update(delta)
    }

    override fun dispose() {
        super.dispose()
        logger.debug { "Ingame exited" }
    }
}