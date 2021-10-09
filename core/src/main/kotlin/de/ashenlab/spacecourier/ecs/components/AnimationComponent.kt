package de.ashenlab.spacecourier.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.collections.GdxArray

/**
 * Handles the given [TextureRegion]'s as an animation.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class AnimationComponent :
    Component,
    Pool.Poolable {

    enum class PlayMode {
        NORMAL,     // Play animation once and stay at last frame
        LOOP        // Restart animation oce the end has been reached
    }

    /**
     * Contains the [TextureRegion]'s that are the frames of the animation.
     */
    lateinit var frames: GdxArray<out TextureRegion>

    /**
     * Time in seconds that every frame will last.
     */
    var frameDuration: Float = 0f

    /**
     * Determines how the animation frames are looped.
     */
    var playMode = PlayMode.NORMAL

    private var currentFrame: Int = 0
    private var time: Float = 0f

    /**
     * Updates the animation with the given [delta] time. If the internal time exceeds the [frameDuration] the next frame is selected. If the frame changes because of the update true is returned, else false.
     */
    fun update(delta: Float) : Boolean {
        val previousFrame = currentFrame
        time += delta
        while (time > frameDuration) {
            time -= frameDuration
            currentFrame++
            if (currentFrame == frames.size) { // Last frame was reached
                when (playMode) {
                    PlayMode.NORMAL -> {
                        // Set to last frame
                        currentFrame = frames.size - 1
                    }
                    PlayMode.LOOP -> {
                        currentFrame = 0
                    }
                }
            }
        }

        return currentFrame != previousFrame
    }

    /**
     * Returns the [TextureRegion] that is currently the active frame.
     */
    fun getCurrentFrame() = frames[currentFrame]

    override fun reset() {
        frameDuration = 0f
        currentFrame = 0
        time = 0f
        playMode = PlayMode.NORMAL
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}