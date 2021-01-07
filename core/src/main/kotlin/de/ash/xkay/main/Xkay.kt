package de.ash.xkay.main

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.viewport.FitViewport
import de.ash.xkay.assets.BitmapFontAsset
import de.ash.xkay.assets.TextureAsset
import de.ash.xkay.assets.TextureAtlasAsset
import de.ash.xkay.assets.get
import de.ash.xkay.audio.AudioService
import de.ash.xkay.audio.DefaultAudioService
import de.ash.xkay.ecs.systems.*
import de.ash.xkay.events.GameEventManager
import de.ash.xkay.screens.LoadingScreen
import de.ash.xkay.screens.XkayScreen
import de.ash.xkay.ui.createSkin
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.clearScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.info

/**
 * Main class of the application. It handles all the screens, resources, etc. of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

const val DEBUG = false

class Xkay : KtxGame<XkayScreen>(null, false) {
    
    private val logger = ashLogger("Main")

    // Viewports used in the game
    val uiViewport by lazy { FitViewport(144f, 256f) }
    val gameViewport by lazy { FitViewport(9f, 16f) }
    val gameCamera by lazy { gameViewport.camera as OrthographicCamera }

    // OpenGL renderers for sprites and shapes
    val batch: SpriteBatch by lazy { SpriteBatch() }
    val shapeRenderer: ShapeRenderer by lazy { ShapeRenderer() }

    // UI
    val stage: Stage by lazy {
        Stage(uiViewport).apply {
            Gdx.input.inputProcessor = this
            isDebugAll = DEBUG
        }
    }

    val eventManager = GameEventManager()

    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }

    val audioService: AudioService by lazy { DefaultAudioService(assets) }

    val preferences: Preferences by lazy { Gdx.app.getPreferences("xkay.data") }

    val engine: Engine by lazy {
        PooledEngine().apply {
            addSystem(InputSystem(gameViewport))
            addSystem(ObstacleSpawnSystem(assets, gameViewport))
            addSystem(MovementSystem(eventManager, gameViewport))
            addSystem(CollisionSystem(assets))
            addSystem(RenderSystem(
                    batch,
                    shapeRenderer,
                    gameViewport,
                    uiViewport,
                    assets[TextureAsset.SPACE_BACKGROUND],
                    isDebug = DEBUG
                )
            )
            addSystem(RemoveSystem(eventManager))
        }
    }

    override fun create() {
        Gdx.app.logLevel = Logger.DEBUG
        logger.info { "Starting game" }

        // Load skin and font to display loading screen
        val assetRefs = gdxArrayOf(
            TextureAtlasAsset.values().filter { it.isUiAtlas }.map { assets.loadAsync(it.descriptor) },
            BitmapFontAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            createSkin(assets)

            // Start loading screen
            addScreen(LoadingScreen(this@Xkay))
            setScreen<LoadingScreen>()
        }
    }

    override fun render() {
        clearScreen(1f, 0f, 1f, 1f)
        super.render()
    }

    override fun dispose() {
        super.dispose()

        logger.debug { "Max sprites in a single batch: ${batch.maxSpritesInBatch}" }
        batch.dispose()
        shapeRenderer.dispose()
        assets.dispose()
        stage.dispose()

        logger.info { "Exiting game" }
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}
