package de.cptahmad.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.cptahmad.xkay.Assets;

/**
 * Created by ahmad on 10.08.17.
 */
public class GameOverScreen extends Overlay
{
    private ShapeRenderer m_opaqueRect = new ShapeRenderer();

    public GameOverScreen(AScreen parent, OrthographicCamera cam, SpriteBatch batch, int highscore)
    {
        super(parent, cam, batch);
        m_opaqueRect.setProjectionMatrix(cam.combined);

        final TextButton gameOverButton = new TextButton("RESTART", Assets.getSkin());
        gameOverButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                ((GameScreen) m_parent).initNewGame();
            }
        });
        final Label highscoreLabel = new Label("Game Over\nHighscore: " + highscore, Assets.getSkin());

        m_guiHandler.getTable().add(highscoreLabel).pad(40f).row();
        m_guiHandler.getTable().add(gameOverButton);

        m_guiHandler.getTable().pack();
    }

    @Override
    public void update(float delta, Vector2 mousePos, boolean isTouched)
    {
    }

    @Override
    public void render(SpriteBatch batch, float delta)
    {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        m_opaqueRect.begin(ShapeRenderer.ShapeType.Filled);
        m_opaqueRect.setColor(new Color(0, 0, 0, 0.75f));
        m_opaqueRect.rect(0, 0, m_cam.viewportWidth, m_cam.viewportHeight);
        m_opaqueRect.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        m_guiHandler.render(delta);
    }
}
