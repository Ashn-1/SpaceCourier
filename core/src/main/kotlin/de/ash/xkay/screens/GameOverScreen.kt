package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import de.ash.xkay.assets.AtlasAsset
import de.ash.xkay.main.Xkay
import de.ash.xkay.ui.LabelStyles
import ktx.actors.plus
import ktx.scene2d.*

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

    private var restartTimer: Long = 0L

    private lateinit var restartLabel: Label

    override fun show() {
        stage.actors {
            table {
                defaults().fillX().expandX()

                // New highscore label
                if (highscore < score) {
                    label("New Highscore!", LabelStyles.DEFAULT.name) {
                        setAlignment(Align.center)
                    }
                    row()
                }

                // Score
                stack { cell ->
                    image(AtlasAsset.LOADING_BAR.regionName)
                    label("Score: $score", LabelStyles.DEFAULT.name) {
                        setAlignment(Align.center)
                    }
                    cell.padLeft(5f).padRight(5f)
                }
                row()

                // Restart message
                restartLabel = label("Touch to Restart", LabelStyles.DEFAULT.name) {
                    wrap = true
                    setAlignment(Align.center)
                    isVisible = false
                    color.a = 0f
                    addAction(Actions.forever(Actions.sequence(
                        Actions.fadeIn(0.5f) + Actions.fadeOut(0.5f)
                    )))
                }

                setFillParent(true)
                pack()
            }
        }

        restartTimer = TimeUtils.millis()
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        engine.update(delta)

        // Enable restarting after 2 seconds
        if (TimeUtils.timeSinceMillis(restartTimer) > 2000L) {
            restartLabel.isVisible = true
            if (Gdx.input.justTouched()) {
                game.setScreen<IngameScreen>()
            }
        }
    }
}