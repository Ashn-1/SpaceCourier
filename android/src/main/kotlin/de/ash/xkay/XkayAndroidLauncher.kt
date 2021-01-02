package de.ash.xkay

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

/**
 * Entry point for the android version of the application.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class XkayAndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        initialize(Xkay(), config)
    }
}