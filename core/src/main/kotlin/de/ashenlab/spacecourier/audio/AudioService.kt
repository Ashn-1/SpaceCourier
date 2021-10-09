package de.ashenlab.spacecourier.audio

import de.ashenlab.spacecourier.assets.MusicAsset
import de.ashenlab.spacecourier.assets.SoundAsset

/**
 * TODO add docs
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
interface AudioService {

    var enabled: Boolean

    fun play(soundAsset: SoundAsset, volume: Float = 1f)
    fun play(musicAsset: MusicAsset, volume: Float = 1f, loop: Boolean = true)
    fun pause()
    fun resume()
    fun stop(clearSounds: Boolean = true)
    fun update()
}