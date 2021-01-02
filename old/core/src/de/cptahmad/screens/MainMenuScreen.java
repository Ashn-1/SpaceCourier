package de.cptahmad.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.cptahmad.handler.GuiHandler;
import de.cptahmad.xkay.Assets;
import de.cptahmad.xkay.XkayGame;

public class MainMenuScreen extends AScreen
{
    private OrthographicCamera m_cam;
    private Vector2 m_touchPos = new Vector2();

    public MainMenuScreen(XkayGame game)
    {
        super(game);

        m_cam = new OrthographicCamera();
        m_cam.setToOrtho(false, 360f / 3f, 640f / 3f);
        m_cam.update();

        Assets.getSpriteBatch().setProjectionMatrix(m_cam.combined);

        m_guiHandler = new GuiHandler(m_cam, Assets.getSpriteBatch());
        final TextButton startButton = new TextButton("START", Assets.getSkin());
        startButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                m_game.setScreen(new GameScreen(m_game));
            }
        });
        final TextButton quitButton = new TextButton("QUIT", Assets.getSkin());
        quitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.app.exit();
            }
        });

        m_guiHandler.getTable().add(startButton).pad(20f).row();
        m_guiHandler.getTable().add(quitButton).pad(20f);
    }

    @Override
    protected void update(float delta)
    {
    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.getSpriteBatch().setProjectionMatrix(m_cam.combined);

        //m_game.getSpriteBatch().begin();

        m_guiHandler.render(delta);

        //m_game.getSpriteBatch().end();
    }

    @Override
    public void show()
    {
    }

    @Override
    public void resize(int width, int height)
    {
        m_guiHandler.resize(width, height);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void dispose()
    {
    }
}
