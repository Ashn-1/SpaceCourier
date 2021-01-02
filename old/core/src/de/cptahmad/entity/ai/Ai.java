package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.cptahmad.entity.Bullet;

import java.util.List;

public class Ai
{
    private ShootingAi m_shootingAi;
    private MovementAi m_movementAi;

    public Ai(ShootingAi shootingAi, MovementAi movementAi)
    {
        m_shootingAi = shootingAi;
        m_movementAi = movementAi;
    }

    public void move(Vector2 pos, Vector2 vel, float delta)
    {
        m_movementAi.move(pos, vel, delta);
    }

    public void shoot(Rectangle shooter, List<Bullet> newBullets)
    {
        m_shootingAi.shoot(shooter, newBullets);
    }
}
