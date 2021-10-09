package de.ashenlab.spacecourier

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import de.ashenlab.spacecourier.main.Xkay

/**
 * Entry point for the android version of the application.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class XkayAndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(Xkay(), AndroidApplicationConfiguration().apply {
            hideStatusBar = true
            useImmersiveMode = true
            useWakelock = true
            useAccelerometer = true

            useCompass = false
        })
    }
}