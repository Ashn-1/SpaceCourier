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
class GameOverUI {

    val table: Table
    val restartButton: Button
    val scoreLabel: Label
    val highscoreLabel: Label

    init {
        table = scene2d.table {
            defaults().fillX().expandX().pad(0.5f, 5f, 0.5f, 5f)

            highscoreLabel = label("New Highscore!", LabelStyles.DEFAULT.name) {
                setAlignment(Align.center)
                wrap = true
            }
            row()

            scoreLabel = label("", LabelStyles.DEFAULT.name) {
                wrap = true
                setAlignment(Align.center)
            }
            row()

            // Restart message
            restartButton = textButton("Restart", ButtonStyles.FRAMED_TEXT.name) {
                label.wrap = true
                label.setAlignment(Align.center)
            }

            setFillParent(true)
            pack()
        }
    }
}