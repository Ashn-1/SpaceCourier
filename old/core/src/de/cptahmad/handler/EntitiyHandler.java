package de.cptahmad.handler;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Disposable;
import de.cptahmad.entity.Bullet;
import de.cptahmad.entity.Enemy;
import de.cptahmad.entity.Player;
import de.cptahmad.entity.PowerUp;
import de.cptahmad.entity.ai.*;
import de.cptahmad.xkay.Debug;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ahmad on 15.08.17.
 */
public class EntitiyHandler implements Disposable
{
    private final List<Bullet> m_playerBullets;
    private final List<Bullet> m_enemyBullets;
    private final List<Enemy>  m_enemies;
    private PowerUp m_powerUp = null;

    private final Player        m_player;
    private final Camera        m_cam;
    private final ShapeRenderer m_shape;

    private boolean m_isGameOver = false;

    public EntitiyHandler(Player player, Camera cam)
    {
        m_playerBullets = new ArrayList<>();
        m_enemyBullets = new ArrayList<>();
        m_enemies = new ArrayList<>();

        m_player = player;
        m_cam = cam;

        m_shape = new ShapeRenderer();
    }

    public void update(float delta)
    {
        if (m_isGameOver)
        {
            return;
        }

        // Update enemy positions
        for (Iterator<Enemy> enemyIter = m_enemies.iterator(); enemyIter.hasNext(); )
        {
            Enemy enemy = enemyIter.next();
            enemy.update(delta, m_cam, m_enemyBullets);
            if (enemy.isOutOfBounds())
            {
                enemyIter.remove();
            }
        }

        /*
        Check if any player bullet collided with an enemy and delete both
         */
        for (Iterator<Bullet> bulletIter = m_playerBullets.iterator(); bulletIter.hasNext(); )
        {
            Bullet bullet = bulletIter.next();
            // Update bullet position
            bullet.update(delta);
            // If the bullet is out of bounds, delete it
            if (bullet.isOutOfBounds(m_cam))
            {
                bulletIter.remove();
                continue;
            }
            // Check for collision with enemy ship
            for (Iterator<Enemy> enemyIter = m_enemies.iterator(); enemyIter.hasNext(); )
            {
                Enemy enemy = enemyIter.next();
                // If the enemy is hit by a bullet, delete both
                if (Intersector.overlaps(enemy.getHitbox(), bullet.getHitbox()))
                {
                    m_player.increaseHighscore(50);
                    enemyIter.remove();
                    bulletIter.remove();
                    if (m_enemies.isEmpty())
                    {
                        spawnPowerUp();
                    }
                    break;
                }
            }
        }

        /*
        Check if any enemy bullet collided with the player and end the game
         */
        for (Bullet bullet : m_enemyBullets)
        {
            bullet.update(delta);
            if (!Debug.IS_PLAYER_INVINCIBLE && Intersector.overlaps(m_player.getHitbox(), bullet.getHitbox()))
            {
                m_isGameOver = true;
                return;
            }
        }

        // Update player position and shoot bullet
        m_player.update(delta, m_cam, m_playerBullets);

        // Update power up and check if player touches it
        if (m_powerUp != null)
        {
            m_powerUp.update(delta);
            if (Intersector.overlaps(m_player.getHitbox(), m_powerUp.getHitbox()))
            {
                m_player.powerUp();
                m_powerUp = null;
            } else if (m_powerUp.isOutOfBounds())
            {
                m_powerUp = null;
            }
        }

        if (m_enemies.isEmpty() && m_powerUp == null)
        {
            spawnStandardWave();
        }
    }

    /**
     * Renders all entities
     * SpriteBatchs begin and end method are not called in this method
     * Neither are the glClearColor and glClear method
     *
     * @param batch The SpriteBatch to draw to
     */
    public void render(SpriteBatch batch)
    {
        m_player.render(batch);
        for (Enemy enemy : m_enemies)
        {
            enemy.render(batch);
        }
        for (Bullet bullet : m_enemyBullets)
        {
            bullet.render(batch);
        }
        for (Bullet bullet : m_playerBullets)
        {
            bullet.render(batch);
        }
        if (m_powerUp != null)
        {
            m_powerUp.render(batch);
        }

        /*
        // DEBUG
        m_shape.setProjectionMatrix(m_cam.combined);
        m_shape.begin(ShapeRenderer.ShapeType.Line);
        // Player hitbox
        m_shape.rect(m_player.getHitbox().x, m_player.getHitbox().y, m_player.getHitbox().width, m_player.getHitbox().height);
        // Power up hitbox
        if(m_powerUp != null)
        {
            Gdx.app.log("DEBUG", "POWER UP");
            m_shape.rect(m_powerUp.getHitbox().x, m_powerUp.getHitbox().y, m_powerUp.getHitbox().width,
                         m_powerUp.getHitbox().height);
        }
        m_shape.end();
        */
    }

    public void reset()
    {
        m_player.reset(m_cam);
        m_isGameOver = false;
        m_enemies.clear();
        m_playerBullets.clear();
        m_enemyBullets.clear();
    }

    private void spawnStandardWave()
    {
        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 5; y++)
            {
                Ai ai = new Ai(new StandardShootingAi(false), new LateralMovementAi());
                Enemy enemy = new Enemy(m_cam.viewportWidth * 0.075f + m_cam.viewportWidth * 0.20f * x,
                                        m_cam.viewportHeight + 10 + m_cam.viewportHeight * 0.075f * y, ai);
                m_enemies.add(enemy);
            }
        }
    }

    private void spawnSideToSideWave()
    {
        float start = m_cam.viewportWidth * -0.25f;
        float end = m_cam.viewportWidth * 1.25f;
        float x = m_cam.viewportWidth * -0.15f;
        float y = m_cam.viewportHeight * 0.80f;

        for(int i = 0; i < 10; i++)
        {
            Ai ai = new Ai(new NoShootingAi(), new SideToSideMovementAi(start, end));
            Enemy enemy = new Enemy(x, y, ai);
            x -= SideToSideMovementAi.VELOCITY.x;
            y += SideToSideMovementAi.VELOCITY.y;

            m_enemies.add(enemy);
        }
    }

    private void spawnPowerUp()
    {
        m_powerUp = new PowerUp(m_cam);
    }

    public boolean isGameOver()
    {
        return m_isGameOver;
    }

    @Override
    public void dispose()
    {
        m_enemies.clear();
        m_playerBullets.clear();
        m_enemyBullets.clear();
    }
}
