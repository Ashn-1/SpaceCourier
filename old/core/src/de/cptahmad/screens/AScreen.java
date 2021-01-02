package de.cptahmad.screens;

import com.badlogic.gdx.Screen;
import de.cptahmad.handler.GuiHandler;
import de.cptahmad.xkay.XkayGame;

/**
 * Created by ahmad on 10.08.17.
 */
public abstract class AScreen implements Screen
{
    public AScreen(XkayGame game)
    {
        m_game = game;
    }

    protected XkayGame   m_game;
    protected GuiHandler m_guiHandler;
    protected Overlay    m_overlay;

    protected abstract void update(float delta);
}
