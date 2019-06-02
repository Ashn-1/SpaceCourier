package de.cptahmad.xkay.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import de.cptahmad.xkay.XkayGame;

public class HtmlLauncher extends GwtApplication
{

    @Override
    public GwtApplicationConfiguration getConfig()
    {
        return new GwtApplicationConfiguration(360, 640);
    }

    @Override
    public ApplicationListener createApplicationListener()
    {
        return new XkayGame();
    }
}