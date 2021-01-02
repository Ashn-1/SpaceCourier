package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahmad on 16.08.17.
 */
public interface MovementAi
{
    void move(Vector2 pos, Vector2 vel, float delta);
}
