package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Rectangle;
import de.cptahmad.entity.Bullet;

import java.util.List;

/**
 * Created by ahmad on 07.09.17.
 */
public class NoShootingAi extends ShootingAi
{
    public NoShootingAi()
    {
        super(ShootingType.NONE);
    }

    @Override
    public void shoot(Rectangle shooter, List<Bullet> newBullets)
    {
    }
}
