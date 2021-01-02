package de.cptahmad.entity;

import com.badlogic.gdx.graphics.Camera;
import de.cptahmad.xkay.Assets;

public class Bullet extends Entity
{

    public Bullet(float x, float y, float dx, float dy, boolean isPlayerBullet)
    {
        super(x, y, dx, dy);
        m_texture = isPlayerBullet ? Assets.getTexture(Assets.BULLET) : Assets.getTexture(Assets.BULLET_ENEMY);
        m_hitbox.setSize(m_texture.getWidth(), m_texture.getHeight());
    }

    public void update(float delta)
    {
        m_position.mulAdd(m_velocity, delta);
        m_hitbox.setPosition(m_position);
    }

    public boolean isOutOfBounds(Camera cam)
    {
        return (m_position.x > cam.viewportWidth || m_position.x < -m_hitbox.width || m_position.y < -m_hitbox.height
                || m_position.y > cam.viewportHeight);

    }
}
