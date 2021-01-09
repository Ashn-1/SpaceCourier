package de.ash.xkay

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import de.ash.xkay.main.Xkay

/**
 * Entry point to the desktop version of the application.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

const val WINDOW_WIDTH = 360
const val WINDOW_HEIGHT = 720

fun main()
{
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("XKay")
        setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        setResizable(true)
    }

    Lwjgl3Application(Xkay(), config)
}
