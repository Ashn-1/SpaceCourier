package de.ash.xkay.main

import ashutils.ktx.ashLogger
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.viewport.FitViewport
import de.ash.xkay.audio.AudioService
import de.ash.xkay.audio.DefaultAudioService
import de.ash.xkay.ecs.systems.*
import de.ash.xkay.events.GameEventManager
import de.ash.xkay.screens.LoadingScreen
import de.ash.xkay.screens.XkayScreen
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.info


const val DEBUG = true
const val UNIT_SCALE = 1 / 8f
const val GAME_SCREEN_WIDTH = 9f
const val GAME_SCREEN_HEIGHT = 16f
const val UI_SCREEN_WIDTH = 144f
const val UI_SCREEN_HEIGHT = 256f

/**
 * Main class of the application. It handles all the screens, resources, etc. of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class Xkay : KtxGame<XkayScreen>() {
    
    private val logger = ashLogger("Main")

    // Viewports used in the game
    val uiViewport by lazy { FitViewport(UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT) }
    val gameViewport by lazy { FitViewport(GAME_SCREEN_WIDTH, GAME_SCREEN_HEIGHT) }

    // OpenGL renderers for sprites and shapes
    val batch: SpriteBatch by lazy { SpriteBatch() }
    val shapeRenderer: ShapeRenderer by lazy { ShapeRenderer() }

    // Contains all the input processors
    val inputs = InputMultiplexer()

    // UI
    val stage: Stage by lazy {
        Stage(uiViewport).apply {
            inputs.addProcessor(0, this)
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
            val playerInput = PlayerInputSystem(assets, gameViewport).apply {
                inputs.addProcessor(1, playerGestureDetection)
                inputs.addProcessor(2, playerInput)
            }
            addSystem(playerInput)
            addSystem(PlayerAnimationSystem(assets))
            addSystem(ShieldSystem())
            addSystem(SpawnSystem(assets, gameViewport).apply { setProcessing(false) })
            addSystem(MovementSystem(eventManager, gameViewport))
            addSystem(AttachSystem())
            addSystem(CollisionSystem(assets, audioService))
            addSystem(AnimationSystem())
            addSystem(RenderSystem(
                    batch,
                    shapeRenderer,
                    stage,
                    gameViewport,
                    isDebug = DEBUG
                )
            )
            addSystem(RemoveSystem(eventManager))
        }
    }

    override fun create() {
        Gdx.app.logLevel = Logger.DEBUG
        logger.info { "Starting game" }

        // Catch back button
        Gdx.input.setCatchKey(Input.Keys.BACK, true)

        Gdx.input.inputProcessor = inputs

        // Start loading screen
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun render() {
        super.render()
        audioService.update()
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
}
