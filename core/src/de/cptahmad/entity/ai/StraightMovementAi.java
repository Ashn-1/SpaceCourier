package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahmad on 16.08.17.
 */
public class StraightMovementAi implements MovementAi
{
    private final float m_scale;

    public StraightMovementAi()
    {
        this(1f);
    }

    public StraightMovementAi(float scale)
    {
        m_scale = scale;
    }

    @Override
    public void move(Vector2 pos, Vector2 vel, float delta)
    {
        vel.y = -7.5f * delta * m_scale;
    }
}
