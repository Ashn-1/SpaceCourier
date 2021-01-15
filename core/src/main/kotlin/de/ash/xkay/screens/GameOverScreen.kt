package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import de.ash.xkay.assets.MusicAsset
import de.ash.xkay.main.PreferenceKeys
import de.ash.xkay.main.Xkay
import de.ash.xkay.ui.GameOverUI
import ktx.actors.onChangeEvent
import ktx.actors.plusAssign
import ktx.log.debug
import ktx.preferences.flush
import ktx.preferences.set

/**
 * TODO add docs
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class GameOverScreen(game: Xkay) : XkayScreen(game) {

    private val logger = ashLogger("GameOverScreen")

    private val engine = game.engine

    var score: Int = 0
    var highscore: Int = 0

    private val ui = GameOverUI().apply {
        restartButton.onChangeEvent {
            restart()
        }
    }

    override fun show() {
        // Update highscore if neccessary
        val isNewHighscore = score > highscore
        if (isNewHighscore) {
            logger.debug { "New highscore -> old: $highscore, new: $score" }

            // Save the new highscore
            preferences.flush {
                this[PreferenceKeys.HIGHSCORE.name] = score
            }
        }

        // Setup UI
        ui.run {
            scoreLabel.setText("Score: $score")
            if (isNewHighscore) newHighscoreLabel.isVisible = true
            stage += this.table
        }

        // Play game over music
        audioService.play(MusicAsset.MISSION_OVER, loop = false)
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    private fun restart() {
        game.setScreen<IngameScreen>()
    }
}