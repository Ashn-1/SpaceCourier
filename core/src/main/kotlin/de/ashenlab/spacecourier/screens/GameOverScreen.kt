package de.ashenlab.spacecourier.screens

import ashutils.ktx.ashLogger
import de.ashenlab.spacecourier.assets.MusicAsset
import de.ashenlab.spacecourier.main.PreferenceKeys
import de.ashenlab.spacecourier.main.Xkay
import de.ashenlab.spacecourier.ui.GameOverUI
import ktx.actors.onChangeEvent
import ktx.actors.plusAssign
import ktx.log.info
import ktx.preferences.flush
import ktx.preferences.set

/**
 * Screen that is shown after the player died in the game. It shows the final score and whether or not the highscore was broken. The player can also restart from here.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class GameOverScreen(game: Xkay) : XkayScreen(game) {

    private val logger = ashLogger("GameOverScreen")

    private val engine = game.engine

    private val ui = GameOverUI().apply {
        restartButton.onChangeEvent {
            restart()
        }
    }

    /**
     * Score of the currently ended game
     */
    var score: Int = 0

    /**
     * Highest ever score achieved
     */
    var highscore: Int = 0

    override fun show() {
        // Update highscore if neccessary
        val isNewHighscore = score > highscore
        if (isNewHighscore) {
            logger.info { "New highscore -> old: $highscore, new: $score" }

            // Save the new highscore
            preferences.flush {
                this[PreferenceKeys.HIGHSCORE.name] = score
            }
        }

        // Setup UI
        ui.run {
            scoreLabel.setText("Score: $score")
            if (isNewHighscore) highscoreLabel.setText("New Highscore!")
            else highscoreLabel.setText("Current Highscore: $highscore")
            stage += this.table
        }

        // Play game over music
        audioService.play(MusicAsset.MISSION_OVER, loop = false)
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        if (isBackButtonPressed()) {
            game.setScreen<MainMenuScreen>()
        }

        engine.update(delta)
    }

    private fun restart() {
        game.setScreen<IngameScreen>()
    }
}