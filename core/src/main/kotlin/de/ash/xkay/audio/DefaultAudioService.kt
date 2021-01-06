package de.ash.xkay.audio

import ashutils.ktx.ashLogger
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.Pool
import de.ash.xkay.assets.MusicAsset
import de.ash.xkay.assets.SoundAsset
import de.ash.xkay.assets.get
import ktx.assets.async.AssetStorage
import ktx.log.debug
import ktx.log.error
import ktx.log.info
import java.util.*
import kotlin.math.max

/**
 * TODO add docs
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class DefaultAudioService(
    private val assets: AssetStorage
) : AudioService {

    private val logger = ashLogger("AudioService")

    override var enabled = true
        set (value) {
            when (value) {
                true -> currentMusic?.play()
                false -> currentMusic?.play()
            }
            field = value
        }

    private val soundRequestPool = SoundRequestPool()
    private val soundRequests = EnumMap<SoundAsset, SoundRequest>(SoundAsset::class.java)
    private var currentMusic: Music? = null

    override fun play(soundAsset: SoundAsset, volume: Float) {
        if (!enabled) return

        when {
            soundAsset in soundRequests -> {
                // Sound was already requested and will be played once
                // with highest volume of both
                logger.debug { "Duplicated sound request for sound $soundAsset" }
                soundRequests[soundAsset]?.let { request ->
                    request.volume = max(request.volume, volume)
                }
            }

            soundRequests.size >= MAX_SOUND_INSTANCES -> {
                logger.info { "Maximum number of sound requests reached" }
            }

            else -> {
                // Actual new request
                if (soundAsset.descriptor !in assets) {
                    logger.error { "Sound $soundAsset is not loaded" }
                    return
                }

                logger.debug { "New sound request for sound $soundAsset. Free request objects: ${soundRequestPool.free}" }
                soundRequests[soundAsset] = soundRequestPool.obtain().apply {
                    this.soundAsset = soundAsset
                    this.volume = volume
                }
            }
        }
    }

    override fun play(musicAsset: MusicAsset, volume: Float, loop: Boolean) {

        if (currentMusic != null) {
            currentMusic?.stop()
        }

        if (musicAsset.descriptor !in assets) {
            logger.error { "Music $musicAsset is not loaded" }
            return
        }

        currentMusic = assets[musicAsset]
        if (!enabled) return

        currentMusic?.let {
            it.volume = volume
            it.isLooping = loop
            it.play()
        }
    }

    override fun pause() {
        currentMusic?.pause()
    }

    override fun resume() {
        if (!enabled) return

        currentMusic?.play()
    }

    override fun stop(clearSounds: Boolean) {
        currentMusic?.stop()
        if (clearSounds) {
            soundRequests.clear()
        }
    }

    override fun update() {
        if (!soundRequests.isEmpty()) {
            // Sounds are queued to be played
            logger.debug { "Playing ${soundRequests.size} sound(s)" }
            soundRequests.values.forEach { request ->
                assets[request.soundAsset].play(request.volume)
                soundRequestPool.free(request)
            }
            soundRequests.clear()
        }
    }

    companion object {
        private const val MAX_SOUND_INSTANCES = 16
    }
}

private class SoundRequest : Pool.Poolable {
    lateinit var soundAsset: SoundAsset
    var volume = 1f

    override fun reset() {
        volume = 1f
    }
}

private class SoundRequestPool : Pool<SoundRequest>(){
    override fun newObject() = SoundRequest()
}
