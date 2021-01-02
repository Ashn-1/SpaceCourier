package de.cptahmad.entity;

import com.badlogic.gdx.graphics.Camera;
import de.cptahmad.xkay.Assets;

import java.util.List;

public class Enemy extends Entity
{
    private de.cptahmad.entity.ai.Ai m_ai;

    public Enemy(float x, float y, de.cptahmad.entity.ai.Ai ai)
    {
        super(x, y);
        m_ai = ai;
        m_texture = Assets.getTexture(Assets.BASIC_ENEMY);
        m_hitbox.setSize(m_texture.getWidth(), m_texture.getHeight());
    }

    public void update(float delta, Camera cam, List<Bullet> newBullets)
    {
        m_ai.move(m_position, m_velocity, delta);
        m_position.add(m_velocity);

        // Check screen bounds and correct positon of ship if neccessary
        /*
        if (m_position.x > cam.viewportWidth - m_hitbox.width)
        {
            m_position.x = cam.viewportWidth - m_hitbox.width;
        } else if (m_position.x < 0)
        {
            m_position.x = 0;
        }
        */

        m_hitbox.setPosition(m_position);
        m_ai.shoot(m_hitbox, newBullets);
    }

    public boolean isOutOfBounds()
    {
        return (m_position.y < -m_hitbox.height - 15);
    }
}
