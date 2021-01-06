package de.ash.xkay.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.ecs.createAsteroid
import ktx.assets.async.AssetStorage

/**
 * Spawns all the obstacles in the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class ObstacleSpawnSystem(
    private val assets: AssetStorage,
    private val gameViewport: Viewport,
    interval: Float = 2.0f
) : IntervalSystem(interval) {

    override fun updateInterval() {
        engine.createAsteroid(assets, gameViewport)
    }
}