/*
 * Copyright (c) 2020 Cpt-Ash (Ahmad Haidari)
 * All rights reserved.
 */

package de.ash.xkay.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound
import ktx.assets.async.AssetStorage

/**
 * Contains all the sound assets of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
enum class SoundAsset(
    fileName: String,
    directory: String = "sounds",
    val descriptor: AssetDescriptor<Sound> = AssetDescriptor("$directory/$fileName", Sound::class.java)
) {

    EXPLOSION("explosion.wav"),

}

operator fun AssetStorage.get(asset: SoundAsset) = get(asset.descriptor)
