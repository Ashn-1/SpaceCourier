package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.ash.xkay.assets.*
import de.ash.xkay.main.Xkay
import de.ash.xkay.ecs.createPlayer
import de.ash.xkay.ecs.createStar
import de.ash.xkay.ecs.systems.SpawnSystem
import de.ash.xkay.events.GameEvent
import de.ash.xkay.events.GameEventListener
import de.ash.xkay.main.PreferenceKeys
import de.ash.xkay.ui.IngameUI
import ktx.actors.onChangeEvent
import ktx.ashley.getSystem
import ktx.log.debug
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*
import ktx.actors.plusAssign
import ktx.log.error
import kotlin.math.min

/**
 * Actual game screen with gameplay.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class IngameScreen(game: Xkay) : XkayScreen(game), GameEventListener {

    private val logger = ashLogger("Ingame")

    /**
     * The maximum delta time that will be used to update the game. This ensures that delta dependent logic will not jump due to lag (i.e. big delta values) or in case low framerates occur.
     */
    private val maxDeltaTime = 1 / 20f

    /**
     * Indicates that the player died and the game should be reset
     */
    private var isGameOver = false

    private val engine = game.engine

    private val ui = IngameUI()

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

        when (Gdx.app.type) {
            Application.ApplicationType.Android -> {
                if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                    game.setScreen<MainMenuScreen>()
                }
            }
            Application.ApplicationType.Desktop -> {
                if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                    game.setScreen<MainMenuScreen>()
                }
            }
            else -> {
                logger.error { "OS not supported" }
            }
        }


        // Update the entity engine with a capped delta time
        engine.update(min(delta, maxDeltaTime))

        // Reset the game if it is over -> usually after returning from the game over screen
        if (isGameOver) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                reset()
            }
        }
    }

    private fun reset() {
        // Add all neccessary entities
        engine.removeAllEntities()
        engine.createPlayer(assets, gameViewport)
        repeat(25) {
            engine.createStar(AtlasAsset.STAR_WHITE, assets, gameViewport, onGameScreen = true)
        }

        // Reset the game over flag
        isGameOver = false

        // Start the game music
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