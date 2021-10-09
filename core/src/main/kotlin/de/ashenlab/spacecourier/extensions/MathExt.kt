package de.ashenlab.spacecourier.extensions

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

fun ShapeRenderer.circle(c: Circle) {
    circle(c.x, c.y, c.radius)
}

fun Circle.draw(shapeRenderer: ShapeRenderer) {
    shapeRenderer.circle(this)
}
