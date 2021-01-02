package de.cptahmad.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.cptahmad.entity.Player;
import de.cptahmad.handler.EntitiyHandler;
import de.cptahmad.handler.GuiHandler;
import de.cptahmad.xkay.Assets;
import de.cptahmad.xkay.XkayGame;

public class GameScreen extends AScreen
{
    private final Label m_highscoreLabel, m_fpsLabel;
    private OrthographicCamera m_cam;
    private Texture            m_background;
    private Vector2            m_touchPos = new Vector2();

    private Player         m_player;
    private EntitiyHandler m_entityHandler;
    private GuiHandler     m_guiHandler;

    private float m_yOffsetBackground;

    public GameScreen(XkayGame game)
    {
        super(game);

        m_cam = new OrthographicCamera();
        m_cam.setToOrtho(false, 360 / 3, 640 / 3); // 120x214
        m_cam.update();

        m_guiHandler = new GuiHandler(m_cam, Assets.getSpriteBatch());
        m_highscoreLabel = new Label("Loading...", Assets.getSkin(), "small");
        m_guiHandler.getTable().add(m_highscoreLabel).expand().bottom().left().pad(5f);
        m_fpsLabel = new Label("FPS: 0", Assets.getSkin(), "small");
        m_guiHandler.getTable().add(m_fpsLabel).expand().bottom().right().pad(5f);

        m_background = Assets.getTexture(Assets.BACKGROUND);
        m_yOffsetBackground = -MathUtils.random(m_background.getHeight() + m_cam.viewportHeight);

        m_player = new Player(m_cam);
        m_entityHandler = new EntitiyHandler(m_player, m_cam);
    }

    @Override
    protected void update(float delta)
    {
        m_fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

        m_touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        Vector3 touchPos3 = m_cam.unproject(new Vector3(m_touchPos, 0));
        m_touchPos.set(touchPos3.x, touchPos3.y);
        boolean isTouched = Gdx.input.isTouched();

        if (m_overlay != null)
        {
            m_overlay.update(delta, m_touchPos, isTouched);
            return;
        }

        if (!m_entityHandler.isGameOver())
        {
            m_yOffsetBackground -= (0.016f * 4);
            if (m_yOffsetBackground < -m_background.getHeight() + m_cam.viewportHeight)
            {
                m_yOffsetBackground = 0;
            }
        }

        m_entityHandler.update(delta);
        m_highscoreLabel.setText("Highscore: " + m_player.getHighscore());
        if (m_entityHandler.isGameOver())
        {
            m_overlay = new GameOverScreen(this, m_cam, Assets.getSpriteBatch(), m_player.getHighscore());
        }
    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.getSpriteBatch().begin();

        Assets.getSpriteBatch().draw(m_background, 0f, m_yOffsetBackground);
        m_entityHandler.render(Assets.getSpriteBatch());

        Assets.getSpriteBatch().end();

        m_guiHandler.render(delta);

        if (m_overlay != null)
        {
            m_overlay.render(Assets.getSpriteBatch(), delta);
        }
    }

    public void initNewGame()
    {
        Gdx.app.log("GameScreen", "New game");

        m_entityHandler.reset();
        m_yOffsetBackground = -MathUtils.random(m_background.getHeight() + m_cam.viewportHeight);
        m_overlay = null;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height)
    {
    }

    @Override
    public void show()
    {
    }

    @Override
    public void hide()
    {
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
    public void dispose()
    {
        m_entityHandler.dispose();
    }
}
