/**
 * *************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved. ************************************************************************************************
 */
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
import cz.tieto.princegame.common.gameobject.Prince;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author damo9117
 */
public class ThinkingStrategy implements GameStrategy {

    private boolean goBack = false;
    public final static String PITFALL = "pitfall";
    public final static String KNIGHT = "knight";
    public final static String SWORD = "sword";
    private int position = 0;
    private Map<Integer, Field> fields = new HashMap<Integer, Field>();

    public Action step(Prince prince) {
        if (prince == null) {
            System.err.println("Prince can not be null.");
            return null;
        }
        Field current = prince.look(0);
        Field backward = prince.look(-1);
        Field forward = prince.look(1);

        fields.put(position, current);
        fields.put(position-1, backward);
        fields.put(position+1, forward);

        final Equipment equipment = current.getEquipment();
        if (equipment != null) {
            return new Grab();
        }
        
        Action goToGate = goToGate(current, forward, backward);
        if (goToGate != null) {
            return goToGate;
        }
        
        if(prince.getHealth() <= 2) {
            return new Heal();
        }

        if (forward == null) {
            goBack = true;
        }

        if (backward == null) {
            goBack = false;
        }
        if (goBack) {
            if (isPitfall(backward)) {
                position = position - 2;
                return new JumpBackward();
            }
            if (isKnight(backward)) {
                if (hasSword(prince) && prince.getHealth() >= 2) {
                    return new Use(getSword(prince), backward.getObstacle());
                } else {
                    goBack = !goBack;
                    if (isPitfall(forward)) {
                        position=position-2;
                        return new JumpForward();
                    }else {
                        position--;
                        return new MoveForward();
                    }
                    
                }
            }
            position--;
            return new MoveBackward();
        // check if forward is obstacle and can be jumped
        } else {
            if (isPitfall(forward)) {
                position = position + 2;
                return new JumpForward();
            }
            if (isKnight(forward)) {
                if (hasSword(prince)&& prince.getHealth() >= 2) {
                    return new Use(getSword(prince), backward.getObstacle());
                } else {
                    goBack = !goBack;
                    if (isPitfall(backward)) {
                        position=position+2;
                        return new JumpBackward();
                    }else {
                        position++;
                        return new MoveBackward();
                    }
                    
                }
            }
            position ++;
            return new MoveForward();
        }

    }
    
    boolean isPitfall(Field field) {
        return field != null && field.getObstacle() != null && PITFALL.equals(field.getObstacle().getName());
    }
   boolean isKnight(Field field) {
        return field != null && field.getObstacle() != null && KNIGHT.equals(field.getObstacle().getName()) && !"0".equals(field.getObstacle().getProperty("HEALTH"));
    }
//   boolean isS(Field field) {
//        return field != null && field.getObstacle() != null && SWORD.equals(field.getObstacle().getName());
//    }

    private Action goToGate(Field current, Field forward, Field backward) {
        if (current.isGate()) {
            return new EnterGate();
        }

        if (backward != null && backward.isGate()) {
            position--;
            return new MoveBackward();
        }

        if (forward != null && forward.isGate()) {
            position++;
            return new MoveForward();
        }
        return null;
    }

    private boolean hasSword(Prince prince) {
        Equipment eq = getSword(prince);
        return eq != null;
    }

    private Equipment getSword(Prince prince) {
        for (Equipment eq : prince.getInventory()){
            if (SWORD.equals(eq.getName())) {
                return eq;
            }
        }
        return null;
    }
}
