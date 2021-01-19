package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.ash.xkay.main.PreferenceKeys
import de.ash.xkay.main.Xkay
import de.ash.xkay.ui.MainMenuUI
import ktx.actors.onChangeEvent
import ktx.actors.plusAssign
import ktx.log.error
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
        // Escape/Back was pressed
        if (Gdx.app.type == Application.ApplicationType.Android && Gdx.input.isKeyJustPressed(Input.Keys.BACK)
            || Gdx.app.type == Application.ApplicationType.Desktop && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        game.engine.update(delta)
    }
}