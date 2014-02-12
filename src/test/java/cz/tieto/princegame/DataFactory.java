package cz.tieto.princegame;

import cz.tieto.princegame.client.gameobject.EquipmentImpl;
import cz.tieto.princegame.client.gameobject.FieldImpl;
import cz.tieto.princegame.client.gameobject.ObstacleImpl;
import cz.tieto.princegame.client.gameobject.PrinceImpl;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author damo9117
 */
public class DataFactory {

    public static Field createField(String equipmentName, String obstacleName, boolean gate) {
        return new FieldImpl(createEquipment(equipmentName), createObstacle(obstacleName), gate);
    }
    
    public static Field createPitfall() {
        return new FieldImpl(null, createObstacle(ThinkingStrategy.PITFALL), true);
    }

    public static Equipment createEquipment(String name) {
        return new EquipmentImpl(name, 1, null);
    }

    public static Obstacle createObstacle(String name) {
        return new ObstacleImpl(name, 1, null);
    }

    public static Obstacle createObstacle(String name, Map<String, String> properties) {
        return new ObstacleImpl(name, 1, properties);
    }

    public static Field createGate(boolean gate) {
        return new FieldImpl(null, null, gate);
    }

    public static Field createKnight(int life, boolean dead) {
        HashMap<String, String> properties = new HashMap<String, String> ();
        properties.put(ThinkingStrategy.HEALTH, Integer.toString(life));
        properties.put(ThinkingStrategy.DEAD, Boolean.toString(dead));
        return new FieldImpl(null, createObstacle(Knight.NAME, properties), false);
    }
    
    public static Prince createPrince(boolean sword) {
        Prince prince = new PrinceImpl (0,"prince", 5, 3, null, new ArrayList<Equipment>());
        if (sword) {
            prince.getInventory().add(createEquipment("sword"));
        }
        return prince;
    }
    
    
}
