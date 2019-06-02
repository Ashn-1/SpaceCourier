package de.cptahmad.xkay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by ahmad on 10.08.17.
 */
public final class Assets
{
    private static final AssetManager s_assetManager = new AssetManager();

    private static final Skin s_skin = new Skin();

    private static final SpriteBatch s_batch = new SpriteBatch();

    /*
    private static final TextureRegion[] s_characters   = new TextureRegion[30];

    private static final BitmapFont s_font = new BitmapFont();

    public static final float CHARACTER_SIZE = 12;
    */

    public static final String BACKGROUND = "textures/background.png", SHIP = "textures/ship.png",
            BASIC_ENEMY                   = "textures/basicenemy.png",
            BULLET                        = "textures/bullet.png", BULLET_ENEMY = "textures/bulletenemy.png",
            GAME_OVER                     = "textures/gameover.png",
            ALPHABET                      = "textures/characters.png", BUTTON = "textures/button.png",
            POWER_UP                      = "textures/bonus.png";

    private Assets()
    {
    }

    public static void load()
    {
        // Load Textures
        s_assetManager.load("textures/background.png", Texture.class);
        s_assetManager.load("textures/ship.png", Texture.class);
        s_assetManager.load("textures/basicenemy.png", Texture.class);
        s_assetManager.load("textures/bullet.png", Texture.class);
        s_assetManager.load("textures/bulletenemy.png", Texture.class);
        s_assetManager.load("textures/gameover.png", Texture.class);
        s_assetManager.load("textures/characters.png", Texture.class);
        s_assetManager.load("textures/button.png", Texture.class);
        s_assetManager.load(POWER_UP, Texture.class);

        // Load True Type Font
        /*
        FileHandleResolver resolver = new InternalFileHandleResolver();
        s_assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        s_assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreeTypeFontLoaderParameter big_text = new FreeTypeFontLoaderParameter();
        big_text.fontFileName = "fonts/MontserratAlternates-Bold.ttf";
        big_text.fontParameters.size = 12;
        s_assetManager.load("big-font.ttf", BitmapFont.class, big_text);

        FreeTypeFontLoaderParameter small_text = new FreeTypeFontLoaderParameter();
        small_text.fontFileName = "fonts/MontserratAlternates-Bold.ttf";
        small_text.fontParameters.size = 6;
        s_assetManager.load("small-font.ttf", BitmapFont.class, small_text);
        */

        // Finish loading all assets
        s_assetManager.finishLoading();

        // Load skins
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/MontserratAlternates-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterBig.size = 12;
        FreeTypeFontGenerator.FreeTypeFontParameter parameterSmall = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterSmall.size = 6;
        BitmapFont fontBig   = generator.generateFont(parameterBig);
        BitmapFont fontSmall = generator.generateFont(parameterSmall);
        generator.dispose();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        s_skin.add("white", new Texture(pixmap));
        s_skin.add("default", fontBig);
        s_skin.add("small", fontSmall);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = s_skin.newDrawable("white", Color.MAGENTA);
        textButtonStyle.down = s_skin.newDrawable("white", Color.RED);
        textButtonStyle.checked = s_skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = s_skin.newDrawable("white", Color.YELLOW);
        textButtonStyle.font = s_skin.getFont("default");
        s_skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = s_skin.getFont("default");
        s_skin.add("default", labelStyleBig);

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = s_skin.getFont("small");
        s_skin.add("small", labelStyleSmall);

        // Log that all assets have been loaded
        Gdx.app.log("Assets", "Done loading assets");
    }

    public static Texture getTexture(String name)
    {
        return s_assetManager.get(name, Texture.class);
    }

    /*
    public static BitmapFont getFont(boolean bigFont)
    {
        if (bigFont)
            return s_assetManager.get("big-font.ttf", BitmapFont.class);
        else
            return s_assetManager.get("small-font.ttf", BitmapFont.class);
    }
*/

    public static Skin getSkin()
    {
        return s_skin;
    }

    public static SpriteBatch getSpriteBatch() { return s_batch; }

    public static void dispose()
    {
        s_assetManager.dispose();
        s_skin.dispose();
        s_batch.dispose();
    }
}
