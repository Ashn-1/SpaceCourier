package de.ash.xkay.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.async.AssetStorage
import ktx.collections.GdxArray

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
    PLAYER_SHIP(TextureAtlasAsset.GRAPHICS, "ship"),
    PLAYER_SHIP_LEFT(TextureAtlasAsset.GRAPHICS, "ship_left"),
    PLAYER_SHIP_RIGHT(TextureAtlasAsset.GRAPHICS, "ship_right"),

    // Obstacle textures
    ASTEROID_SMALL(TextureAtlasAsset.GRAPHICS, "asteroid_small"),
    ASTEROID_MID_1(TextureAtlasAsset.GRAPHICS, "asteroid_mid_small"),
    ASTEROID_MID_2(TextureAtlasAsset.GRAPHICS, "asteroid_mid_big"),
    ASTEROID_BIG(TextureAtlasAsset.GRAPHICS, "asteroid_big"),

    // Background textures
    STAR_WHITE(TextureAtlasAsset.GRAPHICS, "star_white"),
    STAR_GREY(TextureAtlasAsset.GRAPHICS, "star_grey"),
    STAR_RED(TextureAtlasAsset.GRAPHICS, "star_red"),

    // Misc textures
    EXPLOSION(TextureAtlasAsset.GRAPHICS, "explosion"),

    /*
    UI
     */
    LOADING_BAR(TextureAtlasAsset.UI, "loading_bar"),
}

enum class AtlasAnimationAsset(
    val atlas: TextureAtlasAsset,
    val regionName: String
) {
    ENGINE_FIRE(TextureAtlasAsset.GRAPHICS, "engine_fire"),
}

operator fun AssetStorage.get(asset: TextureAtlasAsset) = get(asset.descriptor)

operator fun AssetStorage.get(asset: AtlasAsset): TextureRegion {
    return get(asset.atlas).findRegion(asset.regionName)
}

operator fun AssetStorage.get(asset: AtlasAnimationAsset) : GdxArray<out TextureRegion> {
    return get(asset.atlas).findRegions(asset.regionName)
}
