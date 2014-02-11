package cz.tieto.princegame;

import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author damo9117
 */
public class ThinkingStrategyTest {
    
    private ThinkingStrategy strategy;
    
    @Before
    public void setUp() {
        strategy = new ThinkingStrategy();
    }

    /**
     * Test of isPitfall method, of class ThinkingStrategy.
     */
    @Test
    public void isPitfall() {
        assertTrue(strategy.isPitfall(DataFactory.createPitfall()));
        assertFalse(strategy.isPitfall(DataFactory.createField(null, null, true)));
        assertFalse(strategy.isPitfall(DataFactory.createField("sword", null, true)));
        assertTrue(strategy.isPitfall(DataFactory.createField("sword", "pitfall", false)));
    }

    @Test
    public void isKnight() {
        System.out.print(strategy.isKnight(DataFactory.createKnight(1)));
        assertTrue(strategy.isKnight(DataFactory.createKnight(1)));
        assertFalse(strategy.isKnight(DataFactory.createKnight(0)));
        assertFalse(strategy.isKnight(DataFactory.createKnight(-1)));
    }

    @Test
    public void goToGate() {
        assertNull(strategy.goToGate(DataFactory.createGate(false), DataFactory.createGate(false), DataFactory.createGate(false)));
        assertTrue(strategy.goToGate(DataFactory.createGate(true), DataFactory.createGate(false), DataFactory.createGate(false))
                instanceof EnterGate);
        assertTrue(strategy.goToGate(DataFactory.createGate(false), DataFactory.createGate(true), DataFactory.createGate(false))
                instanceof MoveForward);
        assertTrue(strategy.goToGate(DataFactory.createGate(false), DataFactory.createGate(false), DataFactory.createGate(true))
                instanceof MoveBackward);
    }

    @Test
    public void goToGateStrategy() {
        assertNull(new GoToGateStrategy(DataFactory.createGate(false), DataFactory.createGate(false), DataFactory.createGate(false)).step(null));
        assertTrue(new GoToGateStrategy(DataFactory.createGate(true), DataFactory.createGate(false), DataFactory.createGate(false)).step(null)
                instanceof EnterGate);
        assertTrue(new GoToGateStrategy(DataFactory.createGate(false), DataFactory.createGate(true), DataFactory.createGate(false)).step(null)
                instanceof MoveForward);
        assertTrue(new GoToGateStrategy(DataFactory.createGate(false), DataFactory.createGate(false), DataFactory.createGate(true)).step(null)
                instanceof MoveBackward);
    }
}