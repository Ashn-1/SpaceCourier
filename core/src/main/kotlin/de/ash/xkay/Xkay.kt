package de.ash.xkay

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Main class of the application. It handles all the screens, resources, etc. of the game.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class Xkay : ApplicationAdapter() {
    val batch: SpriteBatch by lazy { SpriteBatch() }
    val image: Texture by lazy { Texture("badlogic.png") }

    override fun create()
    {
    }

    override fun render()
    {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(image, 165f, 180f)
        batch.end()
    }

    override fun dispose()
    {
        batch.dispose()
        image.dispose()
    }
}