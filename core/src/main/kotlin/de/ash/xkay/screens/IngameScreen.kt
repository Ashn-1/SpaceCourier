package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.sun.corba.se.impl.orbutil.graph.Graph
import de.ash.xkay.assets.*
import de.ash.xkay.ecs.components.AnimationComponent
import de.ash.xkay.ecs.components.GraphicComponent
import de.ash.xkay.ecs.components.TransformComponent
import de.ash.xkay.main.Xkay
import de.ash.xkay.ecs.createPlayer
import de.ash.xkay.ecs.createStar
import de.ash.xkay.ecs.systems.SpawnSystem
import de.ash.xkay.events.GameEvent
import de.ash.xkay.events.GameEventListener
import de.ash.xkay.ui.LabelStyles
import ktx.ashley.entity
import ktx.ashley.getSystem
import ktx.ashley.with
import ktx.log.debug
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*
import kotlin.math.min

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class IngameScreen(game: Xkay) : XkayScreen(game), GameEventListener {

    private val logger = ashLogger("Ingame")

    /**
     * The maximum delta time that will be used to update the game. This ensures that delta dependent logic will not jump due to lag (i.e. big delta values) or in case low framerates occur.
     */
    private val maxDeltaTime = 1 / 20f

    private val engine = game.engine

    private var isGameOver = false

    // UI variables
    private lateinit var scoreLabel: Label

    override fun show() {
        eventManager.run {
            register(GameEvent.PlayerDeathEvent::class, this@IngameScreen)
            register(GameEvent.HighscoreChangedEvent::class, this@IngameScreen)
        }

        engine.getSystem<SpawnSystem>().setProcessing(true)

        reset()

        stage.actors {
            table {
                defaults().fillX().expandX()

                align(Align.topLeft)

                stack {
                    image(AtlasAsset.LOADING_BAR.regionName)

                    horizontalGroup {
                        label("Score: ", LabelStyles.DEFAULT.name) {
                            setAlignment(Align.left)
                        }
                        scoreLabel = label("", LabelStyles.DEFAULT.name) {
                            setAlignment(Align.left)
                        }
                    }
                }
                row()

                setFillParent(true)
                pack()
            }
        }

        logger.debug { "Ingame entered" }
    }

    override fun hide() {
        audioService.stop()

        engine.removeAllEntities()
        engine.getSystem<SpawnSystem>().setProcessing(false)

        stage.clear()

        eventManager.unregister(this)
    }

    override fun render(delta: Float) {
        engine.update(min(delta, maxDeltaTime))

        if (isGameOver) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                reset()
            }
        }
    }

    private fun reset() {
        engine.removeAllEntities()
        engine.createPlayer(assets, gameViewport)
        repeat(25) {
            engine.createStar(AtlasAsset.STAR_WHITE, assets, gameViewport, onGameScreen = true)
        }

        isGameOver = false

        audioService.play(MusicAsset.WAVES_IN_FLIGHT)

        logger.debug { "Game was reset" }
    }

    override fun onEvent(gameEvent: GameEvent) {
        when (gameEvent) {
            is GameEvent.PlayerDeathEvent -> {
                logger.debug { "Game over with score ${gameEvent.score}" }
                isGameOver = true

                // Setup the game over screen
                game.getScreen<GameOverScreen>().run {
                    score = gameEvent.score
                    highscore = preferences["highscore", 0]
                }

                // Update highscore if highest score ever
                if (gameEvent.score > preferences["highscore", 0]) {
                    logger.debug { "New highscore -> old: ${preferences["highscore", 0]}, new: ${gameEvent.score}" }

                    // Save the new highscore
                    preferences.flush {
                        this["highscore"] = gameEvent.score
                    }
                }

                // Switch to the game over screen
                game.setScreen<GameOverScreen>()
            }
            is GameEvent.HighscoreChangedEvent -> {
                // Change UI to show updated highscore
                scoreLabel.setText(gameEvent.newHighscore)
            }
        }
    }

    override fun dispose() {
        super.dispose()
        logger.debug { "Ingame exited" }
    }
}