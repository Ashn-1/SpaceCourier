/*
 * Copyright (c) 2020 Cpt-Ash (Ahmad Haidari)
 * All rights reserved.
 */

package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import de.ash.xkay.assets.TextureAsset
import de.ash.xkay.Xkay
import de.ash.xkay.assets.MusicAsset
import de.ash.xkay.assets.SoundAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug

/**
 * Initial state of the game. Here all the assets for the game are loaded and all states are created.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class LoadingScreen(game: Xkay) : XkayScreen(game) {

    private val logger = ashLogger("LoadingState")

    override fun show() {
        logger.debug { "Start loading assets..." }
        val loadingStartTime = System.currentTimeMillis()

        // Set loaders for specific file types
        assets.setLoader(suffix = ".tmx") {
            TmxMapLoader(assets.fileResolver)
        }

        // Queue all the assets to be loaded
        val assetRefs = gdxArrayOf(
            TextureAsset.values().map { assets.loadAsync(it.descriptor) },
            MusicAsset.values().map { assets.loadAsync(it.descriptor) },
            SoundAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()

        // Launch coroutine to load all the assets
        KtxAsync.launch {
            assetRefs.joinAll()
            logger.debug { "Done loading in ${System.currentTimeMillis() - loadingStartTime} ms" }
            assetsLoaded()
        }

        // Continue to setup UI, etc. while the assets are loaded
    }

    /**
     * All assets are loaded --> switch to the next screen. This screen is no longer needed and can be removed.
     */
    private fun assetsLoaded() {
        game.addScreen(IngameScreen(game))
        game.setScreen<IngameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose()
    }
}
