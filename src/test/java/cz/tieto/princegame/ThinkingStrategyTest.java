/*
 * Copyright 2013 TeliaSonera. All rights reserved.
 */
package cz.tieto.princegame;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author damo9117
 */
public class ThinkingStrategyTest {
    
    ThinkingStrategy strategy = new ThinkingStrategy();
    
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Test of step method, of class ThinkingStrategy.
     */
    @Test
    public void testStep() {
    }

    /**
     * Test of isPitfall method, of class ThinkingStrategy.
     */
    @Test
    public void isPitfall() {
        new Field() {

            public Equipment getEquipment() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public Obstacle getObstacle() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public boolean isGate() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        strategy.isPitfall(null);
    }
}