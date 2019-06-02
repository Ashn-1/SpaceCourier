package de.cptahmad.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.cptahmad.handler.GuiHandler;

/**
 * Created by ahmad on 10.08.17.
 */
public abstract class Overlay
{
    protected final GuiHandler         m_guiHandler;
    protected final AScreen            m_parent;
    protected       OrthographicCamera m_cam;

    public Overlay(AScreen parent, OrthographicCamera cam, SpriteBatch batch)
    {
        m_parent = parent;
        m_guiHandler = new GuiHandler(cam, batch);
        m_cam = cam;
    }

    public abstract void update(float delta, Vector2 mousePos, boolean isTouched);

    public abstract void render(SpriteBatch batch, float delta);
}
