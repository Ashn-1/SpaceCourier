package de.ash.xkay.screens

import de.ash.xkay.Xkay
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

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }
}