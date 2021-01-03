/*
 * Copyright (c) 2020 Cpt-Ash (Ahmad Haidari)
 * All rights reserved.
 */

package de.ash.xkay.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import ktx.assets.async.AssetStorage

/**
 * Contains all the texture assets of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
enum class TextureAsset(
    fileName: String,
    directory: String,
    val descriptor: AssetDescriptor<Texture> = AssetDescriptor("$directory/$fileName", Texture::class.java)
) {

    PLAYER_BASE_SHIP("ship.png", "textures"),
    SPACE_BACKGROUND("background.png", "textures")

}

operator fun AssetStorage.get(asset: TextureAsset) = get(asset.descriptor)

