package de.cptahmad.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity
{
    protected Vector2   m_position = new Vector2();
    protected Vector2   m_velocity = new Vector2();
    protected Rectangle m_hitbox   = new Rectangle();

    protected Texture m_texture;

    public Entity()
    {
        this(0f, 0f);
    }

    public Entity(float x, float y)
    {
        this(x, y, 0f, 0f);
    }

    public Entity(float x, float y, float dx, float dy)
    {
        this(x, y, dx, dy, 0f, 0f);
    }

    public Entity(float x, float y, float dx, float dy, float width, float height)
    {
        m_position.set(x, y);
        m_velocity.set(dx, dy);
        m_hitbox.set(x, y, width, height);
    }

    public void render(SpriteBatch batch)
    {
        batch.draw(m_texture, m_position.x, m_position.y);
    }

    public void move()
    {
        m_position.add(m_velocity);
    }

    public Vector2 getPosition()
    {
        return m_position;
    }

    public Rectangle getHitbox()
    {
        return m_hitbox;
    }

    public float getWidth()
    {
        if(m_hitbox == null)
        {
            return 0f;
        }
        return m_hitbox.width;
    }

    public float getHeight()
    {
        if(m_hitbox == null)
        {
            return 0f;
        }
        return m_hitbox.height;
    }
}
