@file:JvmName("Lwjgl3Launcher")

package de.ashenlab.spacecourier.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import de.ashenlab.spacecourier.Main

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(Main(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Space Courier")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
