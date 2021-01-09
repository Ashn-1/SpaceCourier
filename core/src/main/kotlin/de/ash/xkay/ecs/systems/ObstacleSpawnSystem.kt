package de.ash.xkay.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import de.ash.xkay.assets.AtlasAsset
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
    interval: Float = 1.0f
) : IntervalSystem(interval) {

    override fun updateInterval() {
        val p = MathUtils.random()
        when {
            p < 0.5f -> engine.createAsteroid(AtlasAsset.ASTEROID_SMALL, assets, gameViewport)
            p < 0.7f -> engine.createAsteroid(AtlasAsset.ASTEROID_MID_1, assets, gameViewport)
            p < 0.9f -> engine.createAsteroid(AtlasAsset.ASTEROID_MID_2, assets, gameViewport)
            p < 1.0f -> engine.createAsteroid(AtlasAsset.ASTEROID_BIG, assets, gameViewport)
        }
    }
}