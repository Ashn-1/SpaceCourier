package de.ash.xkay.screens

import de.ash.xkay.main.Xkay
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

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }
}