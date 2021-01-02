package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahmad on 16.08.17.
 */
public class LateralMovementAi extends StraightMovementAi
{
    private static final float ONE_DIRECTION_MAX = 15f;
    private static final float VELOCITY_X        = 10f;
    private              float m_direction       = 1f, m_distanceTravelledX = 0f;
    private final float m_scale;

    public LateralMovementAi()
    {
        this(1f, 1f);
    }

    public LateralMovementAi(float scaleX, float scaleY)
    {
        super(scaleY);
        m_scale = scaleX;
    }

    @Override
    public void move(Vector2 pos, Vector2 vel, float delta)
    {
        super.move(pos, vel, delta);

        vel.x = m_direction * VELOCITY_X * delta;
        m_distanceTravelledX += VELOCITY_X * delta;

        if (m_distanceTravelledX > ONE_DIRECTION_MAX * m_scale)
        {
            m_direction *= -1;
            m_distanceTravelledX = 0;
        }
    }

    public float getMaximumDisplacement()
    {
        return ONE_DIRECTION_MAX * m_scale;
    }
}
