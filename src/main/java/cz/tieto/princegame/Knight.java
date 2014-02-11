package cz.tieto.princegame;

import static cz.tieto.princegame.ThinkingStrategy.HEALTH;
import cz.tieto.princegame.common.gameobject.Field;

/**
 *
 * @author damo9117
 */
public class Knight {

    public static final int DEATH_HEALTH = 0;
    public static final String NAME = "knight";

    public static boolean isAlive(Field field) {
        return field != null && field.getObstacle() != null && NAME.equals(field.getObstacle().getName()) && !hasDeathHealth(field.getObstacle().getProperty(HEALTH));
    }

    public static boolean hasDeathHealth(String actual) {
        return Integer.parseInt(actual) <= DEATH_HEALTH;
    }
}
