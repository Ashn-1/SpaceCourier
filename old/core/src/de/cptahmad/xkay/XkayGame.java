package de.cptahmad.xkay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.cptahmad.screens.MainMenuScreen;

public class XkayGame extends Game
{
    @Override
    public void create()
    {
        Assets.load();
        setScreen(new MainMenuScreen(this));

        // Set all debug options here
        Debug.CAN_PLAYER_SHOOT = true;
        Debug.IS_PLAYER_INVINCIBLE = false;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.badlogic.gdx.Game#dispose()
     */
    @Override
    public void dispose()
    {
        super.dispose();
        getScreen().dispose();
        Assets.dispose();

        Gdx.app.log("Xkay", "Done disposing");
    }
}
