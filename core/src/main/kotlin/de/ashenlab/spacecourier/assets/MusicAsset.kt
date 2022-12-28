/*
 * Copyright (c) 2020 Cpt-Ash (Ahmad Haidari)
 * All rights reserved.
 */

package de.ashenlab.spacecourier.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Music
import ktx.assets.async.AssetStorage

/**
 * Contains all the music assets of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
enum class MusicAsset(
    fileName: String,
    directory: String = "music",
    val descriptor: AssetDescriptor<Music> = AssetDescriptor("$directory/$fileName", Music::class.java)
) {

    WAVES_IN_FLIGHT("waves_in_flight.mp3"),
    MISSION_OVER("mission_over.mp3"),

}

operator fun AssetStorage.get(asset: MusicAsset) = get(asset.descriptor)