/*
 * Copyright (c) 2020 Cpt-Ash (Ahmad Haidari)
 * All rights reserved.
 */

package de.ash.xkay.screens

import ashutils.ktx.ashLogger
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import de.ash.xkay.assets.TextureAsset
import de.ash.xkay.main.Xkay
import de.ash.xkay.assets.MusicAsset
import de.ash.xkay.assets.SoundAsset
import de.ash.xkay.assets.TextureAtlasAsset
import de.ash.xkay.ui.LabelStyles
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.scene2d.*
import ktx.actors.*

/**
 * Initial state of the game. Here all the assets for the game are loaded and all states are created.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class LoadingScreen(game: Xkay) : XkayScreen(game) {

    private lateinit var progressBarTexture: Image
    private lateinit var progressBarLabel: Label
    private lateinit var touchToStart: Label

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
            TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) },
            SoundAsset.values().map { assets.loadAsync(it.descriptor) },
            MusicAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()

        // Launch coroutine to load all the assets
        KtxAsync.launch {
            assetRefs.joinAll()
            logger.debug { "Done loading in ${System.currentTimeMillis() - loadingStartTime} ms" }
            assetsLoaded()
        }

        // Continue to setup UI, etc. while the assets are loaded
        setupUI()
    }

    override fun hide() {
        stage.clear()
    }

    private fun setupUI() {
        stage.actors {
            table {

                // Default settings that are applied to all widgets
                defaults().fillX().expandX()

                label("Loading Screen", LabelStyles.DEFAULT.name) {
                    wrap = true
                    setAlignment(Align.center)
                }
                row()

                touchToStart = label("Touch to start", LabelStyles.DEFAULT.name) {
                    wrap = true
                    setAlignment(Align.center)
                    color.a = 0f
                }
                row()

                stack { cell ->
                    progressBarTexture = image("loading_bar").apply {
                        scaleX = 0f
                    }
                    progressBarLabel = label("Loading...", LabelStyles.DEFAULT.name) {
                        setAlignment(Align.center)
                    }
                    cell.padLeft(5f).padRight(5f)
                }

                setFillParent(true)
                pack()
            }
        }
    }

    override fun render(delta: Float) {

        if (assets.progress.isFinished
            && Gdx.input.justTouched()
            && game.containsScreen<IngameScreen>()) {

                // Change the screen and dispose the loading screen
                game.setScreen<IngameScreen>()
                game.removeScreen<LoadingScreen>()
                dispose()
        }

        progressBarTexture.scaleX = assets.progress.percent
        progressBarLabel.setText("${assets.progress.percent * 100} %")

        stage.run {
            viewport.apply()
            act(delta)
            draw()
        }
    }

    private fun assetsLoaded() {
        game.addScreen(IngameScreen(game))
        touchToStart += Actions.forever(Actions.sequence(Actions.fadeIn(0.5f) + Actions.fadeOut(0.5f)))
    }
}
