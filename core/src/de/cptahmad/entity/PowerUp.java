package de.cptahmad.entity;

import com.badlogic.gdx.graphics.Camera;
import de.cptahmad.entity.ai.MovementAi;
import de.cptahmad.entity.ai.StraightMovementAi;
import de.cptahmad.xkay.Assets;

/**
 * Created by ahmad on 26.08.17.
 */
public class PowerUp extends Entity
{
    private MovementAi m_movement;

    public PowerUp(Camera camera)
    {
        super();
        m_movement = new StraightMovementAi(6f);
        m_texture = Assets.getTexture(Assets.POWER_UP);
        m_position.set(camera.viewportWidth / 2f - m_texture.getWidth() / 2f, camera.viewportHeight);
        m_hitbox.setPosition(m_position);
        m_hitbox.setSize(m_texture.getWidth(), m_texture.getHeight());
    }

    public void update(float delta)
    {
        m_movement.move(m_position, m_velocity, delta);
        move();
        m_hitbox.setPosition(m_position);
    }

    public boolean isOutOfBounds()
    {
        return (m_position.y < 0f);
    }
}
