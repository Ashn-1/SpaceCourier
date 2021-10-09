/*
 * Copyright (c) 2020 Cpt-Ash (Ahmad Haidari)
 * All rights reserved.
 */

package de.ashenlab.spacecourier.assets

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



}

operator fun AssetStorage.get(asset: TextureAsset) = get(asset.descriptor)
