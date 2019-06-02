package de.cptahmad.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GuiHandler implements Disposable
{
    private Stage m_stage;
    private Table m_table;

    public GuiHandler(Camera camera, SpriteBatch batch)
    {
        m_stage = new Stage(new StretchViewport(camera.viewportWidth, camera.viewportHeight), batch);

        Gdx.input.setInputProcessor(m_stage);

        m_table = new Table();
        m_table.setFillParent(true);
        m_stage.addActor(m_table);

        /*
        ONLY FOR DEBUG
         */

        //m_table.debug();
    }

    public Table getTable()
    {
        return m_table;
    }

    public void render(float delta)
    {
        m_stage.act(delta);
        m_stage.draw();
    }

    @Override
    public void dispose()
    {
        m_stage.dispose();
    }

    public void resize(int width, int height)
    {
        m_stage.getViewport().update(width, height);
    }
}