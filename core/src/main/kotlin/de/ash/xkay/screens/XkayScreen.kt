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
}