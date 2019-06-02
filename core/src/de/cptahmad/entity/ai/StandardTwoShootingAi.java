package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import de.cptahmad.entity.Bullet;

import java.util.List;

/**
 * Created by ahmad on 05.09.17.
 */
public class StandardTwoShootingAi extends StandardShootingAi
{
    public StandardTwoShootingAi(boolean shootUp, long cooldown, boolean isPlayerBullet)
    {
        super(shootUp, cooldown, isPlayerBullet);
    }

    @Override
    public void shoot(Rectangle shooter, List<Bullet> newBullets)
    {
        if (TimeUtils.timeSinceMillis(m_lastTimeFired) > m_fireCooldown)
        {

            Bullet bullet1 = new Bullet(0f, 0f, 0f, m_direction * 200f, m_isPlayerBullet);
            Bullet bullet2 = new Bullet(0f, 0f, 0f, m_direction * 200f, m_isPlayerBullet);
            float  x1      = shooter.x + shooter.getWidth() * 0.1f;
            float x2 = shooter.x + shooter.getWidth() * 0.9f - bullet1.getHitbox().width;
            float  y      = shooter.y + shooter.getHeight() / 2;
            bullet1.getPosition().set(x1, y);
            bullet2.getPosition().set(x2, y);

            newBullets.add(bullet1);
            newBullets.add(bullet2);
            m_lastTimeFired = TimeUtils.millis();
        }
    }
}
