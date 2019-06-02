package de.cptahmad.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.cptahmad.entity.ai.ShootingAi;
import de.cptahmad.entity.ai.ShootingAi.ShootingType;
import de.cptahmad.xkay.Assets;
import de.cptahmad.xkay.Debug;

import java.util.List;

public class Player extends Entity
{
    private enum TouchType
    {
        NONE,
        LEFT,
        RIGHT
    }

    private Vector2 m_middlePos = new Vector2();
    private Vector3 m_touchPos0 = new Vector3();
    private Vector3 m_touchPos1 = new Vector3();

    private ShootingAi m_shootingAi;

    // 0 = no touch, 1 = left first, 2 = right first
    private TouchType m_touchDownFirst = TouchType.NONE;

    private int m_highscore = 0;
    private int m_level     = 0;

    private float m_movementSpeed = 100f;

    public Player(OrthographicCamera cam)
    {
        super(cam.viewportWidth / 2f, cam.viewportHeight * 0.2f);
        m_texture = Assets.getTexture(Assets.SHIP);
        m_hitbox.setSize(m_texture.getWidth(), m_texture.getHeight());
        m_shootingAi = ShootingAi.getShootingAiAccordingToLevel(ShootingType.STANDARD, m_level);
    }

    public void reset(Camera cam)
    {
        m_position.set(cam.viewportWidth / 2f, cam.viewportHeight * 0.2f);
        m_highscore = 0;
        m_shootingAi = ShootingAi.getShootingAiAccordingToLevel(m_shootingAi.m_type, m_level = 0);
    }

    public void update(float delta, Camera cam, List<Bullet> newBullets)
    {
        m_touchPos0.set(Gdx.input.getX(0), 0f, 0f);
        m_touchPos1.set(Gdx.input.getX(1), 0f, 0f);

        m_touchPos0 = cam.unproject(m_touchPos0);
        m_touchPos1 = cam.unproject(m_touchPos1);

        boolean isLeftPressed = ((Gdx.input.isTouched(0) && m_touchPos0.x < cam.viewportWidth / 2)
                                 || Gdx.input.isTouched(1) && m_touchPos1.x < cam.viewportWidth / 2);
        boolean isRightPressed = ((Gdx.input.isTouched(0) && m_touchPos0.x >= cam.viewportWidth / 2)
                                  || Gdx.input.isTouched(1) && m_touchPos1.x >= cam.viewportWidth / 2);

        m_hitbox.getCenter(m_middlePos);

        switch (Gdx.app.getType())
        {
            case Android:
                if (!isLeftPressed && !isRightPressed)
                {
                    m_velocity.x = 0f;
                } else if (!isLeftPressed && isRightPressed)
                {
                    m_velocity.x = m_movementSpeed;
                    m_touchDownFirst = TouchType.RIGHT;
                } else if (isLeftPressed && !isRightPressed)
                {
                    m_velocity.x = -m_movementSpeed;
                    m_touchDownFirst = TouchType.LEFT;
                } else
                {
                    switch (m_touchDownFirst)
                    {
                        case LEFT:
                            m_velocity.x = m_movementSpeed;
                            m_touchDownFirst = TouchType.NONE;
                            break;
                        case RIGHT:
                            m_velocity.x = -m_movementSpeed;
                            m_touchDownFirst = TouchType.NONE;
                            break;
                    }
                }
                break;
            case Desktop:
                if (Gdx.input.isKeyPressed(Input.Keys.A) == Gdx.input.isKeyPressed(Input.Keys.D))
                {
                    m_velocity.x = 0f;
                } else if (Gdx.input.isKeyPressed(Input.Keys.A))
                {
                    m_velocity.x = -m_movementSpeed;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D))
                {
                    m_velocity.x = m_movementSpeed;
                }
                break;
            default:
                break;
        }

        m_position.mulAdd(m_velocity, delta);

        // Check screen bounds and correct positon of ship if neccessary
        if (m_position.x > cam.viewportWidth - m_hitbox.width)
        {
            m_position.x = cam.viewportWidth - m_hitbox.width;
        } else if (m_position.x < 0)
        {
            m_position.x = 0;
        }

        m_hitbox.setPosition(m_position);

        if (Debug.CAN_PLAYER_SHOOT)
        {
            m_shootingAi.shoot(m_hitbox, newBullets);
        }
    }

    public void powerUp()
    {
        if (m_level < m_shootingAi.m_type.m_maxLevel)
        {
            m_shootingAi = ShootingAi.getShootingAiAccordingToLevel(m_shootingAi.m_type, ++m_level);
        }
    }

    public void increaseHighscore(int points)
    {
        m_highscore += points;
    }

    public int getHighscore()
    {
        return m_highscore;
    }
}