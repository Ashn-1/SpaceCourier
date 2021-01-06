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
fun main()
{
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("XKay")
        setWindowedMode(360, 640)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        setResizable(false)
    }

    Lwjgl3Application(Xkay(), config)
}
