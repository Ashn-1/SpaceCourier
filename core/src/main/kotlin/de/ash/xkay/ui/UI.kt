package de.ash.xkay.ui

import de.ash.xkay.assets.BitmapFontAsset
import de.ash.xkay.assets.TextureAtlasAsset
import de.ash.xkay.assets.get
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

enum class LabelStyles {
    DEFAULT
}

fun createSkin(assets: AssetStorage) {
    val atlas = assets[TextureAtlasAsset.UI]
    val largeFont = assets[BitmapFontAsset.LARGE_TEXT]

    Scene2DSkin.defaultSkin = skin(atlas) { skin ->
        label(LabelStyles.DEFAULT.name) {
            font = largeFont
        }
    }
}
