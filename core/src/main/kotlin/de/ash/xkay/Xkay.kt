package de.ash.xkay

import ashutils.ktx.ashLogger
import de.ash.xkay.ecs.systems.RemoveSystem
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.assets.TextureAsset
import de.ash.xkay.assets.get
import de.ash.xkay.ecs.systems.InputSystem
import de.ash.xkay.ecs.systems.MovementSystem
import de.ash.xkay.ecs.systems.RenderSystem
import de.ash.xkay.events.GameEventManager
import de.ash.xkay.screens.LoadingScreen
import de.ash.xkay.screens.XkayScreen
import ktx.app.KtxGame
import ktx.app.clearScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.graphics.center
import ktx.log.debug
import ktx.log.info

/**
 * Main class of the application. It handles all the screens, resources, etc. of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class Xkay : KtxGame<XkayScreen>(null, false) {
    
    private val logger = ashLogger("Main")

    val gameViewport by lazy { FitViewport(9f, 16f) }
    val gameCamera by lazy { gameViewport.camera as OrthographicCamera }

    val uiViewport by lazy { FitViewport(144f, 256f) }

    val eventManager = GameEventManager()

    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }

    val batch: SpriteBatch by lazy { SpriteBatch() }

    val engine: Engine by lazy {
        PooledEngine().apply {
            addSystem(InputSystem(gameViewport))
            addSystem(MovementSystem(gameViewport))
            addSystem(RenderSystem(
                batch,
                gameViewport,
                uiViewport,
                assets[TextureAsset.SPACE_BACKGROUND])
            )
            addSystem(RemoveSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = Logger.DEBUG
        logger.info { "Starting game" }

        // Create all game screens
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun render() {
        clearScreen(1f, 0f, 1f, 1f)
        super.render()
    }

    override fun dispose() {
        super.dispose()

        logger.debug { "Max sprites in a single batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        assets.dispose()

        logger.info { "Exiting game" }
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}
