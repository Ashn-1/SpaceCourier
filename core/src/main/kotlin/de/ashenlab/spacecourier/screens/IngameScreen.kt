package de.ashenlab.spacecourier.screens

import ashutils.ktx.ashLogger
import de.ashenlab.spacecourier.Main
import de.ashenlab.spacecourier.ecs.createPlayer
import de.ashenlab.spacecourier.ecs.createStar
import de.ashenlab.spacecourier.ecs.systems.SpawnSystem
import de.ashenlab.spacecourier.events.GameEvent
import de.ashenlab.spacecourier.events.GameEventListener
import de.ashenlab.spacecourier.main.PreferenceKeys
import de.ashenlab.spacecourier.ui.IngameUI
import de.ashenlab.spacecourier.assets.AtlasAsset
import de.ashenlab.spacecourier.assets.MusicAsset
import ktx.ashley.getSystem
import ktx.log.debug
import ktx.preferences.get
import ktx.actors.plusAssign
import kotlin.math.min

/**
 * Actual game screen with gameplay.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class IngameScreen(game: Main) : XkayScreen(game), GameEventListener {

    private val logger = ashLogger("Ingame")

    private val engine = game.engine

    private val ui = IngameUI()

    /**
     * The maximum delta time that will be used to update the game. This ensures that delta dependent logic will not jump due to lag (i.e. big delta values) or in case low framerates occur.
     */
    private val maxDeltaTime = 1 / 20f

    override fun show() {

        // Event listener registering
        eventManager.run {
            register(GameEvent.PlayerDeathEvent::class, this@IngameScreen)
            register(GameEvent.ScoreChangedEvent::class, this@IngameScreen)
        }

        // Activate spawning of obstacles
        engine.getSystem<SpawnSystem>().setProcessing(true)

        // Add everything that is needed for the game
        reset()

        ui.run {
            stage += table
        }

        logger.debug { "Ingame entered" }
    }

    override fun hide() {
        // Stop music
        audioService.stop()

        // Clean up the entity engine
        engine.removeAllEntities()
        engine.getSystem<SpawnSystem>().setProcessing(false)

        // Clear the UI
        stage.clear()

        // Unregister the event listener
        eventManager.unregister(this)
    }

    override fun render(delta: Float) {

        if (isBackButtonPressed()) {
            game.setScreen<MainMenuScreen>()
        }


        // Update the entity engine with a capped delta time
        engine.update(min(delta, maxDeltaTime))
    }

    private fun reset() {
        // Add all neccessary entities
        engine.removeAllEntities()
        engine.createPlayer(assets, gameViewport)
        repeat(25) {
            engine.createStar(AtlasAsset.STAR_WHITE, assets, gameViewport, onGameScreen = true)
        }

        // Start the game music
        audioService.play(MusicAsset.WAVES_IN_FLIGHT)

        logger.debug { "Game was reset" }
    }

    override fun onEvent(gameEvent: GameEvent) {
        when (gameEvent) {
            is GameEvent.PlayerDeathEvent -> {
                logger.debug { "Game over with score ${gameEvent.score}" }

                // Setup the game over screen
                game.getScreen<GameOverScreen>().run {
                    score = gameEvent.score
                    highscore = preferences[PreferenceKeys.HIGHSCORE.name, 0]
                }

                // Switch to the game over screen
                game.setScreen<GameOverScreen>()
            }
            is GameEvent.ScoreChangedEvent -> {
                // Change UI to show updated score
                ui.scoreLabel.setText("Score: ${gameEvent.score}")
            }
        }
    }

    override fun dispose() {
        super.dispose()
        logger.debug { "Ingame exited" }
    }
}