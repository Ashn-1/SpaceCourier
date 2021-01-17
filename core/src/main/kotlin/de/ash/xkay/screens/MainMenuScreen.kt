package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import de.ash.xkay.main.PreferenceKeys
import de.ash.xkay.main.Xkay
import de.ash.xkay.ui.MainMenuUI
import ktx.actors.onChangeEvent
import ktx.actors.plusAssign
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class MainMenuScreen(game: Xkay) : XkayScreen(game)  {

    private val logger = ashLogger("MainMenu")

    private val ui = MainMenuUI().apply {

        startButton.onChangeEvent {
            game.setScreen<IngameScreen>()
        }

        highscoreLabel.setText("Score: ${preferences[PreferenceKeys.HIGHSCORE.name, 0]}")

        audioButton.onChangeEvent {
            audioService.enabled = !this.isChecked
            preferences.flush {
                this[PreferenceKeys.IS_AUDIO_ENABLED.name] = audioService.enabled
            }
        }
    }

    override fun show() {
        stage += ui.table
    }

    override fun hide() {
        game.engine.removeAllEntities()
        stage.clear()
    }

    override fun render(delta: Float) {
        game.engine.update(delta)
    }
}