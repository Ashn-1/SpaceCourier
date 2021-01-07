package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.ash.xkay.main.Xkay
import de.ash.xkay.ecs.createPlayer
import de.ash.xkay.events.GameEvent
import de.ash.xkay.events.GameEventListener
import ktx.log.debug
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.actors
import ktx.scene2d.table
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

    override fun show() {
        eventManager.register(GameEvent.PlayerDeathEvent::class, this)
        reset()

        stage.actors {
            table {

            }
        }

        logger.debug { "Ingame entered" }
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        game.gameViewport.apply(true)
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
        isGameOver = false

        logger.debug { "Game was reset" }
    }

    override fun onEvent(gameEvent: GameEvent) {
        when (gameEvent) {
            is GameEvent.PlayerDeathEvent -> {
                logger.debug { "Game over with highscore ${gameEvent.highscore}" }
                isGameOver = true

                // Update highscore if highest score ever
                if (gameEvent.highscore > preferences["highscore", 0]) {
                    preferences.flush {
                        this["highscore"] = gameEvent.highscore
                    }
                }

            }
            is GameEvent.HighscoreChangedEvent -> {
                // Change UI to show updated highscore
            }
        }
    }

    override fun dispose() {
        super.dispose()
        logger.debug { "Ingame exited" }
    }
}