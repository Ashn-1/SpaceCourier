package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import de.cptahmad.entity.Bullet;

import java.util.List;

/**
 * Created by ahmad on 16.08.17.
 */
public class StandardShootingAi extends ShootingAi
{
    protected final long m_fireCooldown;
    protected long m_lastTimeFired = 0l;

    protected int m_direction;

    protected boolean m_isPlayerBullet;

    /**
     * This constructor is mainly for the enemies.
     *
     * @param shootUp True if the bullet should go up.
     */
    public StandardShootingAi(boolean shootUp)
    {
        this(shootUp, 5000L, false);
        m_lastTimeFired = MathUtils.random(m_fireCooldown) + TimeUtils.millis();
    }

    public StandardShootingAi(boolean shootUp, long cooldown, boolean isPlayerBullet)
    {
        super(ShootingType.STANDARD);
        m_direction = shootUp ? 1 : -1;
        m_fireCooldown = cooldown;
        m_isPlayerBullet = isPlayerBullet;
    }

    @Override
    public void shoot(Rectangle shooter, List<Bullet> newBullets)
    {
        if (TimeUtils.timeSinceMillis(m_lastTimeFired) > m_fireCooldown)
        {
            m_lastTimeFired = TimeUtils.millis();
            Bullet bullet = new Bullet(0f, 0f, 0f, m_direction * 200f, m_isPlayerBullet);
            float  x      = shooter.x + shooter.getWidth() / 2 - bullet.getHitbox().getWidth() / 2;
            float  y      = shooter.y + shooter.getHeight() / 2;
            bullet.getPosition().set(x, y);

            newBullets.add(bullet);
        }
    }
}
