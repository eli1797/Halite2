package hlt;

import hlt.Entity;
import hlt.GameMap;
import hlt.Planet;
import hlt.Ship;

import java.util.Map;

/**
 * By Elijah Bailey
 * A class for methods involved in locating game entities and other general
 * navigation functions
 */
public class GenNav {

    /**
     * Returns the nearest planet not owned by anyone
     * @param ship The ship that's looking for a nearby planet
     * @param gameMap The shared 2D playing board
     * @return Planet The closest unoccupied planet
     */
    public static Planet nearestUnownedPlanet(Ship ship, GameMap gameMap) {
        Planet nearest = null;
        Map<Double, Entity> entityMap = gameMap.nearbyEntitiesByDistance(ship);

        if (entityMap != null && !(entityMap.isEmpty())) {
            Double shortDist = null;
            for (Map.Entry<Double, Entity> entry : entityMap.entrySet()) {
                Entity ent = entry.getValue();
                Double dist = entry.getKey();
                if (!(ent instanceof Planet)) {
                    //skip this iteration
                    continue;
                } else {
                    Planet temp = (Planet) ent;
                    if (temp.isOwned()) {
                        //skip this iteration
                        continue;
                    }
                    if (shortDist == null) {
                        shortDist = dist;
                        nearest = temp;
                    } else if (shortDist > dist) {
                        nearest = temp;
                    }
                }
            }
        }
        return nearest;
    }


    /**
     * Returrns the nearest planet regardless of ownerness
     * @param ship The ship looking for a planet
     * @param gameMap the 2D playing field
     * @return Planet a game entity which is the nearest planet to the ship
     */
    public static Planet nearestPlanet(Ship ship, GameMap gameMap) {
        Planet nearest = null;
        Map<Double, Entity> entityMap = gameMap.nearbyEntitiesByDistance(ship);

        if (entityMap != null && !(entityMap.isEmpty())) {
            for (Map.Entry<Double, Entity> entry : entityMap.entrySet()) {
                Entity ent = entry.getValue();
                if (!(ent instanceof Planet)) {
                    //skip this iteration
                    continue;
                } else {
                    nearest = (Planet) ent;
                }
            }
        }
        return nearest;
    }

    /**
     * A method to find the nearest Docked Enemy Ship not owned by me
     * @param ship The ship owned by me looking for a ship to kill
     * @param gameMap The 2D playing field
     * @param playerID My int ID
     * @return The Ship entity closest to my ship that doesn't belong to me
     */
    public static Ship nearestDockedEnemyShip(Ship ship, GameMap gameMap, int
            playerID) {
        Ship nearest = null;
        Map<Double, Entity> entityMap = gameMap.nearbyEntitiesByDistance(ship);

        if (entityMap != null && !(entityMap.isEmpty())) {
            for (Map.Entry<Double, Entity> entry : entityMap.entrySet()) {
                Entity ent = entry.getValue();
                if (!(ent instanceof Ship)) {
                    //skip this iteration
                    continue;
                }
                Ship temp = (Ship) ent;
                if (temp.getDockingStatus() == Ship.DockingStatus.Undocked) {
                    continue;
                }
                if (temp.getOwner() == playerID) {
                    continue;
                } else {
                    nearest = temp;
                }
            }
        }
        return nearest;
    }
}
