package de.ashenlab.spacecourier.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.assets.async.AssetStorage

/**
 * Contains all the bitmap fonts of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
enum class BitmapFontAsset(
    fileName: String,
    directory: String = "fonts",
    val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor(
        "$directory/$fileName",
        BitmapFont::class.java,
        BitmapFontLoader.BitmapFontParameter().apply {
            atlasName = TextureAtlasAsset.UI.descriptor.fileName
        }
    )
) {

    LARGE_TEXT("montserrat_alternate.fnt")

}

operator fun AssetStorage.get(asset: BitmapFontAsset) = get(asset.descriptor)

