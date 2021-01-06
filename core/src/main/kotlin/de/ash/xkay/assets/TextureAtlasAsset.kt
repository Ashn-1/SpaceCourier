package de.ash.xkay.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.async.AssetStorage

/**
 * Contains all the texture atlas assets of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
enum class TextureAtlasAsset(
    fileName: String,
    directory: String,
    val isUiAtlas: Boolean = false,
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
) {

    GRAPHICS("graphics.atlas", "graphics"),
    UI("ui.atlas", "ui", isUiAtlas = true)

}

enum class AtlasAsset(
    val atlas: TextureAtlasAsset,
    val regionName: String
) {

    /*
    GRAPHICS
     */
    // Ship textures
    PLAYER_BASE_SHIP(TextureAtlasAsset.GRAPHICS, "ship"),

    // Obstacle textures
    ASTEROID_BASIC(TextureAtlasAsset.GRAPHICS, "asteroid_basic"),

    // Misc textures
    EXPLOSION(TextureAtlasAsset.GRAPHICS, "explosion"),


    /*
    UI
     */
}

operator fun AssetStorage.get(asset: TextureAtlasAsset) = get(asset.descriptor)

operator fun AssetStorage.get(asset: AtlasAsset): TextureRegion {
    return get(asset.atlas).findRegion(asset.regionName)
}
