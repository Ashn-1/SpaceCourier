package de.cptahmad.xkay.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.cptahmad.xkay.XkayGame;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Project XKay";
        config.width = 360;
        config.height = 640;
        config.resizable = false;
        new LwjglApplication(new XkayGame(), config);
    }
}
