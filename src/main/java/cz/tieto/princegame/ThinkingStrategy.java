package cz.tieto.princegame;

import cz.tieto.princegame.common.GameStrategy;
import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.action.Grab;
import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.action.JumpBackward;
import cz.tieto.princegame.common.action.JumpForward;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import cz.tieto.princegame.common.action.Use;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author damo9117
 */
public class ThinkingStrategy implements GameStrategy {

    private boolean goBack = true;
    private Map<Integer, Field> knownFields = new HashMap<Integer, Field>();
    private boolean heal = false;
    private Integer position = 0;
    private int prevHealth = 0;
    public final static String KNIGHT = "knight";
    public final static String DEAD = "dead";
    public final static String PITFALL = "pitfall";
    public final static String SWORD = "sword";
    public final static String HEALTH = "health";
    public final static String DRAGON = "dragon";
    private boolean jumpNeed = false;
    public final static String CHOPPER = "chopper";
    public final static String OPENING = "opening";
    public final static String CLOSING = "closing";
    public final static int MINIMAL_HEALTH_FOR_DRAGON = 4;

    public Action step(Prince prince) {

        final Field current = prince.look(0);
        if (current.isGate()) {
            return new EnterGate();
        }
        final Field backward = prince.look(-1);
        final Field forward = prince.look(1);
        knownFields.put(position, current);
        knownFields.put(position - 1, backward);
        knownFields.put(position + 1, forward);
        
        if (prevHealth - prince.getHealth() > 0) { // change of health
            if ((isKnight(forward) || isKnight(backward))
                    && prevHealth - prince.getHealth() == 1 && prince.getHealth() < 2) { 
                // prince lost one live and knight is next to prince
                heal = true;
                return moveAway(backward, forward);
            } else if ((isKnight(forward) || isKnight(backward))
                    && prevHealth - prince.getHealth() == 2 && prince.getHealth() < 3) { 
                // prince lost two lives and knight is next to prince and dragon behind
                heal = true;
                return moveAway(backward, forward);
            } else if ((isDragon(forward) || isDragon(backward))
                    && prevHealth - prince.getHealth() == 4
                    && prince.getHealth() < 5) { 
                // prince lost 4 lives and two dragons in queue
                jumpNeed = true;
                heal = true;
                return moveAway(backward, forward);
            } else if ((isDragon(forward) || isDragon(backward))
                    && prevHealth - prince.getHealth() == 3
                    && prince.getHealth() < MINIMAL_HEALTH_FOR_DRAGON) { 
                // prince lost 3 lives and dragon is next to
                jumpNeed = true;
                heal = true;
                return moveAway(backward, forward);
            } else if ((!isKnight(forward) && !isKnight(backward))
                    && prevHealth - prince.getHealth() == 1
                    && prince.getHealth() < MINIMAL_HEALTH_FOR_DRAGON) { 
                // prince lost one live and invisible dragon behind
                heal = true;
                return moveAway(backward, forward);
            }
        }
        prevHealth = prince.getHealth();
        if (backward != null && backward.isGate()) {
            return goBackward(backward); // ok only if has never met the gate before
        }
        if (forward != null && forward.isGate()) {
            return goForward(forward);
        }

        if (jumpNeed) {
            jumpNeed = false;
            return move(backward, forward);
        }

        if (heal && prince.getHealth() < prince.getMaxHealth()) {
            return new Heal();
        }
        heal = false;

        if (current.getEquipment() != null && SWORD.equals(current.getEquipment().getName())) {
            return new Grab();
        }

        // is wall?
        if (backward == null || forward == null) {
            goBack = !goBack;
        }

        if (!goBack) { // forward direction
            if (isKnight(forward)) {
                if (hasSword(prince)) { // fight
                    final Obstacle obstacle = forward.getObstacle();
                    return new Use(getSword(prince), obstacle);
                } else { // search sword
                    goBack = !goBack;
                    return goBackward(backward);
                }
            } else if (isDragon(forward)) {
                if (hasSword(prince)) { // fight
                    final Obstacle obstacle = forward.getObstacle();
                    return new Use(getSword(prince), obstacle);
                } else { // search sword
                    goBack = !goBack;
                    return goBackward(backward);
                }

            }
            return goForward(forward);
        } else {
            if (isKnight(backward)) {
                if (hasSword(prince)) {
                    final Obstacle obstacle = backward.getObstacle();
                    return new Use(getSword(prince), obstacle);
                } else {
                    goBack = !goBack;
                    return goForward(forward);
                }
            } else if (isDragon(backward)) {
                if (hasSword(prince)) {
                    final Obstacle obstacle = backward.getObstacle();
                    return new Use(getSword(prince), obstacle);
                } else {
                    goBack = !goBack;
                    return goForward(forward);
                }
            }
            return goBackward(backward);
        }
    }

    /**
     * @param backward
     * @param forward
     * @return
     */
    public Action moveAway(final Field backward, final Field forward) {
        if (goBack) { // move away
            return goForward(forward);
        } else {
            return goBackward(backward);
        }
    }

    /**
     * @return true if the prince already was there and place is safe
     */
    public boolean canJump(int jump) {
        final Field field = knownFields.get(position + jump);
        return field != null && !isPitfall(field) && !isKnight(field)&& !isChopper(field);
    }

    /**
     * pick up sword from equipments
     *
     * @param prince
     * @return
     */
    public Equipment getSword(Prince prince) {
        for (Equipment eq : prince.getInventory()) {
            if (SWORD.equals(eq.getName())) {
                return eq;
            }
        }
        return null;
    }

    /**
     * @param backward
     * @return
     */
    public Action goBackward(final Field backward) {
        if (isChopper(backward)) {
            if (isChopperOpen(backward)) {
                position -= 2;
                jumpNeed = false;
                return new JumpBackward();
            } else {
                return new Heal();
            }
        }
        if (isPitfall(backward) || canJump(-2)) {
            position -= 2;
            jumpNeed = false;
            return new JumpBackward();
        } else {
            position--;
            return new MoveBackward();
        }
    }

    public Action goForward(final Field forward) {
        if (isChopper(forward)) {
            if (isChopperOpen(forward)) {
                position += 2;
                jumpNeed = false;
                return new JumpForward();
            } else {
                return new Heal();
            }
        }
        if (isPitfall(forward) || canJump(2)) {
            position += 2;
            jumpNeed = false;
            return new JumpForward();
        } else { // unknown place
            position++;
            return new MoveForward();
        }
    }

    private boolean hasSword(Prince prince) {
        return getSword(prince) != null;
    }

    /**
     * returns if on the field is alive knight
     *
     * @param field
     * @return
     */
    public boolean isKnight(Field field) {
        return field != null && field.getObstacle() != null && KNIGHT.equals(field.getObstacle().getName())
                && "false".equals(field.getObstacle().getProperty(DEAD));
    }

    /**
     * returns if on the field is alive dragon
     *
     * @param field
     * @return
     */
    public boolean isDragon(Field field) {
        return field != null && field.getObstacle() != null && DRAGON.equals(field.getObstacle().getName())
                && "false".equals(field.getObstacle().getProperty(DEAD));
    }

    public boolean isPitfall(Field field) {
        return field != null && field.getObstacle() != null && PITFALL.equals(field.getObstacle().getName());
    }

    public boolean isChopper(Field field) {
        return field != null && field.getObstacle() != null && CHOPPER.equals(field.getObstacle().getName());
    }

    public boolean isChopperOpen(Field field) {
        return isChopper(field) && "true".equals(field.getObstacle().getProperty(ThinkingStrategy.OPENING)) 
                                && "false".equals(field.getObstacle().getProperty(ThinkingStrategy.CLOSING));
    }
    
    /**
     * @param backward
     * @param forward
     * @return
     */
    public Action move(final Field backward, final Field forward) {
        if (!goBack) {
            return goForward(forward);
        } else {
            return goBackward(backward);
        }
    }
}