package de.cptahmad.entity.ai;

import com.badlogic.gdx.math.Rectangle;
import de.cptahmad.entity.Bullet;

import java.util.List;

/**
 * Created by ahmad on 16.08.17.
 */
public abstract class ShootingAi
{
    public enum ShootingType
    {
        NONE(0),
        STANDARD(1);

        /**
         * Maximum level of the shooting type. Levels always start with 0.
         */
        public final int m_maxLevel;

        ShootingType(int maxLevel)
        {
            m_maxLevel = maxLevel;
        }
    }

    public final ShootingType m_type;

    public ShootingAi(ShootingType type)
    {
        m_type = type;
    }

    /**
     * The shoot method checks if the condition for firing again is met and, if true, adds new bullets to the given list.
     *
     * @param shooter the hitbox of the shooting entity
     * @param newBullets the list for adding the new bullets
     */
    public abstract void shoot(Rectangle shooter, List<Bullet> newBullets);

    /**
     * Returns a new instance of a ShootingAi implementation according to the type and level provided.
     *
     * @param type the ShootingType of the new ShootingAi
     * @param level the level of the new ShootingAi
     * @return the new ShootingAi instance
     */
    public static ShootingAi getShootingAiAccordingToLevel(ShootingType type, int level)
    {
        // TODO add more shooting ai's for the player

        ShootingAi newShootingAi = null;
        switch (type)
        {
            case STANDARD:
                switch (level)
                {
                    case 0:
                        newShootingAi = new StandardShootingAi(true, 500L, true);
                        break;
                    case 1:
                        newShootingAi = new StandardTwoShootingAi(true, 500L, true);
                        break;
                }
                break;
        }
        return newShootingAi;
    }
}
