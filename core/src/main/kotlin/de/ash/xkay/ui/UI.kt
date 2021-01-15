package de.ash.xkay.ui

import de.ash.xkay.assets.AtlasAsset
import de.ash.xkay.assets.BitmapFontAsset
import de.ash.xkay.assets.TextureAtlasAsset
import de.ash.xkay.assets.get
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.imageButton
import ktx.style.label
import ktx.style.skin
import ktx.style.textButton

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

enum class LabelStyles {
    DEFAULT,
}

enum class ButtonStyles {
    AUDIO,
    FRAMED_TEXT,
}

fun createSkin(assets: AssetStorage) {
    val atlas = assets[TextureAtlasAsset.UI]
    val largeFont = assets[BitmapFontAsset.LARGE_TEXT]

    Scene2DSkin.defaultSkin = skin(atlas) { skin ->
        label(LabelStyles.DEFAULT.name) {
            font = largeFont
        }

        imageButton(ButtonStyles.AUDIO.name) {
            imageUp = skin.getDrawable(AtlasAsset.AUDIO_ENABLED.regionName)
            imageDown = skin.getDrawable(AtlasAsset.AUDIO_DISABLED.regionName)
            imageChecked = imageDown
            up = skin.getDrawable(AtlasAsset.LABEL_FRAME.regionName)
            down = up
        }

        textButton(ButtonStyles.FRAMED_TEXT.name) {
            font = largeFont
            up = skin.getDrawable(AtlasAsset.LABEL_FRAME.regionName)
            down = up
        }
    }
}
