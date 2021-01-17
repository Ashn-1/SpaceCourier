package de.ash.xkay.ui

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table
import de.ash.xkay.assets.AtlasAsset
import de.ash.xkay.main.PreferenceKeys
import ktx.actors.onChangeEvent
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class IngameUI {

    val table: Table
    val scoreLabel: Label

    init {
        table = scene2d.table {
            defaults().expandX().fillX().pad(2.5f).colspan(2)

            // Score stack
            stack {
                image(AtlasAsset.LABEL_FRAME.regionName)
                horizontalGroup {
                    scoreLabel = label("", LabelStyles.DEFAULT.name) {
                    }
                    align(Align.left)
                    padLeft(10f)
                }
            }
            row()

            setFillParent(true)
            top()
            pack()
        }
    }
}