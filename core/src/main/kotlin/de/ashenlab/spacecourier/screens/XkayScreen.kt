package de.ashenlab.spacecourier.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.ashenlab.spacecourier.main.Xkay
import ktx.app.KtxScreen

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
open class XkayScreen(val game: Xkay) : KtxScreen {
    val assets = game.assets
    val batch = game.batch
    val gameViewport = game.gameViewport
    val uiViewport = game.uiViewport
    val eventManager = game.eventManager
    val stage = game.stage
    val audioService = game.audioService
    val preferences = game.preferences

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    protected fun isBackButtonPressed() : Boolean {
        return when (Gdx.app.type) {
            Application.ApplicationType.Android -> {
                Gdx.input.isKeyJustPressed(Input.Keys.BACK)
            }
            Application.ApplicationType.Desktop -> {
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            }
            else -> false

        }
    }
}