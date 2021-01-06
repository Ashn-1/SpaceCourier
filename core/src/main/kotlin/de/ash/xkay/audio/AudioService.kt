package de.ash.xkay.audio

import de.ash.xkay.assets.MusicAsset
import de.ash.xkay.assets.SoundAsset

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