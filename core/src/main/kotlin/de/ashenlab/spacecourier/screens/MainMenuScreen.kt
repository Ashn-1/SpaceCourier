package de.ashenlab.spacecourier.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.Gdx
import de.ashenlab.spacecourier.main.PreferenceKeys
import de.ashenlab.spacecourier.main.Xkay
import de.ashenlab.spacecourier.ui.MainMenuUI
import ktx.actors.onChangeEvent
import ktx.actors.plusAssign
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set

/**
 * Main menu of the game.
 *
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
        ui.run {
            audioButton.isChecked = !audioService.enabled

            stage += table
        }
    }

    override fun hide() {
        game.engine.removeAllEntities()
        stage.clear()
    }

    override fun render(delta: Float) {
        if (isBackButtonPressed()) {
            Gdx.app.exit()
        }

        game.engine.update(delta)
    }
}