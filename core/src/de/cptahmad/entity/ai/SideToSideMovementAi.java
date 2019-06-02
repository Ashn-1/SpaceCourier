package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahmad on 07.09.17.
 */
public class SideToSideMovementAi extends StraightMovementAi
{
    public static Vector2 VELOCITY = new Vector2(120f, 6f);
    private static final float LOWEST_SCALING_VEL_X = 1f;

    private final float m_startingPoint, m_endPoint;

    private int direction = 1;

    public SideToSideMovementAi(float start, float end)
    {
        super(VELOCITY.y);
        m_startingPoint = start;
        m_endPoint = end;
    }

    @Override
    public void move(Vector2 pos, Vector2 vel, float delta)
    {
        // Does the vertical movement part
        super.move(pos, vel, delta);

        /*
        Does the horizontal movement part.
        The velocity in x direction is higher in the middle of the path and lower near the start and end point.
         */
        if(m_startingPoint < m_endPoint)
        {
            if(pos.x < m_startingPoint)
            {
                direction = 1;
            } else if(pos.x > m_endPoint)
            {
                direction = -1;
            }
        } else
        {
            if(pos.x > m_startingPoint)
            {
                direction = -1;
            } else if(pos.x < m_endPoint)
            {
                direction = 1;
            }
        }

        //float relPointInPath = Math.abs((pos.x - m_startingPoint) / (m_endPoint - m_startingPoint) - 1.0f);
        //float scaleVelX = MathUtilsEx.linearFunc(LOWEST_SCALING_VEL_X - 1, 1, relPointInPath);
        vel.x = direction * VELOCITY.x * delta;
    }
}
