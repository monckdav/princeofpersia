package cz.tieto.princegame;

import cz.tieto.princegame.common.GameStrategy;
import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

/**
 *
 * @author damo9117
 */
public class GoToGateStrategy implements GameStrategy {
    Field current;
    Field forward;
    Field backward;

    public GoToGateStrategy(Field current, Field forward, Field backward) {
        this.current = current;
        this.forward = forward;
        this.backward = backward;
    }

    public Action step(Prince prince) {
        if (current.isGate()) {
            return new EnterGate();
        }

        if (backward != null && backward.isGate()) {
            return new MoveBackward();
        }

        if (forward != null && forward.isGate()) {
            return new MoveForward();
        }
        return null;
    }
    

}
