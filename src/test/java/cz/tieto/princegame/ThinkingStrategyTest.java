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
        assertTrue(strategy.isKnight(DataFactory.createKnight(1, false)));
        assertFalse(strategy.isKnight(DataFactory.createKnight(0, true)));
        assertFalse(strategy.isKnight(DataFactory.createKnight(-1, true)));
        assertTrue(strategy.isKnight(DataFactory.createKnight(-1, false)));
    }

    @Test
    public void goToGate() {
//        assertNull(strategy.goToGate(DataFactory.createGate(false), DataFactory.createGate(false), DataFactory.createGate(false)));
//        assertTrue(strategy.goToGate(DataFactory.createGate(true), DataFactory.createGate(false), DataFactory.createGate(false))
//                instanceof EnterGate);
//        assertTrue(strategy.goToGate(DataFactory.createGate(false), DataFactory.createGate(true), DataFactory.createGate(false))
//                instanceof MoveForward);
//        assertTrue(strategy.goToGate(DataFactory.createGate(false), DataFactory.createGate(false), DataFactory.createGate(true))
//                instanceof MoveBackward);
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
    
    @Test
    public void getSword() {
        assertNull(strategy.getSword(DataFactory.createPrince(false)));
        assertNotNull(strategy.getSword(DataFactory.createPrince(true)));
        assertEquals("sword", strategy.getSword(DataFactory.createPrince(true)).getName());
    }

    @Test
    public void isChopper() {
        assertTrue(strategy.isChopper(DataFactory.createChopper(true, false)));
        assertTrue(strategy.isChopper(DataFactory.createChopper(false, false)));
        assertTrue(strategy.isChopper(DataFactory.createChopper(true, true)));
        assertTrue(strategy.isChopper(DataFactory.createChopper(false, true)));
    }

    @Test
    public void isChopperOpen() {
        assertTrue(strategy.isChopperOpen(DataFactory.createChopper(true, false)));
        assertFalse(strategy.isChopperOpen(DataFactory.createChopper(false, false)));
        assertFalse(strategy.isChopperOpen(DataFactory.createChopper(true, true)));
        assertFalse(strategy.isChopperOpen(DataFactory.createChopper(false, true)));
    }

    @Test
    public void canJump() {
        assertFalse(strategy.canJump(-2));
        assertFalse(strategy.canJump(2));
    }
}