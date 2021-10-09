package de.ashenlab.spacecourier.ui

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.scene2d.*

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class MainMenuUI {

    val table: Table
    val title: Label
    val startButton: Button
    val highscoreLabel: Label
    val settingsButton: Button
    val audioButton: Button

    init {
        table = scene2d.table {
            defaults().expandX().fillX().pad(0.5f, 5f, 0.5f, 5f).colspan(2)

            title = label("eXpress Courier", LabelStyles.DEFAULT.name) {
                setAlignment(Align.center)
                wrap = true
            }
            row()

            startButton = textButton("Start Game", ButtonStyles.FRAMED_TEXT.name) {
                label.setAlignment(Align.center)
            }
            row()

            highscoreLabel = label("Score: x", LabelStyles.DEFAULT.name) {
                setAlignment(Align.center)
                wrap = true
            }
            row()

            settingsButton = textButton("Settings", ButtonStyles.FRAMED_TEXT.name) {
                label.setAlignment(Align.center)
                label.wrap = true
                cell(colspan = 1)
            }
            audioButton = imageButton(ButtonStyles.AUDIO.name).cell(colspan = 1, expandX = false)
            row()

            setFillParent(true)
            pack()
        }
    }
}
